package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public record ListOfGames(Collection<GameData> games){

    public String toString() {
        return new Gson().toJson(this);
    }
}
