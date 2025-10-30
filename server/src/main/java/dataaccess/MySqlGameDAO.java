package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.ListOfGames;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{

    Collection<GameData> chessGames;

    public MySqlGameDAO() throws DataAccessException{
        MySqlDaoHelper.configureDatabase();
        chessGames = new ArrayList<>();
    }


    @Override
    public int createGame(String name) throws DataAccessException {
        var statement = "INSERT INTO game (gameId, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        ChessGame newGame = new ChessGame();

        GameData newGameData = new GameData(chessGames.size() + 1, null, null, name, newGame);

        chessGames.add(newGameData);

        String json = new Gson().toJson(newGame);

        MySqlDaoHelper.executeUpdate(statement, newGameData.gameID()
                                            , newGameData.whiteUsername(), newGameData.blackUsername()
                                            , newGameData.gameName(), json);
        return newGameData.gameID();
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId FROM game WHERE gameId=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("GetGame Error");
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ListOfGames();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, whiteUsername, blackUsername, gameName FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                       result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("GetGame Error");
        }

        return result;

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        MySqlDaoHelper.executeUpdate(statement);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "DELETE FROM game WHERE gameId=?";
        MySqlDaoHelper.executeUpdate(statement, gameData.gameID());

        var statement2 = "INSERT INTO game (gameId, whiteUsername, blackUsername, gameName, gameData) VALUES (?, ?, ?, ?, ?)";
        String json = new Gson().toJson(gameData.game());
        MySqlDaoHelper.executeUpdate(statement2, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),  gameData.gameName()
                                    , json);

    }

    private GameData readGame(ResultSet result) throws SQLException {
        int gameId = result.getInt("gameId");
        String whiteUsername = result.getString("whiteUsername");
        String blackUsername = result.getString("blackUsername");
        String name = result.getNString("gameName");
        var json = result.getString("json");
        ChessGame newGame = new Gson().fromJson(json, ChessGame.class);

        return new GameData(gameId, whiteUsername, blackUsername, name, newGame);
    }
}
