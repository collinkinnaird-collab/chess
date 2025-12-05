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
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                MakeMove(moveCommand.getGameID(), moveCommand.getAuthToken(), ctx.session, moveCommand.getUserName(), moveCommand.getMove());
            }
            switch (command.getCommandType()) {
                case CONNECT -> JoinGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                //case MAKE_MOVE -> PlayTurn(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                case LEAVE -> LeaveGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                case RESIGN -> Resign(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        GameData gameData = gameDao.getGame(gameId);
        ChessGame game = gameData.game();

        try{
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }



        gameDao.updateGame(gameData, game);

        LoadGame newGame = new LoadGame(game);
        String json = new Gson().toJson(newGame);
        session.getRemote().sendString(json);
        connections.broadcast(session, json, gameId);

        String colorString = authDao.getName(auth).equals(gameData.whiteUsername()) ? "white" : "black";
        String start = toChessNotation(move.getStartPosition());
        String end = toChessNotation(move.getEndPosition());
        String message = String.format("%s moved from %s to %s", colorString, start, end);

        Notification notification = new Notification(message);
        String otherJson = new Gson().toJson(notification);
        connections.broadcast(session, otherJson, gameId);

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
