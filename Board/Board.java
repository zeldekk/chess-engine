package Board;

import Utilities.FenUtilities;

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
            Square[move.getTo()] = Square[move.getFrom()];
            Square[move.getFrom()] = Piece.EMPTY;
    }
}