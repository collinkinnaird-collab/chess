package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


interface PieceMovesCalculator {


    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition );

    default Collection<ChessMove> loop(ChessBoard board, ChessPosition myPosition, int[][] direction) {

        List<ChessMove> contPieceMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()));


        for (int[] path : direction) {

            int updateXaxis = myPosition.getRow() + path[0];
            int updateYaxis = myPosition.getColumn() + path[1];

            while (updateXaxis <= 8 && updateXaxis >= 1 && updateYaxis <= 8 && updateYaxis >= 1) {

                ChessPiece otherPiece = board.getPiece(new ChessPosition(updateXaxis, updateYaxis));
                if(otherPiece != null) {
                    if (otherPiece.getTeamColor() == myPiece.getTeamColor()) {
                        break;
                    } else {
                        contPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn())
                                         , new ChessPosition(updateXaxis, updateYaxis), null));
                        break;
                    }
                }
                else{
                    contPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                       new ChessPosition(updateXaxis, updateYaxis), null));


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
                        eightPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                            new ChessPosition(updateXaxis, updateYaxis), null));
                    }
                }
                else
                {
                    eightPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(updateXaxis, updateYaxis), null));
                }
            }

        }
        return eightPieceMoves;
    }

    default Collection<ChessMove> pawnOptions(ChessBoard board, ChessPosition myPosition, int[][] direction) {

        List<ChessMove> pawnPieceMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()));
        boolean first;
        boolean behind;

        for (int[] path : direction) {

            first = false;
            behind = false;

            if(myPiece.pieceColor == ChessGame.TeamColor.WHITE)
            {
                if (path[0] < 0)
                {
                    continue;
                }
            }
            else if (myPiece.pieceColor == ChessGame.TeamColor.BLACK)
            {
                if (path[0] > 0)
                {
                    continue;
                }
            }
            if(myPosition.getRow() == 2 || myPosition.getRow() == 7)
            {
                first = true;
            }
            if(!first && (path[0] == 2 || path[0] == -2))
            {
                continue;
            }

            int updateXaxis = myPosition.getRow() + path[0];
            int updateYaxis = myPosition.getColumn() + path[1];

            if (updateXaxis <= 8 && updateXaxis >= 1 && updateYaxis <= 8 && updateYaxis >= 1) {
                    ChessPiece anotherPiece = board.getPiece(new ChessPosition(updateXaxis, updateYaxis));

                    if((path[0] == 2 || path[0] == -2) && first)
                    {
                        int silly = 0;
                        if(myPiece.pieceColor == ChessGame.TeamColor.WHITE) {
                            silly = 1;
                        }
                        else{
                            silly = -1;
                        }
                        ChessPiece flippingEdgeCase = board.getPiece(new ChessPosition(myPosition.getRow() + silly, myPosition.getColumn()));

                        if(flippingEdgeCase != null)
                        {
                            behind = true;
                        }
                    }

                    if (anotherPiece != null) {
                        if (anotherPiece.pieceColor != myPiece.pieceColor && path[1] != 0) {
                            if (updateXaxis == 8 || updateXaxis == 1) {
                                addPawns(pawnPieceMoves, myPosition, updateXaxis, updateYaxis);

                            } else{
                                pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                                   new ChessPosition(updateXaxis, updateYaxis), null ));

                            }
                        }

                    } else {
                        if ((updateXaxis == 8 || updateXaxis == 1) && path[1] == 0) {
                            addPawns(pawnPieceMoves, myPosition, updateXaxis, updateYaxis);
                        }else if (path[1] == 0 && !behind){
                            pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                               new ChessPosition(updateXaxis, updateYaxis), null));
                        }
                    }
            }

        }
        return pawnPieceMoves;
    }

    public default void addPawns(Collection<ChessMove> pawnPieceMoves, ChessPosition myPosition,int updateXaxis, int updateYaxis ){
        pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                new ChessPosition(updateXaxis, updateYaxis), ChessPiece.PieceType.QUEEN));
        pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                new ChessPosition(updateXaxis, updateYaxis), ChessPiece.PieceType.ROOK));
        pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                new ChessPosition(updateXaxis, updateYaxis), ChessPiece.PieceType.KNIGHT));
        pawnPieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                new ChessPosition(updateXaxis, updateYaxis), ChessPiece.PieceType.BISHOP));
    }


}
