package Server;

import Client.MoveListener;
import View.GameBoardControllerHost;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by qwerty on 13-May-17.
 */
public class SensorServer extends UnicastRemoteObject implements MoveSensor {
    private Vector list = new Vector();
    private Integer[] tab= new Integer[9];
    private boolean xo;
    ArrayList<Canvas> canvass;

    private int win=0;

    public SensorServer(boolean xoro,ArrayList<Canvas> can) throws RemoteException
    {
        super();
        for(int i=0;i<9;i++)
        {
            tab[i]=0;
        }
        xo=xoro;
        canvass=can;
    }

    @Override
    public boolean isXo()throws RemoteException {
        return xo;
    }

    @Override
    public void addMoveListener(MoveListener moveListener) throws RemoteException {
        System.out.println ("adding listener -" + moveListener);
        list.add (moveListener);
    }

    @Override
    public void removeMoveListener(MoveListener moveListener) throws RemoteException {
        System.out.println ("removing listener -" + moveListener);
        list.remove (moveListener);
    }

    @Override
    public boolean change(int field,boolean xoro)throws RemoteException
    {
        if(tab[field]==0) {
            if (xo == true) {
                tab[field] = 2;
            } else {
                tab[field] = 1;
            }
            notifyListeners(field);
            if(didanyonewin()!=0)
            {
                System.out.println("You win");  //dodac blokade ruchu dla klienta
                win=1;
                notifyEnd(win);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean change_from_client(int field, boolean xoro) throws RemoteException {
        if(tab[field]==0) {
            GraphicsContext gc = canvass.get(field).getGraphicsContext2D();
            BufferedImage bi = null;
            try {
                bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_emptyspace.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
            if (xo == true) {
                tab[field] = 1;
                try {
                    bi = bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_O.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                tab[field] = 2;
                try {
                    bi = bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_X.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0, 0);
            GameBoardControllerHost.setCanMove(true);
            if(didanyonewin()!=0)
            {
                GameBoardControllerHost.setCanMove(false);
                System.out.println("You lose");
                win=2;
                notifyEnd(win);
                return true;
            }
            return true;
        }
        return false;
    }

    private int row(int start,int finish)
    {
        int counter=0;
        for(int i=start;i<finish;i++) {
            if(tab[i]==1)
            {
                counter++;
            }
            if(counter==3)
            {
                return 1;
            }
        }
        counter=0;
        for(int i=start;i<finish;i++) {
            if(tab[i]==2)
            {
                counter++;
            }
            if(counter==3)
            {
                return 2;
            }
        }

        return 0;
    }

    private int column(int number)
    {
        int counter=0;

        for(int i=number;i<6;i=i+3)
        {
            if(tab[i]==1)
            {
                counter++;
            }
            if(counter==3)
            {
                return 1;
            }
        }
        counter=0;
        for(int i=number;i<6;i=i+3)
        {
            if(tab[i]==2)
            {
                counter++;
            }
            if(counter==3)
            {
                return 2;
            }
        }
        return 0;
    }

    private int didanyonewin()
    {
        int result=row(0,3);
        if(result!=0)
        {
            return result;
        }
        result=row(3,6);
        if(result!=0)
        {
            return result;
        }
        result=row(6,9);
        if(result!=0)
        {
            return result;
        }
        result=column(0);
        if(result!=0)
        {
            return result;
        }
        result=column(1);
        if(result!=0)
        {
            return result;
        }
        result=column(2);
        if(result!=0)
        {
            return result;
        }

        for(int i=0;i<9;i=i+4)
        {
            if(tab[i]==1)
            {
                result++;
            }
            if(result==3)
            {
                return 1;
            }
        }
        result=0;
        for(int i=0;i<9;i=i+4)
        {
            if(tab[i]==2)
            {
                result++;
            }
            if(result==3)
            {
                return 2;
            }
        }
        result=0;
        for(int i=2;i<7;i=i+2)
        {
            if(tab[i]==1)
            {
                result++;
            }
            if(result==3)
            {
                return 1;
            }
        }
        result=0;
        for(int i=2;i<7;i=i+2)
        {
            if(tab[i]==2)
            {
                result++;
            }
            if(result==3)
            {
                return 2;
            }
        }
        return 0;
    }

    private void notifyListeners(int field)
    {
        for (Enumeration e = list.elements(); e.hasMoreElements(); )
        {
            MoveListener listener = (MoveListener) e.nextElement();
            try
            {
                listener.moved (field);
            }
            catch (RemoteException re)
            {
                System.out.println ("removing listener -" + listener);
                list.remove( listener );
            }
        }
    }

    private void notifyEnd(int how)
    {
        for (Enumeration e = list.elements(); e.hasMoreElements(); )
        {
            MoveListener listener = (MoveListener) e.nextElement();
            try
            {
                listener.end(how);
            }
            catch (RemoteException re)
            {
                System.out.println ("removing listener -" + listener);
                list.remove( listener );
            }
        }
    }
}
