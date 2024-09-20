package examen2lab;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PSNUsers {
    private RandomAccessFile RAF;
    private HashTable users;

    public enum Trophy {
        PLATINO(5), ORO(3), PLATA(2), BRONCE(1);
        public final int points;
        Trophy(int points) {
            this.points = points;
        }
    }

    public PSNUsers() throws IOException {
        RAF = new RandomAccessFile("psn", "rw");
        this.users = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        users = new HashTable();
        RAF.seek(0);
        while (RAF.getFilePointer() < RAF.length()) {
            try {
                long posInicio = RAF.getFilePointer();
                String username = RAF.readUTF();
                RAF.readInt(); // puntos
                RAF.readInt(); // trofeos
                boolean activo = RAF.readBoolean();
                if (activo) {
                    users.add(username, posInicio);
                }
            } catch (IOException e) {
                System.out.println("Error durante la carga de la tabla hash: " + e.getMessage());
                break; // Romper el bucle si se produce una excepción
            }
        }
    }

    public boolean buscarUsername(String username) throws IOException {
        // Busca en la tabla hash de usuarios
        return users.search(username) != -1 || checkInactiveUser(username);
    }

    private boolean checkInactiveUser(String username) throws IOException {
        RAF.seek(0);
        while (RAF.getFilePointer() < RAF.length()) {
            long posInicio = RAF.getFilePointer();
            String user = RAF.readUTF();
            RAF.readInt(); // puntos
            RAF.readInt(); // trofeos
            boolean activo = RAF.readBoolean();
            if (user.equals(username) && !activo) {
                return true; // El usuario existe pero está inactivo
            }
        }
        return false; // No se encontró el usuario
    }

    public boolean addUser(String username) throws IOException {
        if (buscarUsername(username)) {
            throw new IOException("El usuario ya existe (activo o inactivo). No se puede agregar un usuario duplicado.");
        }
        RAF.seek(RAF.length());
        long posInicio = RAF.getFilePointer();
        RAF.writeUTF(username);
        RAF.writeInt(0);
        RAF.writeInt(0);
        RAF.writeBoolean(true); // Establecer como activo
        users.add(username, posInicio);
        return true;
    }

    public void deactivateUser(String username) throws IOException {
        long posicion = users.search(username);
        if (posicion != -1) {
            RAF.seek(posicion);
            RAF.readUTF(); // leer username
            RAF.readInt(); // leer puntos
            RAF.readInt(); // leer trofeos
            RAF.writeBoolean(false); // cambiar a inactivo

            users.remove(username);

            eliminarTrofeos(username);
            reloadHashTable(); // recargar la tabla hash para reflejar el cambio
        }
    }

    private void eliminarTrofeos(String username) throws IOException {
        RandomAccessFile tempTrophyFile = new RandomAccessFile("Trofeos_temp", "rw");
        RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw");
        trophyFile.seek(0);
        while (trophyFile.getFilePointer() < trophyFile.length()) {
            try {
                String user = trophyFile.readUTF();
                String tipo = trophyFile.readUTF();
                String juego = trophyFile.readUTF();
                String descripcion = trophyFile.readUTF();
                String fecha = trophyFile.readUTF();
                if (!user.equals(username)) {
                    tempTrophyFile.writeUTF(user);
                    tempTrophyFile.writeUTF(tipo);
                    tempTrophyFile.writeUTF(juego);
                    tempTrophyFile.writeUTF(descripcion);
                    tempTrophyFile.writeUTF(fecha);
                }
            } catch (IOException e) {
                System.out.println("Error al eliminar trofeos: " + e.getMessage());
                break;
            }
        }
        trophyFile.close();
        tempTrophyFile.close();
        new java.io.File("Trofeos").delete();
        new java.io.File("Trofeos_temp").renameTo(new java.io.File("Trofeos"));
    }

    public void addTrophieTo(String username, String trophyGame, String trophyName, String description, Trophy type) throws IOException {
        long posicion = users.search(username);
        if (posicion != -1) {
            RAF.seek(posicion);
            RAF.readUTF(); // leer username
            int puntos = RAF.readInt();
            int trophies = RAF.readInt();
            puntos += type.points;
            trophies++;
            RAF.seek(posicion + username.length() + 2);
            RAF.writeInt(puntos);
            RAF.writeInt(trophies);

            try (RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw")) {
                trophyFile.seek(trophyFile.length());
                trophyFile.writeUTF(username);
                trophyFile.writeUTF(type.name());
                trophyFile.writeUTF(trophyGame);
                trophyFile.writeUTF(trophyName);
                trophyFile.writeUTF(description);
                SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                trophyFile.writeUTF(fechaFormateada.format(new Date()));
            }
        }
    }

    public String playerInfo(String username) throws IOException {
        long posicion = users.search(username);
        StringBuilder info = new StringBuilder();
        if (posicion != -1) {
            RAF.seek(posicion);
            String user = RAF.readUTF();
            info.append("Usuario: ").append(user).append("\n");
            int puntos = RAF.readInt();
            info.append("Puntos: ").append(puntos).append("\n");
            int trophies = RAF.readInt();
            boolean isActive = RAF.readBoolean();
            info.append("Activo: ").append(isActive ? "Sí" : "No").append("\n");
            info.append("Cantidad de Trofeos: ").append(trophies).append("\n");

            try (RandomAccessFile trophyFile = new RandomAccessFile("Trofeos", "rw")) {
                trophyFile.seek(0);
                while (trophyFile.getFilePointer() < trophyFile.length()) {
                    try {
                        String userFromFile = trophyFile.readUTF(); // Leer usuario
                        String tipo = trophyFile.readUTF(); // Leer tipo de trofeo
                        String juego = trophyFile.readUTF(); // Leer nombre del juego
                        String nombreTrofeo = trophyFile.readUTF(); // Leer nombre del trofeo
                        String descripcion = trophyFile.readUTF(); // Leer descripción del trofeo
                        String fecha = trophyFile.readUTF(); // Leer fecha del trofeo

                        if (userFromFile.equals(username)) {
                            info.append("Fecha: ").append(fecha).append(" - ");
                            info.append("Tipo: ").append(tipo).append(" - ");
                            info.append("Juego: ").append(juego).append(" - ");
                            info.append("Nombre: ").append(nombreTrofeo).append(" - ");
                            info.append("Descripción: ").append(descripcion).append("\n");
                        }
                    } catch (IOException e) {
                        System.out.println("Error al leer información de trofeos: " + e.getMessage());
                        break;
                    }
                }
            }
        } else {
            info.append("Usuario no encontrado.");
        }
        return info.toString();
    }
}
