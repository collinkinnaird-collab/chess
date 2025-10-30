package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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

        try{
            getUser(newUser);
        } catch(Exception e) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            String hashed = storeUserPassword(newUser.password());
            MySqlDaoHelper.executeUpdate(statement, newUser.username(), hashed, newUser.email());
            return newUser;
        }

        throw new AlreadyTakenException("user already exists");
    }

    @Override
    public UserData getUser(UserData existingUser) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password FROM user WHERE username=? ";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, existingUser.username());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs, existingUser.password(), existingUser.email());
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("getUser error");
        }
        throw new DataAccessException("Empty Database");
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        MySqlDaoHelper.executeUpdate(statement);
    }

    private UserData readUser(ResultSet result, String userPassword, String email) throws SQLException, DataAccessException {
        String username = result.getNString("username");
        if(!verifyUser(result.getNString("password"), userPassword)){
            throw new DataAccessException("badPassword");
        }


        return new UserData(username, userPassword, email);

    }

    private String storeUserPassword(String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        return hashedPassword;

    }

    boolean verifyUser(String previousPassword, String currentPassword) {

        return BCrypt.checkpw(currentPassword, previousPassword);
    }
}
