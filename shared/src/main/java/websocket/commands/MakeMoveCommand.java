package websocket.commands;

import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;
    GameData game;

    public MakeMoveCommand (CommandType commandType, String authToken, Integer gameID, String userName, ChessMove move, GameData game){
        super(commandType, authToken, gameID, userName);
        this.move = move;
        this.game = game;
    }

    public ChessMove getMove(){
        return move;
    }

    public GameData getGame(){
        return game;
    }

    public String toString() {return new Gson().toJson(this);}
}
