package View;

import Client.Client;
import Client.MoveListener;
import Server.MoveSensor;
import Server.SensorServer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by qwerty on 13-May-17.
 */
public class GameBoardClientController {
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

    private static boolean canMove;

    public static void setCanMove(boolean canMove) {
        GameBoardClientController.canMove = canMove;
    }

    private ArrayList<Canvas> tabcan=new ArrayList<Canvas>();

    private String host;

    private boolean xoro;

    @FXML
    private void initialize() {
        try {
            canMove=false;
            Collections.addAll(tabcan,canvas00,canvas01,canvas02,canvas10,canvas11,canvas12,canvas20,canvas21,canvas22);
            host = StartMenuController.getId_string();
            Registry register = LocateRegistry.getRegistry(host,444);
            Remote remoteService = register.lookup("RMI");
            MoveSensor server = (MoveSensor) remoteService;

            if(server.isXo()==true)
            {
                xoro=false;
            }
            else
            {
                xoro=true;
            }
            Client client = new Client(tabcan,xoro);
            server.addMoveListener(client);
            drawingEdges(xoro,server);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void drawingEdges(boolean xoro,MoveSensor server) throws IOException {
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
                            if(server.change_from_client(finalI,xoro)==true)
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
    private void closeButtonPressed()
    {
        System.exit(0);
    }
}
