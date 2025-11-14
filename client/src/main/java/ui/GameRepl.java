package ui;

import model.AuthData;

import java.util.Scanner;

public class GameRepl {
    private final GameClient client;

    public GameRepl(GameClient gameClient) throws Exception{
        client = gameClient;
    }

    public void run () {
        System.out.println("What would you like to do? ");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try{
                result = client.eval(line);
                System.out.print(result);
            } catch (Exception e){
                System.out.println("error: " + e.getMessage());
                printPrompt();
            }

        }


    }

    public void printPrompt(){
        System.out.print("\n" + "input: ");
    }
}
