package websocket.commands;

import com.google.gson.Gson;

public class LeaveCommand extends UserGameCommand{

    int gameId;

    public LeaveCommand(CommandType commandType, String authToken, Integer gameID, String userName, int gameId) {
        super(commandType, authToken, gameID, userName);
        this.gameId = gameID;
    }

    public int getGameId(){
        return gameId;
    }

    public String toString() {return new Gson().toJson(this);}
}
