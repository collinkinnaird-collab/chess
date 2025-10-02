package chess;

import java.util.Collection;

public interface CheckMateCalculator {

    Collection<ChessMove> validMoves(ChessPosition startPosition);

    default boolean check(ChessPosition startPosition){



        return true;
    }
}
