import service.ClearService;
import service.GameService;
import service.UserService;
import dataaccess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {


        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}