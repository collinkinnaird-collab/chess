package ui;

import model.AuthData;

import java.util.Arrays;
import java.util.Scanner;

public class IntroREPL {
    private final IntroClient beginningClient;
    private final LoggedinClient secondClient;

    public IntroREPL(String server) throws Exception{
        beginningClient = new IntroClient(server);
        secondClient = new LoggedinClient(server);
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
                    new LoggedinREPL(secondClient, beginningClient.getAuthData()).run();
                }
            } catch (Exception e){
                throw new RuntimeException();
            }

        }


    }

    public void printPrompt(){
        System.out.print("\n" + "input: ");
    }


}
