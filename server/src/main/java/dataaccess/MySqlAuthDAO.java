package dataaccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO{


    @Override
    public AuthData getAuth(String name) throws DataAccessException {
        return null;
    }

    @Override
    public void newAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public Boolean deleteAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public void clearTotal() throws DataAccessException {

    }

    @Override
    public AuthData getName(String auth) throws DataAccessException {
        return null;
    }
}
