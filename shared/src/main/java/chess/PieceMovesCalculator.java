package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


interface PieceMovesCalculator {


    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition );

    default boolean onBoard(ChessPosition testPos) {
        return (testPos.getRow() <= 8 && testPos.getRow() >= 1 && testPos.getColumn() <= 8 && testPos.getColumn() >= 1);

    }

    default Collection<ChessMove> loop(ChessBoard board, ChessPosition myPosition, int[][] direction) {

        List<ChessMove> PieceMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()));


        for (int[] path : direction) {
            ChessPosition test = new ChessPosition(myPosition.getRow() + path[0], myPosition.getColumn() + path[1]);
            ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() + path[0], myPosition.getColumn() + path[1]));


            if (onBoard(test) && otherPiece != null) {
                if (otherPiece.pieceColor == myPiece.pieceColor) {
                    break;
                } else {
                    PieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(myPosition.getRow() + path[0], myPosition.getColumn() + path[1]), null));

                }

            }
        }


        return PieceMoves;
    }
}
