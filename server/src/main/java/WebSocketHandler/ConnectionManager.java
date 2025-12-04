package WebSocketHandler;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap <Integer, Session> connections = new ConcurrentHashMap<>();

    public void add(Integer gameId, Session session){
        connections.put(gameId, session);
    }

    public void remove(Integer gameId, Session session){
        connections.remove(gameId, session);
    }

    public void broadcast(Session excludeSession, ServerMessage message, String printable) throws IOException {
        String msg = message.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }




}
