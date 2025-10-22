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
    public GameData createGame(GameData gameName) throws DataAccessException {

        ChessGame newGame = new ChessGame();

        GameData newGameData = new GameData(chessGames.size(), gameName.whiteUsername(), gameName.blackUsername(), gameName.gameName(), newGame);

        chessGames.add(newGameData);

        return newGameData;

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
    public void listGames() throws DataAccessException {

    }

    @Override
    public boolean clear() throws DataAccessException {
        return false;
    }
}
