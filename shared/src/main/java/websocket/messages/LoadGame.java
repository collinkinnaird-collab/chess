package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGame extends ServerMessage {

    ChessGame game;

    public LoadGame(ChessGame gamer) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gamer;
    }

    public ChessGame getGame(){
        return game;
    }

    public String toString() {return new Gson().toJson(this);}
}
