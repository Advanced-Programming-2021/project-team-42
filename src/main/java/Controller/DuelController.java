package Controller;

public class DuelController {
    private static DuelController instance = null;

    private DuelController(){}

    public void startNewDuel(String player1, String player2, String rounds){}


    public static DuelController getInstance(){
        if(instance == null)
            instance = new DuelController();
        return instance;
    }
}
