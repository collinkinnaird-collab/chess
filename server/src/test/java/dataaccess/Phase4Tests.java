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

    private static TestUser myExistingUser;
    private static TestUser myNewUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server myServer;
    private String existingAuth;

    @BeforeAll
    public static void startServer() {

        var myPort = myServer.run(0);
        System.out.println("Started test HTTP server on " + myPort);
        serverFacade = new TestServerFacade("localhost", Integer.toString(myPort));
        myExistingUser = new TestUser("EpicUser", "EpicPassword", "EpicEmail@mail.com");
        myNewUser = new TestUser("bigTimeRush", "Cant Stop the Feeling", "sillyGoose@mail.com");
        createRequest = new TestCreateRequest("myTestGame");
        myServer = new Server();
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult normResult = serverFacade.register(myExistingUser);
        existingAuth = normResult.getAuthToken();
    }

    @BeforeEach
    public void setUp() {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        myServer.stop();
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
