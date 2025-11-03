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

        myServer = new Server();
        var myPort = myServer.run(0);
        System.out.println("Started test HTTP server on " + myPort);
        serverFacade = new TestServerFacade("localhost", Integer.toString(myPort));
        myExistingUser = new TestUser("EpicUser", "EpicPassword", "EpicEmail@mail.com");
        myNewUser = new TestUser("bigTimeRush", "Cant Stop the Feeling", "sillyGoose@mail.com");
        createRequest = new TestCreateRequest("myTestGame");

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

        assertHttpIsOk(loginResult);

        Assertions.assertEquals("buddy", loginResult.getUsername(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.getAuthToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Register Test Negative")
    @Order(2)
    public void registerTestNegative() {

        TestAuthResult loginResult = serverFacade.register(new TestUser(null, "sadfaefe", null));

        assertHttpIsBadRequest(loginResult);

        assertHttpIsBadRequest(loginResult);
    }

    @Test
    @DisplayName("Create game Positive")
    @Order(3)
    public void createGameTestPositive() {
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        assertHttpIsOk(createResult);

        Assertions.assertNotNull(createResult.getGameID(), "Result did not return a game ID");
        Assertions.assertTrue(createResult.getGameID() > 0, "Result returned invalid game ID");
    }

    @Test
    @DisplayName("Create game Negative")
    @Order(4)
    public void createGameTestNegative() {
        TestCreateResult createResult = serverFacade.createGame(null, existingAuth);

        assertHttpIsBadRequest(createResult);
    }

    @Test
    @DisplayName("get auth positive")
    @Order(5)
    public void getAuthToken() {


        TestAuthResult authResult = serverFacade.register(myNewUser);

        Assertions.assertNotNull(authResult.getAuthToken());
    }

    @Test
    @DisplayName("join game positive")
    @Order(6)
    public void joinGamePositive() {

        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createResult.getGameID());
        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);

        assertHttpIsOk(joinResult);

    }

    @Test
    @DisplayName("create game negative")
    @Order(7)
    public void joinGameNegative() {

        TestCreateResult createResult = serverFacade.createGame(new TestCreateRequest(""), existingAuth);

        assertHttpIsBadRequest(createResult);

    }

    @Test
    @DisplayName("create game Positive")
    @Order(8)
    public void createGamePositive() {

        TestCreateResult createResult = serverFacade.createGame(new TestCreateRequest("hooray!"), existingAuth);

        assertHttpIsBadRequest(createResult);

    }

    @Test
    @DisplayName("get game Positive")
    @Order(9)
    public void getGamePositive() {

        TestCreateResult createResult = serverFacade.createGame(new TestCreateRequest("hooray!"), existingAuth);
        TestJoinRequest newJoin = new TestJoinRequest(ChessGame.TeamColor.BLACK, createResult.getGameID());

        assertHttpIsOk(serverFacade.joinPlayer(newJoin, existingAuth));

    }


    @FunctionalInterface
    private interface TableAction {
        void execute(String tableName, Connection connection) throws SQLException;
    }

    private void assertHttpIsOk(TestResult result) {
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(),
                "Server response code wasn't 200 OK (message: %s)".formatted(result.getMessage()));
        Assertions.assertFalse(result.getMessage() != null &&
                        result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Result has returned an error message");
    }

    private void assertHttpIsBadRequest(TestResult result) {
        assertHttpHasError(result, HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
    }

    private void assertHttpHasError(TestResult result, int statusCodes, String message) {
        Assertions.assertEquals(statusCodes, serverFacade.getStatusCode(),
                "Server response code was not %d %s (message: %s)".formatted(statusCodes, message, result.getMessage()));
        Assertions.assertNotNull(result.getMessage(), "Bad Request didn't return an error message");
        Assertions.assertTrue(result.getMessage().toLowerCase(Locale.ROOT).contains("error"),
                "Error message did not contain the word \"Error\"");
    }

}
