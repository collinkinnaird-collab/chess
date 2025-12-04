package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.ListOfGames;
import model.UserData;
import server.ServerFacade;

import WebSocket.WebSocketFacade;
import WebSocket.NotificationHandler;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;

import java.awt.*;
import java.net.http.WebSocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GameClient implements NotificationHandler{

    private final ServerFacade server;
    private final WebSocketFacade ws;


    public GameClient(String serverURL) throws Exception {
        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
    }

    public String eval(String resp, AuthData userAuth) throws Exception {
        try {
            String[] tokens = resp.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> "quit";
                case "2" -> RedrawChessBoard();
                case "3" -> MakeMove(userAuth, params);
                case "4" -> Resign(userAuth);
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

    public String MakeMove(AuthData userAuth, String ... params) throws Exception {
        if(params.length > 1){

            ListOfGames personalGameList = server.listGames(userAuth);
            ChessPosition start = new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            ChessPosition end = new ChessPosition(Integer.parseInt(params[2]), Integer.parseInt(params[3]));
            ChessPiece piece = null;
            boolean found = false;
            GameData verifGame = null;
            ChessMove move = null;


            for (GameData games : personalGameList.games()){
                if (games.blackUsername().equals(userAuth.username()) || games.whiteUsername().equals(userAuth.username())){
                   piece = games.game().getBoard().getPiece(start);
                   verifGame = games;
                   move = new ChessMove(start, end, piece.getPieceType());
                   for (ChessMove playableMoves: games.game().validMoves(start)){
                       if (move == playableMoves){
                           found = true;
                       }
                   }

                }
            }
            if(found) {
                ws.MakeMove(userAuth.username(), userAuth.authToken(), verifGame, move);
                return "Success";
            }
        }

        return null;

    }

    public String Resign (AuthData userAuth){

        return "Success";
    }

    public String HighlightLegalMoves(String ... params){
        if(params.length > 1){

        }
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
