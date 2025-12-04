package ui;

import static ui.EscapeSequences.*;


public class DrawBoard {

    StringBuilder chessBoard = new StringBuilder();




    public void freshBoard(String teamColor){
        if(teamColor.equalsIgnoreCase("WHITE")){
            whiteBoard();
        } else if (teamColor.equalsIgnoreCase("BLACK")){
            blackBoard();
        }
    }



    public void blackBoard(){
        blackRow();

        for(int i = 0; i < 8; i++ ){
            for(int j = 0; j < 10; j++){
                if(j > 0 && j < 9){
                    boolean isDark = (i + j) % 2 == 1;
                    boolean isBlack = i == 0 || i == 1;
                    String squareColor = isDark ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY;
                    chessBoard.append(squareColor);

                    chessBoard.append(pieces(j, i, !isBlack, false));


                    chessBoard.append(RESET_BG_COLOR);

                } else {
                    chessBoard.append(SET_BG_COLOR_BLACK + String.valueOf(i + 1));
                }
            }
            chessBoard.append("\n");
        }

        blackRow();
        System.out.print(chessBoard);
    }


    public void whiteBoard() {


        whiteRow();

        for(int i = 0; i < 8; i++ ){
            for(int j = 0; j < 10; j++){
                if(j > 0 && j < 9){
                    boolean isDark = (i + j) % 2 == 1;
                    boolean isBlack = i == 0 || i == 1;
                    String squareColor = isDark ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY;
                    chessBoard.append(squareColor);

                    chessBoard.append(pieces(j, i, isBlack, true));


                    chessBoard.append(RESET_BG_COLOR);

                } else {
                    chessBoard.append(SET_BG_COLOR_BLACK + String.valueOf(8 - i));
                }
            }
            chessBoard.append("\n");
        }

        whiteRow();
        System.out.print(chessBoard);
    }

    public void whiteRow(){
        chessBoard.append(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + SET_TEXT_BOLD);
        chessBoard.append("  A  B  C  D  E  F  G  H  \n");
        chessBoard.append(RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_BOLD_FAINT);
    }

    public void blackRow(){
        chessBoard.append(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + SET_TEXT_BOLD);
        chessBoard.append("  H  G  F  E  D  C  B  A  \n");
        chessBoard.append(RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_BOLD_FAINT);
    }

    public String pieces(int num, int outerNum, boolean isBlack, boolean flipQueen){
        if((outerNum == 0 || outerNum == 7) && isBlack) {
            return switch (num) {
                case 1 -> BLACK_ROOK;
                case 2 -> BLACK_KNIGHT;
                case 3 -> BLACK_BISHOP;
                case 4 -> BLACK_QUEEN;
                case 5 -> BLACK_KING;
                case 6 -> BLACK_BISHOP;
                case 7 -> BLACK_KNIGHT;
                case 8 -> BLACK_ROOK;
                default -> BLACK_ROOK;
            };
        } else if(isBlack && (outerNum == 1 || outerNum == 6))
        {
            return BLACK_PAWN;
        }

        if(!isBlack && (outerNum == 0 || outerNum == 7)) {
            return switch (num) {
                case 1 -> WHITE_ROOK;
                case 2 -> WHITE_KNIGHT;
                case 3 -> WHITE_BISHOP;
                case 4 -> WHITE_QUEEN;
                case 5 -> WHITE_KING;
                case 6 -> WHITE_BISHOP;
                case 7 -> WHITE_KNIGHT;
                case 8 -> WHITE_ROOK;
                default -> WHITE_ROOK;
            };
        } else if(!isBlack && (outerNum == 1 || outerNum == 6))
        {
            return WHITE_PAWN;
        }
        return "   ";
    }
}