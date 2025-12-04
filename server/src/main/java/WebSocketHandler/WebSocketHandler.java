package WebSocketHandler;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void JoinGame(int gameId, String auth, Session session, String username) throws IOException {
        connections.add(gameId, session);
        var message = String.format( "%s has joined the Game!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, message);

    }

    public void PlayTurn(int gameId, String auth, Session session, String username) throws IOException {
        connections.add(gameId, session);
        var message = String.format( "%s has joined the Game!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, message);

    }

    public void LeaveGame(int gameId, String auth, Session session, String username) throws IOException {
        connections.add(gameId, session);
        var message = String.format( "%s has left the Game!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, message);

    }

    public void Resign(int gameId, String auth, Session session, String username) throws IOException {
        connections.add(gameId, session);
        var message = String.format( "%s has given up!", username);
        var ServerMessage = new ServerMessage(websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, ServerMessage, message);

    }

}
