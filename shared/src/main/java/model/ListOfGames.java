package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class ListOfGames extends ArrayList<GameData> {

    public ListOfGames() {


    }

    public ListOfGames(Collection<GameData> games) {
        super(games);
    }

    public String toString() {
        return new Gson().toJson(this.toArray());
    }

}
