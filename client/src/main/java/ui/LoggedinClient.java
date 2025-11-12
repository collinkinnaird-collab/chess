package ui;

import server.ServerFacade;

public class LoggedinClient {

    private final ServerFacade server;


    public LoggedinClient(String serverURL){
        server = new ServerFacade(serverURL);
    }

}
