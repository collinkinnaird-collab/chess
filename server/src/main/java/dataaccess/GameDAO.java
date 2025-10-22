package dataaccess;

import model.GameData;

public interface GameDAO {


    GameData createGame(GameData gameName) throws DataAccessException;

    GameData getGame(int ID) throws DataAccessException;

    void listGames() throws DataAccessException;

    boolean clear() throws DataAccessException;


}
