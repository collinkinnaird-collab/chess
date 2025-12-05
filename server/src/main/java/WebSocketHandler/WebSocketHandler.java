package WebSocketHandler;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MySqlGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public WebSocketHandler(GameDAO gameDao, AuthDAO authDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                MakeMove(moveCommand.getGameID(), moveCommand.getAuthToken(), ctx.session, moveCommand.getUserName(), moveCommand.getMove());
            }
            else {
                switch (command.getCommandType()) {
                    case CONNECT ->
                            JoinGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                    //case MAKE_MOVE -> PlayTurn(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                    case LEAVE ->
                            LeaveGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                    case RESIGN ->
                            Resign(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            ErrorMessage throwError = new ErrorMessage("Invalid Auth!");
            String json = new Gson().toJson(throwError);
            MessageTime(ctx.session, json);
            return;
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void JoinGame(int gameId, String auth, Session session, String username) throws Exception {
        try {
            connections.add(gameId, session);
            ChessGame game = gameDao.getGame(gameId).game();
            LoadGame myMessage = new LoadGame(game);
            String json = new Gson().toJson(myMessage);
            var message = String.format("%s has joined the Game!", authDao.getName(auth));
            MessageTime(session, json);
            Notification notification = new Notification(message);
            String otherJson = new Gson().toJson(notification);
            connections.broadcast(session, otherJson, gameId);
        } catch (Exception e) {
            var message = String.format("player cannot connect with wrong id");
            ErrorMessage error = new ErrorMessage(message);
            String json = new Gson().toJson(error);
            MessageTime(session, json);

        }

    }

    public void MakeMove(int gameId, String auth, Session session, String username, ChessMove move) throws Exception {
        try{
        GameData gameData = gameDao.getGame(gameId);
        ChessGame game = gameData.game();
        ChessGame.TeamColor userColor;
        ChessGame.TeamColor otherColor;
        Notification notification;
        String name = authDao.getName(auth).username();


            if(authDao.getName(auth).username().equals(gameData.whiteUsername())) {
                userColor = ChessGame.TeamColor.WHITE;
                otherColor = ChessGame.TeamColor.BLACK;
            }
            else if(authDao.getName(auth).username().equals(gameData.blackUsername())) {
                userColor = ChessGame.TeamColor.BLACK;
                otherColor = ChessGame.TeamColor.WHITE;
            } else {
                userColor = null;
                otherColor = null;
            }

        if(game.getGameOver()){
            ErrorMessage throwError = new ErrorMessage("Can't make move, game's over!");
            String json = new Gson().toJson(throwError);
            MessageTime(session, json);
            return;
        }
        if(userColor == null){
            ErrorMessage throwError = new ErrorMessage("Can't make move when you're observing!");
            String json = new Gson().toJson(throwError);
            MessageTime(session, json);
            return;
        }

            String colorString = authDao.getName(auth).username().equals(gameData.whiteUsername()) ? "white" : "black";
            String start = toChessNotation(move.getStartPosition());
            String end = toChessNotation(move.getEndPosition());

            if(game.getTeamTurn().equals(userColor)) {
                game.makeMove(move);


                if(game.isInCheckmate(otherColor)){
                    notification = new Notification(String.format("Checkmate! %s wins", authDao.getName(auth)));
                    game.gameOver(true);
                }
                else if (game.isInStalemate(otherColor)){
                    notification = new Notification(String.format("Stalemate!"));
                    game.gameOver(true);
                }
                else if (game.isInCheck(otherColor)){
                    notification = new Notification(String.format("%s moved from %s to %s, %s is now in check !", colorString, start, end, otherColor));

                }
                else {
                    String message = String.format("%s moved from %s to %s", colorString, start, end);
                    notification = new Notification(message);
                }

                gameDao.updateGame(gameData, game);

                LoadGame newGame = new LoadGame(game);
                String json = new Gson().toJson(newGame);
                session.getRemote().sendString(json);
                connections.broadcast(session, json, gameId);
                String otherJson = new Gson().toJson(notification);
                connections.broadcast(session, otherJson, gameId);
            } else {
                ErrorMessage error = new ErrorMessage("It is not your turn");
                String json = new Gson().toJson(error);
                MessageTime(session, json);
            }

        } catch (InvalidMoveException e) {
            ErrorMessage error = new ErrorMessage("That's an invalid move!");
            String json = new Gson().toJson(error);
            MessageTime(session, json);
            return;
        }


    }

    public void LeaveGame(int gameId, String auth, Session session, String username) throws IOException {
        connections.remove(gameId, session);
        var message = String.format( "%s has left the Game!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, "", gameId);

    }

    public void Resign(int gameId, String auth, Session session, String username) throws IOException {
        var message = String.format( "%s has given up!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, "ServerMessage", gameId);

    }

    public void MessageTime(Session session, String json) throws IOException {
        session.getRemote().sendString(json);
    }

    private String toChessNotation(ChessPosition pos) {
        char file = (char) ('a' + pos.getColumn() - 1);
        char rank = (char) ('0' + pos.getRow());
        return "" + file + rank;
    }


}
