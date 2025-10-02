package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements CheckMateCalculator{

   // ChessGame myGame = new ChessGame();
    ChessBoard gameBoard = new ChessBoard();
    TeamColor myColor = TeamColor.WHITE;
    public ChessGame() {


    }

    public ChessGame(ChessGame copy){

        ChessBoard copyBoard = new ChessBoard(copy.gameBoard);


        this.myColor = copy.myColor;
        this.gameBoard = copyBoard;



    }




    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return myColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        myColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {


        List<ChessMove> allMoves = new ArrayList<>();

        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        if (currentPiece.getPieceType() == null)
        {
            return allMoves;
        }


        return currentPiece.pieceMoves(gameBoard, startPosition);

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

//        Collection<ChessMove> totalMoves = new ArrayList<>();
//        totalMoves = validMoves(move.getStartPosition());

        //ChessGame copy = new ChessGame(this);


        ChessPiece trialPiece = gameBoard.getPiece(move.getStartPosition());
        if (isInCheck(trialPiece.pieceColor)) {
            throw new InvalidMoveException();
        }
        if (isInCheckmate(trialPiece.pieceColor)){
            throw new InvalidMoveException();
        }
        if (isInStalemate(trialPiece.pieceColor)){
            throw new InvalidMoveException();
        } else {
            gameBoard.addPiece(move.getEndPosition(), trialPiece);
            gameBoard.addPiece(move.getStartPosition(), null);
            setBoard(gameBoard);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //find king
        ChessPiece kingPiece;
        ChessPosition kingPosition = new ChessPosition(0,0);
        Collection<ChessMove> kingMoves = new ArrayList<>();
        Collection<ChessMove> otherMoves = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                kingPiece = gameBoard.getPiece(new ChessPosition(i,j));
                if (kingPiece.getPieceType() == ChessPiece.PieceType.KING)
                {
                    kingPosition = new ChessPosition(i,j);
                    kingMoves = validMoves(kingPosition);
                    break;
                }

            }
        }
        for(int k = 0; k < 8; k++){
            for(int l = 0; l < 8; l++){
                if (gameBoard.getPiece(new ChessPosition(k,l)).getPieceType() != null)
                {
                    ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(k,l));
                    if(testPiece.pieceColor != teamColor){
                        otherMoves = testPiece.pieceMoves(gameBoard, new ChessPosition(k,l));
                        Iterator<ChessMove> iterator = otherMoves.iterator();
                        while(iterator.hasNext())
                        {
                            if(iterator.next().getEndPosition() == kingPosition){
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

//        //find king
//        ChessPiece kingPiece;
//        ChessPosition kingPosition = new ChessPosition(0,0);
//        Collection<ChessMove> kingMoves = new ArrayList<>();
//        Collection<ChessMove> otherMoves = new ArrayList<>();
//
//        for(int i = 0; i < 8; i++){
//            for(int j = 0; j < 8; j++){
//                kingPiece = gameBoard.getPiece(new ChessPosition(i,j));
//                if (kingPiece.getPieceType() == ChessPiece.PieceType.KING)
//                {
//                    kingPosition = new ChessPosition(i,j);
//                    kingMoves = validMoves(kingPosition);
//                    break;
//                }
//
//            }
//        }
//        for(int k = 0; k < 8; k++){
//            for(int l = 0; l < 8; l++){
//                if (gameBoard.getPiece(new ChessPosition(k,l)).getPieceType() != null)
//                {
//                    ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(k,l));
//                    if(testPiece.pieceColor != teamColor){
//                        otherMoves = testPiece.pieceMoves(gameBoard, new ChessPosition(k,l));
//                        Iterator<ChessMove> iterator = otherMoves.iterator();
//                        while(iterator.hasNext())
//                        {
//                            if(iterator.next().getEndPosition() == kingPosition){
//                                return true;
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
        // call valid moves here
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

//    @Override
//    public ChessGame clone(){
//        try{
//            ChessGame clonedGame = (ChessGame) super.clone();
//
//            ChessBoard copyBoard = (ChessBoard) getBoard().clone();
//            clonedGame.setBoard(copyBoard);
//
//            return clonedGame;
//        } catch (CloneNotSupportedException e){
//            throw new RuntimeException();
//        }
//    }


    @Override
    public String toString() {
        return "ChessGame{}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && myColor == chessGame.myColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, myColor);
    }


}


//