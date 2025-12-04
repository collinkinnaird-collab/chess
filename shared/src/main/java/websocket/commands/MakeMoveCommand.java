package websocket.commands;

import chess.ChessMove;
import model.GameData;

public class MakeMoveCommand {
    ChessMove move;
    GameData game;

    public MakeMoveCommand (ChessMove move, GameData game){
        this.move = move;
        this.game = game;
    }

    public ChessMove getMove(){
        return move;
    }

    public GameData getGame(){
        return game;
    }
}
