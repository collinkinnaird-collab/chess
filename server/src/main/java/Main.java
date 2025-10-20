import Service.UserService;
import chess.*;
import dataaccess.MemoryDataAccess;
import dataaccess.UserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {


        UserDAO dataAccessUser = new MemoryDataAccess();

        var userService = new UserService(dataAccessUser);
        Server server = new Server(userService);
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}