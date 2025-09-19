package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


interface PieceMovesCalculator {


    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition );

//    default boolean onBoard(int row, int col) {
//        return (row <= 8 && row >= 1 && col <= 8 && col >= 1);
//
//    }

    default Collection<ChessMove> loop(ChessBoard board, ChessPosition myPosition, int[][] direction) {

        List<ChessMove> contPieceMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()));


        for (int[] path : direction) {

            int updateXaxis = myPosition.getRow() + path[0];
            int updateYaxis = myPosition.getColumn() + path[1];

            while (updateXaxis <= 8 && updateXaxis >= 1 && updateYaxis <= 8 && updateYaxis >= 1) {

                ChessPiece otherPiece = board.getPiece(new ChessPosition(updateXaxis, updateYaxis));
                if(otherPiece != null) {
                    if (otherPiece.pieceColor == myPiece.pieceColor) {
                        break;
                    } else {
                        contPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));
                        break;
                    }
                }
                else{
                    contPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));


                    updateYaxis = updateYaxis + path[1];
                    updateXaxis = updateXaxis + path[0];
                }

            }
        }


        return contPieceMoves;
    }

    default Collection<ChessMove> eightOptions(ChessBoard board, ChessPosition myPosition, int[][] direction) {

        List<ChessMove> eightPieceMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()));


        for (int[] path : direction) {

            int updateXaxis = myPosition.getRow() + path[0];
            int updateYaxis = myPosition.getColumn() + path[1];

            if (updateXaxis <= 8 && updateXaxis >= 1 && updateYaxis <= 8 && updateYaxis >= 1) {

                ChessPiece anotherPiece = board.getPiece(new ChessPosition(updateXaxis, updateYaxis));
                if (anotherPiece != null) {
                    if (anotherPiece.pieceColor != myPiece.pieceColor)
                    {
                        eightPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));
                    }
                }
                else
                {
                    eightPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));
                }
            }

        }
        return eightPieceMoves;
    }


}
