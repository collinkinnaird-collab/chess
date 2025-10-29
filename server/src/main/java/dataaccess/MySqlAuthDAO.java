package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

import static dataaccess.createStatementsString.createStatements;

public class MySqlAuthDAO implements AuthDAO{


    public MySqlAuthDAO() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public AuthData getAuth(String name) throws DataAccessException {
        return null;
    }

    @Override
    public void newAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public Boolean deleteAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public void clearTotal() throws DataAccessException {

    }

    @Override
    public AuthData getName(String auth) throws DataAccessException {
        return null;
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("nope");
        }
    }
}
