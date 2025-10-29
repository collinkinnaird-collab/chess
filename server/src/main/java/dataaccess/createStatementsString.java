package dataaccess;

public interface createStatementsString {
     public final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              username varchar(256) NOT NULL,
              password int NOT NULL,
              email varchar(256) NOT NULL
              )
            """,
            """ 
            CREATE TABLE IF NOT EXISTS user (
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
}
