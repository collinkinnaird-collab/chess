package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO{

    private final Collection<AuthData> verifiedAuth = new ArrayList<>();

    @Override
    public AuthData getAuth(AuthData auth) {
        return null;
    }

    @Override
    public void newAuth(AuthData auth) throws DataAccessException {
        verifiedAuth.add(auth);
    }


    @Override
    public AuthData deleteAuth(AuthData auth) {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        verifiedAuth.clear();
    }
}
