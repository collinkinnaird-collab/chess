package websocket.commands;


import chess.ChessGame;
import com.google.gson.Gson;

public class ConnectCommand extends UserGameCommand {

    int gameId;
    ChessGame.TeamColor color;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, String userName, ChessGame.TeamColor color, int gameId) {
        super(commandType, authToken, gameID, userName);
        this.color = color;
        this.gameId = gameId;
    }

    public int getGameId(){
        return gameId;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }

    public String toString() {return new Gson().toJson(this);}
}
