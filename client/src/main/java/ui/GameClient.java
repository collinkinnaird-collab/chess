package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.ListOfGames;
import server.ServerFacade;

import websocket.WebSocketFacade;
import websocket.NotificationHandler;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Arrays;

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
                case "2" -> redrawChessBoard();
                case "3" -> makeMove(userAuth, params);
                case "4" -> resign(userAuth);
                case "5" -> highlightLegalMoves(userAuth, params);
                case "6" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters");
        }
    }

    public String redrawChessBoard(){

        return "Success";
    }

    public String makeMove(AuthData userAuth, String ... params) throws Exception {
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
                   found = isFound(games, move, start );

                }
            }
            if(found) {
                MakeMoveCommand moves = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, userAuth.authToken()
                                                            , verifGame.gameID(), userAuth.username(), move,verifGame);
                ws.makeMove(moves);
                return "Success";
            }
        }

        return null;

    }

    public String resign (AuthData userAuth) throws Exception {

        ListOfGames personalGameList = server.listGames(userAuth);

        for (GameData games : personalGameList.games()) {
            if (games.blackUsername().equals(userAuth.username())) {

                ws.loseGame(userAuth.username(), userAuth.authToken(), games);
                return (games.blackUsername() + " gave up ... " + games.whiteUsername() + " wins! ");


            } else if (games.whiteUsername().equals(userAuth.username())) {

                ws.loseGame(userAuth.username(), userAuth.authToken(), games);
                return (games.whiteUsername() + " gave up ... " + games.blackUsername() + " wins! ");
            }

        }
        return null;
    }

    public String highlightLegalMoves(AuthData userAuth, String ... params) throws Exception {
        if(params.length > 1){
            ListOfGames personalGameList = server.listGames(userAuth);
            ChessPosition start = new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1]));

            for (GameData games : personalGameList.games()){
                if (games.blackUsername().equals(userAuth.username()) || games.whiteUsername().equals(userAuth.username())) {
                    for (ChessMove playableMoves: games.game().validMoves(start)) {

                    }
                }

            }
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

    public boolean isFound(GameData games, ChessMove move, ChessPosition start ){
        for (ChessMove playableMoves: games.game().validMoves(start)){
            if (move == playableMoves){
                return true;
            }
        }
        return true;
    }
}
