package websocket.commands;

public class ResignCommand extends UserGameCommand{

    int gameId;

    public ResignCommand(CommandType commandType, String authToken, Integer gameID, String userName, int gameId) {
        super(commandType, authToken, gameID, userName);
        this.gameId = gameId;

    }

    public int getGameId(){
        return gameId;
    }
}
