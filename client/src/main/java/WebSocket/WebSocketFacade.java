package WebSocket;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception("FAILED");
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void EnterGame(String username, String authToken, GameData game) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, game.gameID(), username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void ExitGame(String username, String authToken, GameData game) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, game.gameID(), username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void LoseGame(String username, String authToken, GameData game) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, game.gameID(), username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void MakeMove(String username, String authToken, GameData game, ChessMove move) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, game.gameID(), username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e){
            throw new Exception(e);
        }
    }



}
