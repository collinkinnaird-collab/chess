package WebSocketHandler;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
    public final ConcurrentHashMap <Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameId, Session session){
        connections.computeIfAbsent(gameId, id -> new CopyOnWriteArrayList<>()).add(session);
    }

    public void remove(Integer gameId, Session session){
        List<Session> sessions = connections.get(gameId);

        if(sessions != null ) {
            sessions.remove(session);
        }
    }

    public void broadcast(Session excludeSession, String message, int gameId) throws IOException {

        List<Session> session = connections.get(gameId);
        for (Session c : session) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(message);
                }
            }
        }
    }




}
