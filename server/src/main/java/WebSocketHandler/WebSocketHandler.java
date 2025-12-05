package WebSocketHandler;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MySqlGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDao;

    public WebSocketHandler(GameDAO gameDao) {
        this.gameDao = gameDao;
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
            switch (command.getCommandType()) {
                case CONNECT -> JoinGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                case MAKE_MOVE -> PlayTurn(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                case LEAVE -> LeaveGame(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
                case RESIGN -> Resign(command.getGameID(), command.getAuthToken(), ctx.session, command.getUserName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void JoinGame(int gameId, String auth, Session session, String username) throws IOException, DataAccessException {
        connections.add(gameId, session);
        ChessGame game = gameDao.getGame(gameId).game();
        LoadGame myMessage = new LoadGame(game);
        String json = new Gson().toJson(myMessage);
        var message = String.format( "%s has joined the Game!", username);

        Notification notification = new Notification(message + json);
        connections.broadcast(session, notification, gameId);

    }

    public void PlayTurn(int gameId, String auth, Session session, String username) throws IOException {
        var message = String.format( "%s's turn!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, gameId);

    }

    public void LeaveGame(int gameId, String auth, Session session, String username) throws IOException {
        connections.remove(gameId, session);
        var message = String.format( "%s has left the Game!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, gameId);

    }

    public void Resign(int gameId, String auth, Session session, String username) throws IOException {
        var message = String.format( "%s has given up!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, gameId);

    }

    public void Message(Session session, String json) throws IOException {
        session.getRemote().sendString(json);
    }

}
