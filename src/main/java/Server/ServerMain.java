package Server;

import Server.Controller.*;
import Server.Model.Card;
import Server.Model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerMain {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        parsing();
        new Thread(ServerMain::adminThread).start();
        initialize();
    }

    public static void adminThread() {
        System.out.println("1. Set Limit\n" +
                "2. Increase Amount\n" +
                "3. Decrease Amount\n" +
                "4. Close Server");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                System.out.println("Enter the Desire Limit Number:");
                int limit = scanner.nextInt();
                ShopController.setLimit(limit);
                System.out.println("Done!");
                break;
            case "2": {
                System.out.println("Enter Card name:");
                String cardName = scanner.nextLine();
                if (Card.getCardByName(cardName) == null)
                    System.out.println("There is no card with given name");
                else {
                    System.out.println("Enter increasing count:");
                    int count = scanner.nextInt();
                    ShopController.increaseAmount(Card.getCardByName(cardName), count);
                    System.out.println("Done!");
                }
                break;
            }
            case "3": {
                System.out.println("Enter Card name:");
                String cardName = scanner.nextLine();
                if (Card.getCardByName(cardName) == null)
                    System.out.println("There is no card with given name");
                else {
                    System.out.println("Enter decreasing count:");
                    int count = scanner.nextInt();
                    ShopController.decreaseAmount(Card.getCardByName(cardName), count);
                    System.out.println("Done!");
                }
                break;
            }
            case "4": {
                RegisterController.rewriteData();
                DeckController.rewriteData();
                System.exit(0);
            }
            default:
                System.out.println("Please enter valid number");
                break;
        }
        scanner.nextLine();
        adminThread();
    }

    public static void parsing() {
        CardController.parseCards();
        DeckController.parseDecks();
        RegisterController.parseUsers();
    }

    public static void initialize() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("server socket created successfully");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");
                new Thread(() -> {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        while (true) {
                            String clientMessage = dataInputStream.readUTF();
                            String result = process(clientMessage);
                            dataOutputStream.writeUTF(result);
                            dataOutputStream.flush();
                        }
                    } catch (Exception e) {
                        System.out.println("user logged out");
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Can not instantiate Server Socket");
        }
    }

    public static String process(String clientMessage) {
        if (clientMessage.startsWith("signup")) {
            String[] messageParts = clientMessage.split(",");
            try {
                RegisterController.getInstance().createNewUser(messageParts[1], messageParts[2], messageParts[3]);
                return "success";
            } catch (Exception e) {
                return "error " + e.getMessage();
            }
        } else if (clientMessage.startsWith("login")) {
            String[] messageParts = clientMessage.split(",");
            try {
                return RegisterController.getInstance().loginUser(messageParts[1], messageParts[2]);
            } catch (Exception e) {
                return "error " + e.getMessage();
            }
        } else if (clientMessage.startsWith("logout")) {
            String token = clientMessage.substring(7);
            UserController.removeUser(token);
            return "success";
        } else if (clientMessage.startsWith("updateScoreBoard"))
            return ScoreBoardController.getInstance().showScoreboard();
        else if (clientMessage.startsWith("buyCard")) {
            String[] messageParts = clientMessage.split(",");
            String token = messageParts[1];
            String cardName = messageParts[2];
            try {
                if (UserController.getUserByToken(token) != null)
                    ShopController.getInstance().buyCard(UserController.getUserByToken(token).getUsername(), cardName);
                return "success";
            } catch (Exception e) {
                return "error " + e.getMessage();
            }
        } else if (clientMessage.startsWith("sellCard")) {
            String[] messageParts = clientMessage.split(",");
            String token = messageParts[1];
            String cardName = messageParts[2];
            try {
                ShopController.getInstance().sellCard(UserController.getUserByToken(token).getUsername(), cardName);
                return "success";
            } catch (Exception e) {
                return "error " + e.getMessage();
            }
        } else if (clientMessage.startsWith("cancel")){
            String[] messageParts = clientMessage.split(",");
            User user = UserController.getUserByToken(messageParts[2]);
            DuelController.removeUser(user);
            return "success";
        } else if (clientMessage.startsWith("newGame")){
            String[] messageParts = clientMessage.split(",");
            int rounds = Integer.parseInt(messageParts[1]);
            User user = UserController.getUserByToken(messageParts[2]);
            DuelController.addNewPlayer(rounds, user);
            return "success";
        } else if (clientMessage.startsWith("loadMessages")){
            return UserController.getAllMessages();
        } else if (clientMessage.startsWith("newMessage")){
            String[] messageParts = clientMessage.split(",");
            User user = UserController.getUserByToken(messageParts[1]);
            UserController.addNewMessage(messageParts[2], user.getUsername());
            return "success";
        }
        else
            return "WTF!";
    }
}
