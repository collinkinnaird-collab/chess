package ui;

import java.util.Scanner;

public class LoggedinREPL {

    private final LoggedinClient client;

    public LoggedinREPL(LoggedinClient loggedClient) throws Exception{
        client = loggedClient;
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
                result = client.eval(line);
                System.out.print(result);
            } catch (Exception e){
                throw new RuntimeException();
            }

        }


    }
}
