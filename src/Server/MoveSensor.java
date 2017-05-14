package Server;

import Client.MoveListener;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by qwerty on 13-May-17.
 */
public interface MoveSensor extends Remote {
    public void addMoveListener(MoveListener moveListener) throws RemoteException;
    public void removeMoveListener(MoveListener moveListener) throws RemoteException;
    public boolean change(int field,boolean xoro)throws RemoteException;
    public boolean isXo()throws RemoteException;
    public boolean change_from_client(int field,boolean xoro) throws RemoteException;
}
