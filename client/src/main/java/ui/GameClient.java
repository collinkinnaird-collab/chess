package ui;

import server.ServerFacade;

import WebSocket.WebSocketFacade;
import WebSocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.net.http.WebSocket;

import java.util.Arrays;

public class GameClient implements NotificationHandler{

    private final ServerFacade server;
    private final WebSocketFacade ws;


    public GameClient(String serverURL) throws Exception {
        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
    }

    public String eval(String resp) throws Exception {
        try {
            String[] tokens = resp.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> "quit";
                case "2" -> RedrawChessBoard();
                case "3" -> MakeMove(params);
                case "4" -> Resign();
                case "5" -> HighlightLegalMoves(params);
                case "6" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters");
        }
    }

    public String RedrawChessBoard(){

        return "Success";
    }

    public String MakeMove(String ... params){

        return "Success";
    }

    public String Resign (){

        return "Success";
    }

    public String HighlightLegalMoves(String ... params){

        return "Success";
    }

    public String help(){
        return("""
                1. quit
                2. Redraw chessboard
                3. Make a move (start position of piece, desired end position of piece)
                4. give up
                5. highlight legal moves (type position of piece)
                6. help
                """);
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getServerMessageType());
    }
}
