package SceneController;

import Server.Controller.DuelController;
import javafx.stage.Stage;


public class SettingView {
    public static int rounds;
    public static Stage popup;

    public void surrender() throws Exception {
        popup.close();
        GamePlayView.stage.close();
        DuelController.getInstance().setWinner(GamePlayView.secondPlayersBoard, GamePlayView.firstPlayersBoard, rounds, true);

    }

    public void exit() {
        popup.close();
    }
}
