package client.Menus.RegisterMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMenuController extends MenuController {

    @FXML
    public TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void register(ActionEvent event){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String email = emailTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if(username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty() ){
            errorLabel.setText("Please fill all the fields");
            return;
        }
        else if(!password.equals(confirmPassword)){
            errorLabel.setText("Passwords do not match!");
            return;
        }
        JSONObject registerJSON = new JSONObject();
        registerJSON.put("requestType", "register");
        registerJSON.put("username", username);
        registerJSON.put("email", email);
        registerJSON.put("password", password);
        QBitzApplication.getSceneController().sendMessageToServer(registerJSON);
    }

    @FXML
    public void gotoLogin(){
        QBitzApplication.getSceneController().gotoMenu("LoginMenu");
    }

    @FXML
    public void goToVerificationMenu(){
        JSONObject payload = new JSONObject();
        payload.put("requestType", "verify");
        payload.put("email", emailTextField.getText());
        QBitzApplication.getSceneController().gotoMenu("VerificationMenu", payload);
    }

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("register")){
            if (responseJSON.getBoolean("result")){
                Platform.runLater(() -> {
                    goToVerificationMenu();
                });
            }
        }
    }
}
