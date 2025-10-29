package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{


    public MySqlGameDAO() throws DataAccessException{
        MySqlDaoHelper.configureDatabase();
    }


    @Override
    public int createGame(String name) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }
}
