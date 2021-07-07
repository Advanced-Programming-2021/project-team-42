package SceneController;

import javafx.stage.Stage;

public class DuelView {
    private static DuelView instance = null;
    private static Stage stage;

    public void start(Stage stage){
        DuelView.stage = stage;
    }


    public static DuelView getInstance(){
        if(instance == null)
            instance = new DuelView();
        return instance;
    }
}
