package SceneController;

import Server.Controller.DuelController;
import Server.Controller.UserController;
import View.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DuelView {
    private static DuelView instance = null;
    private static Stage stage;
    public Rectangle backGround;
    public Label Error;
    public SplitMenuButton chooseRands;
    public String rands = null;
    public Button applyButton;
    public Button backButton;
    public TextField messageField;
    public VBox vBox;
    private int rounds;

    public void start(Stage stage) throws IOException {
        DuelView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/DuelScene.fxml"));
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public void initialize() {
        backGround.setFill(new ImagePattern(new Image(getClass().getResource("/Assets/DuelScenePhoto.png").toExternalForm())));
        setCursor(applyButton);
        setCursor(backButton);
        loadMessages();
    }

    private void loadMessages() {
        try {
            Main.dataOutputStream.writeUTF("loadMessages");
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            createVBox(vBox, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createVBox(VBox vBox, String result) {
        vBox.getChildren().clear();
        String[] ranks = result.split("\n");
        for (String rank : ranks) {
            Text text = new Text(rank + "\n");
            text.setFill(Color.BLACK);
            Font font = new Font("Book Antiqua", 24);
            text.setFont(font);
            vBox.getChildren().add(text);
            vBox.setAlignment(Pos.TOP_LEFT);
        }
    }

    public void setCursor(Button button) {
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setCursor(Cursor.HAND);
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setCursor(Cursor.DEFAULT);
            }
        });
    }


    public void applyClicked() {
        if (rands == null) {
            Error.setText("choose number of rounds");
            Error.setVisible(true);
            return;
        }

        try {
            Main.dataOutputStream.writeUTF("newGame," + rounds + "," + Main.token);
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
            if (result.equals("success")) {
                System.out.println("ishala Badan!!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static DuelView getInstance() {
        if (instance == null)
            instance = new DuelView();
        return instance;
    }


    public void exitClicked() {
        try {
            Main.dataOutputStream.writeUTF("cancel," + Main.token);
            Main.dataOutputStream.flush();
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void oneRandSelected(ActionEvent actionEvent) {
        rands = "1";
        rounds = Integer.parseInt(rands);
        chooseRands.setText("1 rand");
    }

    public void threeRandsSelected(ActionEvent actionEvent) {
        rands = "3";
        rounds = Integer.parseInt(rands);
        chooseRands.setText("3 rands");
    }

    public void sendMessage() {
        String message = messageField.getText();
        try {
            Main.dataOutputStream.writeUTF("newMessage," + Main.token + "," + message);
            Main.dataOutputStream.flush();
            String result = Main.dataInputStream.readUTF();
        } catch (Exception e){
            e.printStackTrace();
        }
        loadMessages();
    }

    public void refresh(MouseEvent mouseEvent) {
        loadMessages();
    }
}
