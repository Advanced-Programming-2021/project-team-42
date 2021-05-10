package Controller;

public class CardController {
    private static CardController instance = null;

    private CardController(){}


    public static CardController getInstance() {
        if (instance == null)
            instance = new CardController();
        return instance;
    }
}
