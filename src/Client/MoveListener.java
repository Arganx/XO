package Client;

/**
 * Created by qwerty on 13-May-17.
 */
public interface MoveListener extends java.rmi.Remote {//wywolywany gdy zmiana pola w tablicy po stronie servera
    public void moved(int field) throws  java.rmi.RemoteException;
    public void end(int how) throws  java.rmi.RemoteException;
}
