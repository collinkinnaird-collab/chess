package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard  {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard copy){

        ChessPiece[][] copyBoard = copy.squares;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (squares[i][j].getPieceType() != null){
                    ChessPiece copyPiece = new ChessPiece(squares[i][j].pieceColor, squares[i][j].getPieceType());
                    copyBoard[i][j] = copyPiece;

                }
            }
        }
        // nested for loop,

        this.squares = copyBoard;


    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn()-1] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
//        throw new RuntimeException("Not implemented");
//
        return  squares[position.getRow() - 1][position.getColumn()-1];
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

    // first row of white
        ChessPiece rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece Bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece Queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece King = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece Bishop2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece Knight2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece rook2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        // second row of white
        ChessPiece Pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece Pawn8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        // first row of black
        ChessPiece Brook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece Bknight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece BBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece BQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece BKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece BBishop2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece BKnight2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece Brook2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        // second row of black
        ChessPiece BPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn3 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn4 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn5 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn6 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn7 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BPawn8 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

        addPiece(new ChessPosition(1,1), rook);
        addPiece(new ChessPosition(1,2), knight);
        addPiece(new ChessPosition(1,3), Bishop);
        addPiece(new ChessPosition(1,4), Queen);
        addPiece(new ChessPosition(1,5), King);
        addPiece(new ChessPosition(1,6), Bishop2);
        addPiece(new ChessPosition(1,7), Knight2);
        addPiece(new ChessPosition(1,8), rook2);

        addPiece(new ChessPosition(2,1), Pawn);
        addPiece(new ChessPosition(2,2), Pawn2);
        addPiece(new ChessPosition(2,3), Pawn3);
        addPiece(new ChessPosition(2,4), Pawn4);
        addPiece(new ChessPosition(2,5), Pawn5);
        addPiece(new ChessPosition(2,6), Pawn6);
        addPiece(new ChessPosition(2,7), Pawn7);
        addPiece(new ChessPosition(2,8), Pawn8);

        addPiece(new ChessPosition(8,1), Brook);
        addPiece(new ChessPosition(8,2), Bknight);
        addPiece(new ChessPosition(8,3), BBishop);
        addPiece(new ChessPosition(8,4), BQueen);
        addPiece(new ChessPosition(8,5), BKing);
        addPiece(new ChessPosition(8,6), BBishop2);
        addPiece(new ChessPosition(8,7), BKnight2);
        addPiece(new ChessPosition(8,8), Brook2);

        addPiece(new ChessPosition(7,1), BPawn);
        addPiece(new ChessPosition(7,2), BPawn2);
        addPiece(new ChessPosition(7,3), BPawn3);
        addPiece(new ChessPosition(7,4), BPawn4);
        addPiece(new ChessPosition(7,5), BPawn5);
        addPiece(new ChessPosition(7,6), BPawn6);
        addPiece(new ChessPosition(7,7), BPawn7);
        addPiece(new ChessPosition(7,8), BPawn8);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
