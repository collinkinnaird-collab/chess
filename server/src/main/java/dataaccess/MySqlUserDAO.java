package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{



    public MySqlUserDAO() throws DataAccessException{
            MySqlDaoHelper.configureDatabase();
    }


    @Override
    public UserData register(UserData newUser) throws DataAccessException, AlreadyTakenException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        int id = MySqlDaoHelper.executeUpdate(statement, newUser.username(), newUser.password(), newUser.email());
        return newUser;
    }

    @Override
    public UserData getUser(UserData existingUser) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, existingUser.username());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("no");
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        MySqlDaoHelper.executeUpdate(statement);
    }

    private UserData readUser(ResultSet result) throws SQLException {
        var id = result.getInt("id");
        String username = result.getNString("username");
        String password = result.getNString("password");
        String email = result.getNString("email");
        return new UserData(username, password, email);
    }
}
