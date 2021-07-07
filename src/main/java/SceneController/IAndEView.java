package SceneController;

import Controller.IAndEController;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IAndEView {
    private static IAndEView instance = null;
    public static Stage stage;
    public TextField importedCard;
    public TextField exportedCard;
    public Label importReview;
    public Label exportReview;

    public void start(Stage stage) throws Exception{
        IAndEView.stage = stage;
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/IAndEScene.fxml"));
        Image image = new Image(getClass().getResource("/Assets/rsz_impexpbackground.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.5);
        pane.getChildren().add(0, imageView);
        Scene scene = new Scene(pane);
        IAndEView.stage.setScene(scene);
        stage.show();
    }

    public static IAndEView getInstance(){
        if(instance == null)
            instance = new IAndEView();
        return instance;
    }

    public void exportCheck() {
        try{
            IAndEController.getInstance().exportCard(exportedCard.getText());
            exportReview.setText("Card " + exportedCard.getText() + " Exported successfully");
            exportReview.setVisible(true);
        } catch (Exception e){
            exportReview.setText(e.getMessage());
            exportReview.setVisible(true);
        }
    }

    public void importCheck() {
        try{
            IAndEController.getInstance().importCard(importedCard.getText());
            importReview.setText("Card " + importedCard.getText() + " Imported successfully");
            importReview.setVisible(true);
        } catch (Exception e){
            importReview.setText(e.getMessage());
            importReview.setVisible(true);
        }
    }

    public void exitClicked() {
        try {
            MainView.getInstance().start(Main.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
