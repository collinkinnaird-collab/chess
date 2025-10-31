package dataaccess;
import model.*;

import java.util.Collection;
import java.util.UUID;


public interface AuthDAO {

        AuthData getAuth(String name) throws DataAccessException;

        void newAuth(AuthData auth) throws DataAccessException;

        Boolean deleteAuth(AuthData auth) throws DataAccessException;

        void clearTotal() throws DataAccessException;

        AuthData getName(String auth) throws Exception;


        //void clear
}
