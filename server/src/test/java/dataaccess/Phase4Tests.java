package dataaccess;

import chess.ChessGame;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Phase4Tests {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server server;
    private String existingAuth;

    @BeforeAll
    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        createRequest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }

    @BeforeEach
    public void setUp() {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register Test positive")
    @Order(1)
    public void registerTest() {

        TestAuthResult loginResult = serverFacade.register(new TestUser("buddy", "epicPassword", "rew@gmail.com"));

        assertHttpOk(loginResult);

        Assertions.assertEquals("buddy", loginResult.getUsername(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.getAuthToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Register Test Negative")
    @Order(2)
    public void registerTestNegative() {

        TestAuthResult loginResult = serverFacade.register(new TestUser(null, "sadfaefe", null));

        assertHttpBadRequest(loginResult);

        assertHttpBadRequest(loginResult);
    }

    @Test
    @DisplayName("Create game Positive")
    @Order(3)
    public void createGameTestPositive() {
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        assertHttpOk(createResult);

        Assertions.assertNotNull(createResult.getGameID(), "Result did not return a game ID");
        Assertions.assertTrue(createResult.getGameID() > 0, "Result returned invalid game ID");
    }

//    @Test
//    @DisplayName("Bcrypt")
//    @Order(2)
//    public void bcrypt() {
//        serverFacade.register(TEST_USER);
//        Assertions.assertEquals(200, serverFacade.getStatusCode(), "Unable to register");
//        executeForAllTables(this::checkTableForPassword);
//    }
//
//    @Test
//    @DisplayName("Database Error Handling")
//    @Order(3)
//    public void databaseErrorHandling() throws ReflectiveOperationException {
//        /*
//        This test simulates an interruption in connecting to MySQL after the server is already running (it started with
//        MySQL working normally). If this happens, this should be considered an "Internal Server Error" and the response
//        code for any endpoint which no longer can do what it needs to do (which for this project should be all of them)
//        should be 500. The body of each of these responses should include a reasonable, relevant error message.
//         */
//        Properties fakeDbProperties = new Properties();
//        fakeDbProperties.setProperty("db.name", UUID.randomUUID().toString());
//        fakeDbProperties.setProperty("db.user", UUID.randomUUID().toString());
//        fakeDbProperties.setProperty("db.password", UUID.randomUUID().toString());
//        fakeDbProperties.setProperty("db.host", "localhost");
//        fakeDbProperties.setProperty("db.port", "100000");
//
//        Class<?> databaseManagerClass = findDatabaseManager();
//        Method loadPropertiesMethod = databaseManagerClass.getDeclaredMethod("loadProperties", Properties.class);
//        loadPropertiesMethod.setAccessible(true);
//        Object obj = databaseManagerClass.getDeclaredConstructor().newInstance();
//        loadPropertiesMethod.invoke(obj, fakeDbProperties);
//
//        List<Supplier<TestResult>> operations = List.of(
//                () -> serverFacade.clear(),
//                () -> serverFacade.register(TEST_USER),
//                () -> serverFacade.login(TEST_USER),
//                () -> serverFacade.logout(UUID.randomUUID().toString()),
//                () -> serverFacade.createGame(new TestCreateRequest("inaccessible"), UUID.randomUUID().toString()),
//                () -> serverFacade.listGames(UUID.randomUUID().toString()),
//                () -> serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.WHITE, 1), UUID.randomUUID().toString())
//        );
//
//        try {
//            for (Supplier<TestResult> operation : operations) {
//                TestResult result = operation.get();
//                Assertions.assertEquals(500, serverFacade.getStatusCode(),
//                        "Server response code was not 500 Internal Error");
//                Assertions.assertNotNull(result.getMessage(), "Invalid Request didn't return an error message");
//                Assertions.assertTrue(result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
//                        "Error message didn't contain the word \"Error\"");
//            }
//        } finally {
//            Method loadFromResources = databaseManagerClass.getDeclaredMethod("loadPropertiesFromResources");
//            loadFromResources.setAccessible(true);
//            loadFromResources.invoke(obj);
//        }
//    }
//
//    private int getDatabaseRows() {
//        AtomicInteger rows = new AtomicInteger();
//        executeForAllTables((tableName, connection) -> {
//            try (var statement = connection.createStatement()) {
//                var sql = "SELECT count(*) FROM " + tableName;
//                try (var resultSet = statement.executeQuery(sql)) {
//                    if (resultSet.next()) {
//                        rows.addAndGet(resultSet.getInt(1));
//                    }
//                }
//            }
//        });
//
//        return rows.get();
//    }
//
//    private void checkTableForPassword(String table, Connection connection) throws SQLException {
//        String sql = "SELECT * FROM " + table;
//        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columns = rsmd.getColumnCount();
//            while (rs.next()) {
//                for (int i = 1; i <= columns; i++) {
//                    String value = rs.getString(i);
//                    Assertions.assertFalse(value.contains(TEST_USER.getPassword()),
//                            "Found clear text password in database");
//                }
//            }
//        }
//    }
//
//    private void executeForAllTables(TableAction tableAction) {
//        String sql = """
//                    SELECT table_name
//                    FROM information_schema.tables
//                    WHERE table_schema = DATABASE();
//                """;
//
//        try (Connection conn = getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
//            try (var resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    tableAction.execute(resultSet.getString(1), conn);
//                }
//            }
//        } catch (ReflectiveOperationException | SQLException e) {
//            Assertions.fail(e.getMessage(), e);
//        }
//    }
//
//    private Connection getConnection() throws ReflectiveOperationException {
//        Class<?> clazz = findDatabaseManager();
//        Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
//        getConnectionMethod.setAccessible(true);
//
//        Object obj = clazz.getDeclaredConstructor().newInstance();
//        return (Connection) getConnectionMethod.invoke(obj);
//    }
//
//    private Class<?> findDatabaseManager() throws ClassNotFoundException {
//        if(databaseManagerClass != null) {
//            return databaseManagerClass;
//        }
//
//        for (Package p : getClass().getClassLoader().getDefinedPackages()) {
//            try {
//                Class<?> clazz = Class.forName(p.getName() + ".DatabaseManager");
//                clazz.getDeclaredMethod("getConnection");
//                databaseManagerClass = clazz;
//                return clazz;
//            } catch (ReflectiveOperationException ignored) {}
//        }
//        throw new ClassNotFoundException("Unable to load database in order to verify persistence. " +
//                "Are you using DatabaseManager to set your credentials? " +
//                "Did you edit the signature of the getConnection method?");
//    }

    @FunctionalInterface
    private interface TableAction {
        void execute(String tableName, Connection connection) throws SQLException;
    }

    private void assertHttpOk(TestResult result) {
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(),
                "Server response code was not 200 OK (message: %s)".formatted(result.getMessage()));
        Assertions.assertFalse(result.getMessage() != null &&
                        result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }

    private void assertHttpBadRequest(TestResult result) {
        assertHttpError(result, HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
    }

    private void assertHttpError(TestResult result, int statusCode, String message) {
        Assertions.assertEquals(statusCode, serverFacade.getStatusCode(),
                "Server response code was not %d %s (message: %s)".formatted(statusCode, message, result.getMessage()));
        Assertions.assertNotNull(result.getMessage(), "Invalid Request didn't return an error message");
        Assertions.assertTrue(result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Error message didn't contain the word \"Error\"");
    }

}
