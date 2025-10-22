package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO{

    Collection<AuthData> verifiedAuth;

    public MemoryAuthDAO() {

        verifiedAuth = new ArrayList<>();
    }

    @Override
    public AuthData getAuth(String user) throws DataAccessException{

        for (AuthData authToken : verifiedAuth){
            if(authToken.username().equals(user)){
                return authToken;
            }
        }

        throw new DataAccessException("No authToken");

    }

    @Override
    public void newAuth(AuthData auth) throws DataAccessException {
        verifiedAuth.add(auth);
    }

    @Override
    public Boolean deleteAuth(AuthData auth) throws DataAccessException {

        return verifiedAuth.removeIf(AuthData -> AuthData.authToken().equals(auth.authToken()));


    }

    @Override
    public void clearTotal() throws DataAccessException {
        verifiedAuth.clear();
    }
}
