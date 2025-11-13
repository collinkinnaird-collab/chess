package ui;

import model.GameData;
import server.ServerFacade;

import java.util.Arrays;

public class LoggedinClient {

    private final ServerFacade server;


    public LoggedinClient(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public String eval(String resp) throws Exception{
        try {
            String[] tokens = resp.toLowerCase().split("");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> createGame(params);
                case "2" -> listGames();
                case "3" -> playGame(params);
                case "4" -> observeGame(params);
                case "5" -> logout();
                case "6" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("DO LATER");
        }
    }

    public String createGame(String... params) throws Exception {
        if(params.length > 1){
            String gameName = params[0];

            server.createGame(gameName);
            return ( gameName + " has successfully been created!");

        }
        throw new Exception("incorrect parameters");
    }

    public String listGames() throws Exception {
        server.listGames();
        return( "\n"+"Here is the list!");
    }

    public String playGame(String... params) throws Exception {
        if(params.length > 2) {
            Integer id = Integer.parseInt(params[0]);
            String playerColor = params[1];

            record UpdateGameData(String playerColor, Integer gameID) {}

            UpdateGameData newGame = new UpdateGameData(playerColor, id);

            server.playGame(newGame);

            return ("you are now the " + playerColor + "of game" + id);

        }
        throw new Exception("incorrect parameters");
    }

    public String observeGame(String... params) throws Exception {
        if(params.length > 1){
            Integer id = Integer.parseInt(params[0]);

            server.observeGame(id);

            return ("you are now observing game " + id);
        }

        throw new Exception("incorrect parameters");
    }

    public String logout() throws Exception{
        server.logout();
        return "quit";
    }

    public String help(){
        return("""
                1. create a game
                2. list all games
                3. play a game
                4. observe a game
                5. logout
                6. help
                """);
    }




}
