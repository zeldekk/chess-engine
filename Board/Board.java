package Board;

import Utilities.FenUtilities;

import static Board.Piece.isWhite;

public class Board {
    public int[] Square;

    public Board() {
        Square = new int[64];
        setStartingPosition();
    }

    private void setStartingPosition() {
        Square = FenUtilities.fenToArray(FenUtilities.startingPositionFen);
    }

    public void display() {
        for (int i = 0; i < 64; i++) {
            System.out.print(Square[i] + "\t");
            if ((i + 1) % 8 == 0) System.out.println();
        }
    }

    public void makeMove(Move move) {
            if (isLegalMove(move)) {
                Square[move.getTo()] = Square[move.getFrom()];
                Square[move.getFrom()] = Piece.EMPTY;
            }
    }

    private boolean isLegalMove(Move move) {
        int movingPiece = Square[move.getFrom()];

        switch (movingPiece) {
            case Piece.EMPTY -> {
                return false;
            }
            case Piece.WHITE_KING -> { //Add can't move into check
                if (isWhite(Square[move.getTo()])) {
                    return false;
                }
                if (move.getTo() != move.getFrom() + 1 || move.getTo() != move.getFrom() + 8 || move.getTo() != move.getFrom() + 9 || move.getTo() != move.getFrom() + 7 || move.getTo() != move.getFrom() - 1 || move.getTo() != move.getFrom() - 8 || move.getTo() != move.getFrom() - 7 || move.getTo() != move.getFrom() - 9) {
                    return false;
                }
            }
            case Piece.BLACK_KING -> {
                if (!isWhite(Square[move.getTo()])) {
                    return false;
                }
                if (move.getTo() != move.getFrom() + 1 || move.getTo() != move.getFrom() + 8 || move.getTo() != move.getFrom() + 9 || move.getTo() != move.getFrom() + 7 || move.getTo() != move.getFrom() - 1 || move.getTo() != move.getFrom() - 8 || move.getTo() != move.getFrom() - 7 || move.getTo() != move.getFrom() - 9) {
                    return false;
                }
            }
        }

        return true;
    }
}