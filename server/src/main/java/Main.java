import Service.ClearService;
import Service.UserService;
import dataaccess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {


        UserDAO dataAccessUser = new MemoryUserDAO();
        AuthDAO dataAccessAuth = new MemoryAuthDAO();
        GameDAO dataAccessGame = new MemoryGameDAO();

        var userService = new UserService(dataAccessUser, dataAccessAuth);
        var clearService = new ClearService(dataAccessUser, dataAccessGame, dataAccessAuth);
        Server server = new Server(userService, clearService);
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}