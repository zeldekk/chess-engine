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
        int targetSquare = Square[move.getTo()];

        if (movingPiece == Piece.EMPTY) return false;

        boolean isWhitePiece = isWhite(movingPiece);
        boolean isTargetPieceWhite = isWhite(targetSquare);

        if (isWhitePiece == isTargetPieceWhite && targetSquare != Piece.EMPTY) return false;

        if (!canMove(move.getFrom(), move.getTo(), movingPiece)) return false;

        return (movingPiece != Piece.WHITE_KING && movingPiece != Piece.BLACK_KING)
                || !isSquareAttacked(move.getTo(), !isWhitePiece);
    }

    public boolean isSquareAttacked(int square, boolean byWhite) {
        for (int i = 0; i < 64; i++) {
            int piece = Square[i];

            if (piece == Piece.EMPTY) continue;

            if ((byWhite && !isWhite(piece)) || (!byWhite && isWhite(piece))) {
                if (canMove(i, square, piece)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMove(int from, int to, int piece) {
        int rowDiff = Math.abs((to / 8) - (from / 8));
        int colDiff = Math.abs((to % 8) - (from % 8));

        switch (piece) {
            case Piece.WHITE_PAWN -> {
                if (colDiff == 1 && rowDiff == 1 && isWhite(Square[to])) return true;
            }
            case Piece.BLACK_PAWN -> {
                if (colDiff == 1 && rowDiff == 1 && !isWhite(Square[to]) && Square[to] != Piece.EMPTY) return true;
            }
            case Piece.WHITE_KNIGHT, Piece.BLACK_KNIGHT -> {
                return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
            }
            case Piece.WHITE_KING, Piece.BLACK_KING -> {
                return rowDiff <= 1 && colDiff <= 1;
            }
            case Piece.WHITE_BISHOP, Piece.BLACK_BISHOP -> {
                return isValidDiagonalMove(from, to);
            }
            case Piece.WHITE_ROOK, Piece.BLACK_ROOK -> {
                return isValidStraightMove(from, to);
            }
            case Piece.WHITE_QUEEN, Piece.BLACK_QUEEN -> {
                return isValidStraightMove(from, to) || isValidDiagonalMove(from, to);
            }
        }
        return false;
    }

    private boolean isValidDiagonalMove(int from, int to) {
        int rowStart = from / 8, colStart = from % 8;
        int rowEnd = to / 8, colEnd = to % 8;

        if (Math.abs(rowStart - rowEnd) != Math.abs(colStart - colEnd)) return false;
        int rowStep = (rowEnd > rowStart) ? 1 : -1;
        int colStep = (colEnd > colStart) ? 1 : -1;

        int r = rowStart + rowStep, c = colStart + colStep;
        while (r != rowEnd && c != colEnd) {
            if (Square[r * 8 + c] != Piece.EMPTY) return false;
            r += rowStep;
            c += colStep;
        }
        return true;
    }

    private boolean isValidStraightMove(int from, int to) {
        int rowStart = from / 8, colStart = from % 8;
        int rowEnd = to / 8, colEnd = to % 8;

        if (rowStart == rowEnd) {
            int step = (colEnd > colStart) ? 1 : -1;
            for (int c = colStart + step; c != colEnd; c += step) {
                if (Square[rowStart * 8 + c] != Piece.EMPTY) return false;
            }
            return true;
        } else if (colStart == colEnd) {
            int step = (rowEnd > rowStart) ? 1 : -1;
            for (int r = rowStart + step; r != rowEnd; r += step) {
                if (Square[r * 8 + colStart] != Piece.EMPTY) return false;
            }
            return true;
        }
        return false;
    }
}

