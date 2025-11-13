package ui;

import model.AuthData;

import java.util.Scanner;

public class LoggedinREPL {

    private final LoggedinClient client;
    private final AuthData myAuth;

    public LoggedinREPL(LoggedinClient loggedClient, AuthData userAuth) throws Exception{
        client = loggedClient;
        myAuth = userAuth;
    }

    public void run (){
        System.out.println("What would you like to do today? ");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = client.eval(line, myAuth);
                System.out.print(result);
            } catch (Exception e){
                throw new RuntimeException();
            }

        }


    }

    public void printPrompt(){
        System.out.print("\n" + "input: ");
    }
}
