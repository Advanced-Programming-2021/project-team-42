package Controller;

public class IAndEController {
    private static IAndEController instance = null;

    private IAndEController(){}

    public void importCard(String cardName){}

    public void exportCard(String cardName){}

    public static IAndEController getInstance(){
        if(instance == null)
            instance = new IAndEController();
        return instance;
    }
}
