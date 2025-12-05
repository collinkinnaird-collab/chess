package ui;

import WebSocket.NotificationHandler;
import WebSocket.WebSocketFacade;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.ListOfGames;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import java.util.Arrays;


public class LoggedinClient implements NotificationHandler {

    private final ServerFacade server;
    private final WebSocketFacade ws;


    public LoggedinClient(String serverURL) throws Exception {
        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
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
                case "5" -> logout(userAuth);
                case "6" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters ");
        }

    }

    public String createGame(AuthData userAuth, String... params) throws Exception {
        if(params.length == 1){
            String gameName = params[0];

            server.createGame(gameName, userAuth);
            return ( gameName + " has successfully been created!");

        }
        throw new Exception("incorrect parameters");
    }

    public String listGames(AuthData userAuth) throws Exception {

        ListOfGames allGames = server.listGames(userAuth);
        for (GameData games : allGames.games()){
            System.out.println(games.gameID() + " " + games.gameName() + ", players: white = " + games.whiteUsername()
                                + " black = " + games.blackUsername());
        }


        return String.format("\n"+" Here is the list! ");
    }

    public String playGame(AuthData userAuth, String... params) throws Exception {
        if(params.length == 2) {
            Integer id = Integer.parseInt(params[0]);
            String playerColor = params[1].toUpperCase();

            record UpdateGameData(String playerColor, Integer gameID) {}

            UpdateGameData newGame = new UpdateGameData(playerColor, id);

            server.playGame(newGame, userAuth);
            for (GameData games: server.listGames(userAuth).games()){
                if(games.blackUsername().equals(userAuth.username()) || games.whiteUsername().equals(userAuth.username())){
                    ws.EnterGame(userAuth.username() , userAuth.authToken(), games);
                    break;
                }
            }

            DrawBoard chessBoard = new DrawBoard();
            chessBoard.freshBoard(playerColor);

            return String.format("Entered game! You are now the " + playerColor + " of game " + id);

        }
        throw new Exception("incorrect parameters");
    }

    public String observeGame(AuthData userAuth, String... params) throws Exception {
        if(params.length == 1){
            Integer id = Integer.parseInt(params[0]);

            DrawBoard chessBoard = new DrawBoard();
            chessBoard.freshBoard("WHITE");

            return String.format("Entered game! you are now observing game " + id);
        }

        throw new Exception("incorrect parameters");
    }

    public String logout(AuthData userAuth) throws Exception{
        server.logout(userAuth);
        return "quit";
    }

    public String help(){
        return("""
                1. create a game (game name)
                2. list all games
                3. play a game (game id, chess color)
                4. observe a game (game id)
                5. logout
                6. help
                """);
    }


    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getServerMessageType());
    }
}
