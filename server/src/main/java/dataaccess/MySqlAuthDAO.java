package dataaccess;

import chess.ChessGame;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.MySqlDaoHelper.CREATE_STATEMENTS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO{


    public MySqlAuthDAO() throws DataAccessException{
        MySqlDaoHelper.configureDatabase();
    }

    @Override
    public AuthData getAuth(String name) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("GET AUTH ERROR");
        }
        return null;
    }

    @Override
    public void newAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        MySqlDaoHelper.executeUpdate(statement, auth.username(), auth.authToken());
    }

    @Override
    public Boolean deleteAuth(AuthData auth) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        if(auth != null) {
            MySqlDaoHelper.executeUpdate(statement, auth.authToken());
            return true;
        }
        else{
            throw new DataAccessException("error");
        }
    }

    @Override
    public void clearTotal() throws DataAccessException {
        var statement = "TRUNCATE auth";
        MySqlDaoHelper.executeUpdate(statement);
    }

    @Override
    public AuthData getName(String auth) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("GET NAME ERRROR");
        }
        throw new DataAccessException("does not exist");
    }

    private AuthData readAuth(ResultSet result) throws SQLException{
        String username = result.getNString("username");
        String authToken = result.getNString("authToken");

        return new AuthData(username, authToken);
    }

}
