package websocket.commands;


import chess.ChessGame;

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
}
