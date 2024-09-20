package examen2lab;
import examen2lab.HashTable.Trophy;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class PSNUsers {
    RandomAccessFile RAF;
    HashTable users;
    
    public PSNUsers() throws IOException{
        RAF=new RandomAccessFile("psn", "rw");
        this.users=new HashTable();
        reloadHashTable();
    }
    
    private void reloadHashTable() throws IOException{
        RAF.seek(0);
        while(RAF.getFilePointer()<RAF.length()){
            String username=RAF.readUTF();
            RAF.skipBytes(9);
            long posicion=RAF.getFilePointer();
            users.add(username, posicion);
        }
    }

    
    public boolean buscarUsername(String username) throws IOException{
        RAF.seek(0);
        while (RAF.getFilePointer()<RAF.length()) {
            String user=RAF.readUTF();
            RAF.readInt();
            RAF.readInt();
            RAF.readBoolean();
            if(user.equals(username))
                return true;
        }
        return false;
    }
    
    public void addUser(String username) throws IOException{                
        if (buscarUsername(username))
            JOptionPane.showMessageDialog(null,"El usuario ya existe","Atención",0);
        else {
            RAF.seek(RAF.length());
            RAF.writeUTF(username);
            RAF.writeInt(0); 
            RAF.writeInt(0);
            RAF.writeBoolean(true);
            users.add(username,RAF.getFilePointer());
        }
    }

    public void deactivateUser(String username) throws IOException{
        long posicion=users.Search(username);
        if(posicion!=-1){
            RAF.seek(posicion);
            RAF.writeBoolean(false);
            users.remove(username);
        }
    }
    
    public void addTrophieTo(String username,String trophyGame,String trophyName,Trophy type) throws IOException{
        long posicion=users.Search(username);
        if(posicion!=-1){
            RAF.seek(posicion-9);
            int puntos=RAF.readInt();
            int trophies=RAF.readInt();
            puntos+=type.puntos;
            trophies++;            
            RAF.seek(posicion-9);
            RAF.writeInt(puntos);
            RAF.writeInt(trophies);
            
            RandomAccessFile trophyFile=new RandomAccessFile("Trofeos", "rw");
            trophyFile.seek(trophyFile.length());
            trophyFile.writeUTF(username);
            trophyFile.writeUTF(type.name());
            trophyFile.writeUTF(trophyGame);
            trophyFile.writeUTF(trophyName);
            SimpleDateFormat fechaFormateada=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            Date fecha=new Date();  
            trophyFile.writeUTF(fechaFormateada.format(fecha));
        }
    }

    public String playerInfo(String username) throws IOException{
        long posicion=users.Search(username);
        StringBuilder info = new StringBuilder();
        if(posicion!=-1){
            RAF.seek(posicion-9-username.length()-2);
            String user=RAF.readUTF();
            info.append("Usuario: "+user+"\n");
            int puntos=RAF.readInt();
            info.append("Puntos: "+puntos+"\n");
            int trophies=RAF.readInt();
            info.append("Activo: "+RAF.readBoolean()+"\n");
            info.append("Cantidad de Trofeos: "+trophies+"\n");

            RandomAccessFile trophyFile=new RandomAccessFile("Trofeos", "rw");
            trophyFile.seek(0);
            while (trophyFile.getFilePointer()<trophyFile.length()){
                if(trophyFile.readUTF().equals(username)){
                    info.append(trophyFile.readUTF()+" - "+trophyFile.readUTF()+" - "+trophyFile.readUTF()
                            +" - "+trophyFile.readUTF()+"\n");
                }else{
                    RAF.skipBytes(9+username.length()+2);
                }
            }
        }
        return info.toString();
    }
    

}
