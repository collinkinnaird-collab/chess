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
                case "2" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters");
        }
    }

    public String help(){
        return("""
                1. quit
                2. help
                """);
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getServerMessageType());
    }
}
