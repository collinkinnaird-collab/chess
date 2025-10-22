package model;

import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername
                        , String gameName, ChessGame game) {

    public GameData createNewGame(int newgameID, String newwhiteUsername, String newblackUsername
            , String newgameName, ChessGame newgame) {
        return new GameData (newgameID, newwhiteUsername, newblackUsername, newgameName, newgame);
    }


    public String toString() {
        return new Gson().toJson(this);
    }
}
