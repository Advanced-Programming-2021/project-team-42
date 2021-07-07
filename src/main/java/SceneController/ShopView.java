package SceneController;


import javafx.stage.Stage;

public class ShopView {
    private static ShopView instance = null;
    private static Stage stage;

    private ShopView(){}

    public void start(Stage stage){
        ShopView.stage = stage;
    }



    public static ShopView getInstance(){
        if(instance == null)
            instance = new ShopView();
        return instance;
    }
}
