package ui;

import server.ServerFacade;

import java.util.Arrays;

public class GameClient {

    private final ServerFacade server;


    public GameClient(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public String eval(String resp) throws Exception {
        try {
            String[] tokens = resp.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> "quit";
                case "2" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters");
        }
    }

    public String help(){
        return("""
                1. quit
                2. help
                """);
    }

}
