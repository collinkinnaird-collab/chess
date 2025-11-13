package server;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ServerFacade {
    private final HttpClient client= HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade (String url) {
        serverURL = url;
    }

    public void register(UserData user) throws Exception {
        var path = "/user";
        this.makeRequest("POST", path, user, UserData.class);
    }

    public void login(UserData user) throws Exception {
        var path = "/session";
        this.makeRequest("POST", path, user, UserData.class);
    }

    public void logout() throws Exception{
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public void createGame(String game) throws Exception {
        var path = "/game";
        this.makeRequest("POST", path, game, GameData.class);
    }

    public void listGames() throws Exception {
        var path = "/game";
        this.makeRequest("GET", path, null, GameData.class);
    }

    public void playGame(Object game) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, game, GameData.class);
    }

    public void observeGame(Object id) throws Exception {
        var path = "/game";
        this.makeRequest("GET", path, id, GameData.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI( serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (Exception e){
            throw new Exception("DO LATER");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if(!isSuccessful(status)) {
            throw new Exception("failure");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException{
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if(responseClass != null){
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }






}
