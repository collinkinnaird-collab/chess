package ui;

import model.AuthData;
import server.ServerFacade;

import java.util.Arrays;

public class LoggedinClient {

    private final ServerFacade server;


    public LoggedinClient(String serverURL){
        server = new ServerFacade(serverURL);
    }

    public String eval(String resp, AuthData userAuth) throws Exception{
        try {
            String[] tokens = resp.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> createGame(userAuth, params);
                case "2" -> listGames(userAuth);
                case "3" -> playGame(userAuth, params);
                case "4" -> observeGame(userAuth, params);
                case "5" -> logout();
                case "6" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("DO LATER");
        }
    }

    public String createGame(AuthData userAuth, String... params) throws Exception {
        if(params.length > 0){
            String gameName = params[0];

            server.createGame(gameName, userAuth);
            return ( gameName + " has successfully been created!");

        }
        throw new Exception("incorrect parameters");
    }

    public String listGames(AuthData userAuth) throws Exception {
        server.listGames(userAuth);
        return( "\n"+"Here is the list!");
    }

    public String playGame(AuthData userAuth, String... params) throws Exception {
        if(params.length > 2) {
            Integer id = Integer.parseInt(params[0]);
            String playerColor = params[1];

            record UpdateGameData(String playerColor, Integer gameID) {}

            UpdateGameData newGame = new UpdateGameData(playerColor, id);

            server.playGame(newGame, userAuth);

            return ("you are now the " + playerColor + "of game" + id);

        }
        throw new Exception("incorrect parameters");
    }

    public String observeGame(AuthData userAuth, String... params) throws Exception {
        if(params.length > 1){
            Integer id = Integer.parseInt(params[0]);

            server.observeGame(id, userAuth);

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
