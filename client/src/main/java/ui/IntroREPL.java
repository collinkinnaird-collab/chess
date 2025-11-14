package ui;

import model.AuthData;

import java.util.Arrays;
import java.util.Scanner;

public class IntroREPL {
    private final IntroClient beginningClient;
    private final LoggedinClient secondClient;
    private final String serveString;

    public IntroREPL(String server) throws Exception{
        beginningClient = new IntroClient(server);
        secondClient = new LoggedinClient(server);
        serveString = server;
    }

    public void run (){
        System.out.println("Welcome to chess! register or login to start ");
        System.out.print(beginningClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = beginningClient.eval(line);
                System.out.print(result);
                String firstWord = result.split(" ")[0];
                if(firstWord.equals("you")){
                    new LoggedinREPL(secondClient, serveString, beginningClient.getAuthData()).run();
                    System.out.println( "\n" + beginningClient.help());
                }
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
