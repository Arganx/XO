package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Main;

import java.io.IOException;

/**
 * Created by qwerty on 12-May-17.
 */
public class StartMenuController {
    @FXML
    private RadioButton host;

    @FXML
    private RadioButton client;

    @FXML
    private Button next;

    @FXML
    private RadioButton x;

    @FXML
    private RadioButton o;

    @FXML
    private TextField id_text;

    private static String id_string;

    public static String getId_string() {
        return id_string;
    }

    private static boolean xOro;//for host:true = x, false = y

    public static boolean isxOro() {
        return xOro;
    }

    @FXML
    private void hostSelected()
    {
        client.setSelected(false);
        x.setDisable(false);
        o.setDisable(false);
    }

    @FXML
    private void clientSelected()
    {
        host.setSelected(false);
        x.setDisable(true);
        o.setDisable(true);
    }

    @FXML
    private void initialize() {
        x.setDisable(true);
        o.setDisable(true);
    }

    @FXML
    private void xSelected()
    {
        o.setSelected(false);
        xOro=true;
    }

    @FXML
    private void oSelected()
    {
        x.setSelected(false);
        xOro=false;
    }

    @FXML
    private void nextPressed() {
        id_string="localhost";
        if(id_text.getText()!=null)
        {
            id_string=id_text.getText();
        }
        if(host.isSelected()!=false&&(x.isSelected()==true||o.isSelected()==true))
        {
            System.out.println("Mode: Host");
            try {
                Parent plan = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
                Stage gameStage = new Stage();
                gameStage.setTitle("Host Mode");
                gameStage.setScene(new Scene(plan,1200,800));
                gameStage.show();
                Main.getPrimalstage().close();
            } catch (IOException e) {
                System.out.println("Could not load FXML file");
                e.printStackTrace();
            }
        }
        else if(client.isSelected()!=false)
        {
            //tu otwarcie nowego okna z gra i zamkniecie menu startowego oraz uruchomienie klienta, dodatkowo sprawdzenie czy serwer dziala
            System.out.println("Mode: Client");
            try
            {
            Parent plan = FXMLLoader.load(getClass().getResource("GameBoardClient.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Client Mode");
            gameStage.setScene(new Scene(plan,1200,800));
            gameStage.show();
            Main.getPrimalstage().close();
        } catch (IOException e) {
            System.out.println("Could not load FXML file");
            e.printStackTrace();
        }
        }
        else if(host.isSelected()!=false&&x.isSelected()==false&&o.isSelected()==false)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("As a host you have to chose what you want to play as");
            alert.setContentText("Please chose either X or O");

            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("The mode of the aplication has not been selected");
            alert.setContentText("Please select a mode: host or client");

            alert.showAndWait();
        }
    }
}
