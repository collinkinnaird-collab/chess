package dataaccess;
import model.*;

import java.util.Collection;
import java.util.UUID;


public interface AuthDAO {

        AuthData getAuth(AuthData auth) throws DataAccessException;

        void newAuth(AuthData auth) throws DataAccessException;

        AuthData deleteAuth(AuthData auth) throws DataAccessException;

        void clear() throws DataAccessException;
}
