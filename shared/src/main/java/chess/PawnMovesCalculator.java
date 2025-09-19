package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ){
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        int [][] possibleMoves = {{1,0},{-1,0}, {1,1}, {-1,1}, {1,-1}, {-1,-1}, {2,0}, {-2,0}};

        pawnMoves = pawnOptions(board, myPosition, possibleMoves);

        return pawnMoves;
    }
}
