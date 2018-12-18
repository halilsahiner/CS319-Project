package client.RoomMenu;

import client.MenuController;
import client.QBitzApplication;
import client.UserConfiguration;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RoomMenuController extends MenuController {


    @FXML
    private TableView<Room> roomTable;

    private ArrayList<Room> roomArrayList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            getRoomList();
            addButtonToTable();
        });

        TableColumn<Room, String>  nameColumn = new TableColumn<>("Room Name");
        //nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Room, String>  modeColumn = new TableColumn<>("Game Mode");
        //modeColumn.setMinWidth(200);
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("gamemode"));

        TableColumn<Room, String>  ownerColumn = new TableColumn<>("Owner");
        //ownerColumn.setMinWidth(200);
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerid"));

        TableColumn<Room, String>  playerNoColumn = new TableColumn<>("Players");
        //playerNoColumn.setMinWidth(200);
        playerNoColumn.setCellValueFactory(new PropertyValueFactory<>("players"));

        TableColumn<Room, String>  levelColumn = new TableColumn<>("Entrance Level");
        //levelColumn.setMinWidth(200);
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("entranceLevel"));

        TableColumn joinColumn = new TableColumn("Join");
        //levelColumn.setMinWidth(200);

        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        roomTable.getColumns().addAll(nameColumn, modeColumn, ownerColumn,playerNoColumn,levelColumn, joinColumn);
    }

    public ObservableList<Room> populateRoomList(JSONArray roomList){
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        for (Object roomObj : roomList) {
            JSONObject roomJSON = (JSONObject) roomObj;
            rooms.add(new Room(
                    roomJSON.getString("name"),
                    roomJSON.getInt("id"),
                    roomJSON.getInt("gameMode"),
                    roomJSON.getInt("players"),
                    roomJSON.getInt("maxPlayers"),
                    roomJSON.getInt("entranceLevel")
                    )
            );
        }
        roomTable.getItems().addAll(rooms);
        return rooms;
    }

    private void getRoomList(){
        JSONObject getRoomsJSON = new JSONObject();
        getRoomsJSON.put("requestType", "displayRooms");
        QBitzApplication.getSceneController().sendMessageToServer(getRoomsJSON);
    }

    public void createRoom(ActionEvent actionEvent) {
        QBitzApplication.getSceneController().changeScene("RoomCreateMenu");
    }

    public void enterPrivateRoom(ActionEvent actionEvent) {
    }

    private void addButtonToTable() {
        TableColumn<Room, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = new Callback<TableColumn<Room, Void>, TableCell<Room, Void>>() {
            @Override
            public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
                final TableCell<Room, Void> cell = new TableCell<Room, Void>() {

                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Room room = getTableView().getItems().get(getIndex());
                            joinRoom(room.getId());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        roomTable.getColumns().add(colBtn);

    }

    private void joinRoom(int id){
        JSONObject roomJSON = new JSONObject();
        roomJSON.put("requestType", "joinRoom");
        roomJSON.put("id", id);
        QBitzApplication.getSceneController().sendMessageToServer(roomJSON);
    }

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("displayRooms")){
            if (responseJSON.getBoolean("result")){
                Platform.runLater(() -> {
                    populateRoomList(responseJSON.getJSONArray("roomList"));
                });
            }
        }
        else if(responseJSON.getString("responseType").equals("joinRoom")){
            int resultCode = responseJSON.getInt("result");
            if (resultCode == 0){
                Platform.runLater(() -> {
                    System.out.println("Joined");
                    QBitzApplication.getSceneController().changeScene("RoomLobbyMenu", responseJSON);
                });
            }
            else if(resultCode == 1){
                System.out.println("Room is full");
            }
            else if(resultCode == 2){
                System.out.println("Entrance level :(");
            }
            else if(resultCode == 3){
                System.out.println("Room does not exist :(");
            }
        }
    }
}
