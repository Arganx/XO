package Client;

import View.GameBoardClientController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by qwerty on 13-May-17.
 */
public class Client extends UnicastRemoteObject implements MoveListener {

    ArrayList<Canvas> canvass;
    private boolean xoro;
    public Client(ArrayList<Canvas> list,boolean xoro) throws RemoteException {
        canvass=list;
        this.xoro=xoro;
    }

    @Override
    public void moved(int field) throws RemoteException
    {
        //to ma sie wydarzyc po wykryciu nacisniecia pola po stronie ?hosta?
        //Pojawia sie po stronie klienta

        //System.out.println("Ciekawe gdzie to sie pojawi???");
        BufferedImage bi= null;
        try {
            bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_emptyspace.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(xoro==true)
        {
            try {
                bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_O.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_X.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        GameBoardClientController.setCanMove(true);
        GraphicsContext gc = canvass.get(field).getGraphicsContext2D();
        gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0, 0);
    }

    @Override
    public void end(int how) throws RemoteException {
        if(how==2)
        {
            System.out.println("You win");
        }
        else
        {
            System.out.println("You lose");
        }
        GameBoardClientController.setCanMove(false);
    }
}
