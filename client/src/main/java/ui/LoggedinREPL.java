package ui;

import model.AuthData;

import java.util.Scanner;

public class LoggedinREPL {

    private final LoggedinClient client;
    private final AuthData myAuth;
    private final String nextString;

    public LoggedinREPL(LoggedinClient loggedClient, String serverString, AuthData userAuth) throws Exception{
        client = loggedClient;
        myAuth = userAuth;
        nextString = serverString;
    }

    public void run () throws Exception {
        System.out.println("! What would you like to do today? ");
        System.out.print(client.help());

        GameClient gameTime = new GameClient(nextString);

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try{
                result = client.eval(line, myAuth);
                System.out.print(result);
                String firstWord = result.split(" ")[0];
                if(firstWord.equals("Entered")){
                    new GameRepl(gameTime, myAuth).run();
                    System.out.println( "\n" + client.help());
                }
            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
                printPrompt();
            }

        }


    }

    public void printPrompt(){
        System.out.print("\n" + "input: ");
    }
}
