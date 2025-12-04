package WebSocketHandler;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

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




}
