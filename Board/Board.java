package Board;

public class Board {
    public int[] Square;

    public Board() {
        Square = new int[64];
        setStartingPosition();
    }

    private void setStartingPosition() {
        for (int i = 8; i < 16; i++) Square[i] = Piece.WHITE_PAWN;
        for (int i = 48; i < 56; i++) Square[i] = Piece.BLACK_PAWN;
        Square[0] = Square[7] = Piece.WHITE_ROOK;
        Square[56] = Square[63] = Piece.BLACK_ROOK;
        Square[1] = Square[6] = Piece.WHITE_KNIGHT;
        Square[57] = Square[62] = Piece.BLACK_KNIGHT;
        Square[2] = Square[5] = Piece.WHITE_BISHOP;
        Square[58] = Square[61] = Piece.BLACK_BISHOP;
        Square[3] = Piece.WHITE_QUEEN;
        Square[4] = Piece.WHITE_KING;
        Square[59] = Piece.BLACK_QUEEN;
        Square[60] = Piece.BLACK_KING;
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