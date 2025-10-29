package dataaccess;

import chess.ChessGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public interface MySqlDaoHelper {
     public final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
              id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              username varchar(256) NOT NULL,
              password int NOT NULL,
              email varchar(256) NOT NULL
              )
            """,
            """ 
            CREATE TABLE IF NOT EXISTS auth (
              id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              username varchar(256) NOT NULL,
              authToken varchar(256) NOT NULL
             )
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
              id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
              gameId INT NOT NULL,
              whiteUsername VARCHAR(256),
              blackUsername VARCHAR(256),
              gameName VARCHAR(256) NOT NULL,
              game JSON
            )

            """


    };

     static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("nope");
        }
    }

    static void configureDatabase() throws DataAccessException {
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


