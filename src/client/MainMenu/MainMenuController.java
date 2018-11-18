package client.MainMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends MenuController {

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println(payload);
    }

    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }

    @FXML
    public void gotoSingleplayerMenu(){
        QBitzApplication.getSceneController().changeScene("SingleplayerMenu");
    }
    public void gotoLogin(){
        QBitzApplication.getSceneController().changeScene("LoginMenu");
    }
}
