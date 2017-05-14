package View;

import Server.SensorServer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by qwerty on 12-May-17.
 */
public class GameBoardControllerHost {
    @FXML
    private Canvas canvas00;
    @FXML
    private Canvas canvas01;
    @FXML
    private Canvas canvas02;
    @FXML
    private Canvas canvas10;
    @FXML
    private Canvas canvas11;
    @FXML
    private Canvas canvas12;
    @FXML
    private Canvas canvas20;
    @FXML
    private Canvas canvas21;
    @FXML
    private Canvas canvas22;
    @FXML
    private Button close;

    private boolean xo;

    private String host;

    private static boolean canMove;

    public static void setCanMove(boolean canMove) {
        GameBoardControllerHost.canMove = canMove;
    }

    private ArrayList<Canvas> tabcan=new ArrayList<Canvas>();
    private SensorServer server;
    Registry registry;

    void drawingEdges(boolean xoro,SensorServer server) throws IOException {
        BufferedImage bi;
        BufferedImage empty = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_emptyspace.png"));
        if(xoro==true)
        {
            bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_X.png"));
        }
        else
        {
            bi = ImageIO.read(new File("C:\\Users\\qwerty\\IdeaProjects\\XO\\out\\production\\XO\\Assets\\Tic_Tac_Toe_O.png"));
        }

        for (int i = 0; i < tabcan.size(); i++) {
            GraphicsContext gc = tabcan.get(i).getGraphicsContext2D();
            gc.drawImage(SwingFXUtils.toFXImage(empty, null), 0, 0);
            int finalI = i;
            tabcan.get(i).setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(canMove==true) {
                        try {
                            if(server.change(finalI,xo)==true)
                            {
                                canMove=false;
                                gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0, 0);
                                //System.out.println ("Sending information that: "+finalI+" was pressed");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    @FXML
    private void initialize() {
        try {
            canMove=true;
            xo = StartMenuController.isxOro();
            Collections.addAll(tabcan,canvas00,canvas01,canvas02,canvas10,canvas11,canvas12,canvas20,canvas21,canvas22);
            server=new SensorServer(xo,tabcan);
            drawingEdges(xo,server);
            registry = LocateRegistry.createRegistry(444);
            registry.bind( "RMI", server );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void  closeButtonPressed()
    {
        try {
            registry.unbind("RMI");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        //Platform.exit();
        System.exit(0);
    }
}
