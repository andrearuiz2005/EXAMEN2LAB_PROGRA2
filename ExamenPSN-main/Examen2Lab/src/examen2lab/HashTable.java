package examen2lab;

public class HashTable {
    Entry principal;
    
    public void add(String username, long pos){
        Entry elementoNuevo=new Entry(username,pos);
        if(principal==null){
            principal=elementoNuevo;
        }else{
            Entry temp=principal;
            while(temp.siguiente!=null){
                temp=temp.siguiente;
            }
            temp.siguiente=elementoNuevo;
        }
    }
    
    public void remove(String username){
        if(principal==null) 
            return;
        if(principal.username.equals(username)){
            principal=principal.siguiente;
            return;
        }
        Entry temp=principal;
        while(temp.siguiente!=null){
            if(temp.siguiente.username.equals(username)){
                temp.siguiente=temp.siguiente.siguiente;
                return;
            }
            temp=temp.siguiente;
        }
    }
    
    public long Search(String username){
        Entry temp=principal;
        while(temp!=null){
            if(temp.username.equals(username)) 
                return temp.posicion;            
            temp=temp.siguiente;
        }
        return -1;
    }
    
    public enum Trophy{
        PLATINO(5),ORO(3),PLATA(2),BRONCE(1);

        public final int puntos;

        Trophy(int puntos) {
            this.puntos=puntos;
        }
    }
}
