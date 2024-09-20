package examen2lab;

public class HashTable {
    private Entry principal;

    public void add(String username, long pos) {
        Entry nuevoElemento = new Entry(username, pos);
        if (principal == null) {
            principal = nuevoElemento;
        } else {
            Entry temp = principal;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevoElemento);
        }
    }

    public void remove(String username) {
        if (principal == null) return;
        if (principal.getUsername().equals(username)) {
            principal = principal.getSiguiente();
            return;
        }
        Entry temp = principal;
        while (temp.getSiguiente() != null) {
            if (temp.getSiguiente().getUsername().equals(username)) {
                temp.setSiguiente(temp.getSiguiente().getSiguiente());
                return;
            }
            temp = temp.getSiguiente();
        }
    }

    public long search(String username) {
        Entry temp = principal;
        while (temp != null) {
            if (temp.getUsername().equals(username)) {
                return temp.getPosicion();
            }
            temp = temp.getSiguiente();
        }
        return -1; // Usuario no encontrado
    }
}
