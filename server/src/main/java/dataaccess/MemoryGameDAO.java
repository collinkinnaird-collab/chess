package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{

    Collection<GameData> chessGames;

    public MemoryGameDAO(){
        chessGames = new ArrayList<>();
    }

    @Override
    public int createGame(String name) throws DataAccessException {

        ChessGame newGame = new ChessGame();

        GameData newGameData = new GameData(chessGames.size(), null, null, name, newGame);

        chessGames.add(newGameData);

        return newGameData.gameID();

    }

    @Override
    public GameData getGame(int ID) throws DataAccessException {

        for (GameData gameId : chessGames) {

            if (gameId.gameID() == ID) {
                return gameId;
            }
        }
            throw new DataAccessException("Game does not exist");
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return chessGames;

    }

    @Override
    public void clear() throws DataAccessException {
        chessGames.clear();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try{
            chessGames.remove(getGame(gameData.gameID()));
            chessGames.add(gameData);
        } catch (DataAccessException e) {
            chessGames.add(gameData);
        }
    }
}
