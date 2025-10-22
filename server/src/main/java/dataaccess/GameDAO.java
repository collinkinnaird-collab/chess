package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {


    int createGame(String name) throws DataAccessException;

    GameData getGame(int ID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    boolean clear() throws DataAccessException;



}
