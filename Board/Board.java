package Board;

public class Board {
    int[] Squares;

    public Board() {
        Squares = new int[64];
    }

    private void setStartingPosition() {

    }

    public void makeMove(Move move) {
        if (move.isLegalMove()) {
            Squares[move.getTo()] = Squares[move.getFrom()];
            Squares[move.getFrom()] = Piece.EMPTY;
        }
    }
}