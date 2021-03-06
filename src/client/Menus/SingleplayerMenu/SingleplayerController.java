package client.Menus.SingleplayerMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import client.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleplayerController extends MenuController {

    @FXML
    private Button taEasy;
    @FXML
    private Button taNormal;
    @FXML
    private Button taHard;
    @FXML
    private Button irEasy;
    @FXML
    private Button irNormal;
    @FXML
    private Button irHard;
    @FXML
    private Button memEasy;
    @FXML
    private Button memNormal;
    @FXML
    private Button memHard;


    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonMethods();
    }

    private void setButtonMethods()
    {
        taEasy.setOnAction(e-> startTimeGame(3));
        taNormal.setOnAction(e-> startTimeGame(4));
        taHard.setOnAction(e-> startTimeGame(5));
        irEasy.setOnAction(e-> startImageGame(3));
        irNormal.setOnAction(e-> startImageGame(4));
        irHard.setOnAction(e-> startImageGame(5));
        memEasy.setOnAction(e-> startMemoryGame(3));
        memNormal.setOnAction(e-> startMemoryGame(4));
        memHard.setOnAction(e-> startMemoryGame(5));
    }

    @FXML
    public void startTimeGame(int boardSize){
        JSONObject payload = new JSONObject();
        payload.put("boardSize", boardSize);

        QBitzApplication.getSceneController().gotoGameMode(SceneController.SP, "TimeAttackMode", payload);
    }

    @FXML
    public void startMemoryGame(int boardSize){
        JSONObject payload = new JSONObject();
        payload.put("boardSize", boardSize);
        QBitzApplication.getSceneController().gotoGameMode(SceneController.SP, "MemoryMode", payload);
    }

    @FXML
    public void startImageGame(int boardSize){
        JSONObject payload = new JSONObject();
        payload.put("boardSize", boardSize);
        QBitzApplication.getSceneController().gotoGameMode(SceneController.SP, "SingleImageRecreationMode", payload);
    }
}
