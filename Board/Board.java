package Board;

//TODO: potentially canmove and isattacked are linked wrongly so ill need to fix that, and also board cloning would be cool af

import Utilities.FenUtilities;

import static Board.Piece.isWhite;

public class Board {
    public int[] Square;
    public boolean whiteToMove;
    private boolean canCastleWhiteKingside = true;
    private boolean canCastleWhiteQueenside = true;
    private boolean canCastleBlackKingside= true;
    private boolean canCastleBlackQueenside = true;
    private int enPassantTarget;
    private int halfmoveClock;
    private int fullmoveCount;

    public Board() {
        Square = new int[64];
        setStartingPosition();
    }

    private void setStartingPosition() {
        loadFromFEN(FenUtilities.startingPositionFen);
    }

    public void loadFromFEN(String fen) {
        FenUtilities.BoardState state = FenUtilities.fenToBoardState(fen);
        this.Square = state.board;
        this.whiteToMove = state.whiteToMove;
        this.canCastleWhiteKingside = state.canCastleWhiteKingside;
        this.canCastleWhiteQueenside = state.canCastleWhiteQueenside;
        this.canCastleBlackKingside = state.canCastleBlackKingside;
        this.canCastleBlackQueenside = state.canCastleBlackQueenside;
        this.enPassantTarget = state.enPassantTarget;
        this.halfmoveClock = state.halfmoveClock;
        this.fullmoveCount = state.fullmoveNumber;
    }

    public void display() {
        for (int i = 0; i < 64; i++) {
            System.out.print(Square[i] + "\t");
            if ((i + 1) % 8 == 0) System.out.println();
        }
    }

    public void makeMove(Move move) {
        if (isLegalMove(move)) {
            int movingPiece = Square[move.getFrom()];
            int targetPiece = Square[move.getTo()];

            if (move.getMoveFlag() == 0) {
                Square[move.getTo()] = movingPiece;
                Square[move.getFrom()] = Piece.EMPTY;
                if (movingPiece == Piece.WHITE_KING) {
                    canCastleWhiteKingside = false;
                    canCastleWhiteQueenside = false;
                }
                if (movingPiece == Piece.BLACK_KING) {
                    canCastleBlackKingside = false;
                    canCastleBlackQueenside = false;
                }
                if (move.getFrom() == 0 || move.getTo() == 0) canCastleBlackQueenside = false;
                if (move.getFrom() == 7 || move.getTo() == 7) canCastleBlackKingside = false;
                if (move.getFrom() == 56 || move.getTo() == 56) canCastleWhiteQueenside = false;
                if (move.getFrom() == 63 || move.getTo() == 63) canCastleWhiteKingside = false;
            } else if (move.getMoveFlag() == 1) {
                if (move.getFrom() == 4 && move.getTo() == 2) { // black queenside castling
                    Square[3] = Piece.BLACK_ROOK;
                    Square[0] = Piece.EMPTY;
                    canCastleBlackQueenside = false;
                } else if (move.getFrom() == 4 && move.getTo() == 6) { // black kingside castling
                    Square[5] =  Piece.BLACK_ROOK;
                    Square[7] = Piece.EMPTY;
                    canCastleBlackKingside = false;
                } else if (move.getFrom() == 60 && move.getTo() == 58) { // white queenside castling
                    Square[59] = Piece.WHITE_ROOK;
                    Square[56] = Piece.EMPTY;
                    canCastleWhiteQueenside = false;
                } else if (move.getFrom() == 60 && move.getTo() == 62) {
                    Square[61] = Piece.WHITE_ROOK;
                    Square[63] = Piece.EMPTY;
                    canCastleWhiteKingside = false;
                } else {
                    return;
                }
            } else if (move.getMoveFlag() == 2) {
                Square[move.getTo()] = move.getPromotionPiece();
                Square[move.getFrom()] = Piece.EMPTY;
            } else if (move.getMoveFlag() == 3) {
                Square[move.getTo()] = movingPiece;
                Square[move.getFrom()] = Piece.EMPTY;
                if (isWhite(movingPiece)) {
                    Square[move.getTo() + 8] = Piece.EMPTY;
                } else {
                    Square[move.getTo() - 8] = Piece.EMPTY;
                }
            }

            if (targetPiece != Piece.EMPTY || movingPiece == Piece.WHITE_PAWN || movingPiece == Piece.BLACK_PAWN) {
                halfmoveClock = 0;
            } else {
                halfmoveClock++;
            }
            enPassantTarget = -1;
            if (movingPiece == Piece.WHITE_PAWN && move.getTo() - move.getFrom() == 16) {
                enPassantTarget = move.getFrom() + 8;
            } else if (movingPiece == Piece.BLACK_PAWN && move.getFrom() - move.getTo() == 16) {
                enPassantTarget = move.getFrom() - 8;
            }
            whiteToMove = !whiteToMove;
            if (!whiteToMove) {
                fullmoveCount++;
            }
        }
    }

    public boolean isLegalMove(Move move) {
        int movingPiece = Square[move.getFrom()];
        int targetSquare = Square[move.getTo()];
        int moveFlag = move.getMoveFlag();
        int promotionPiece = move.getPromotionPiece();

        if (whiteToMove != isWhite(movingPiece)) return false;

        if (movingPiece == Piece.EMPTY) return false;

        if (moveFlag != 2 && promotionPiece != Piece.EMPTY) return false;


        boolean isWhitePiece = isWhite(movingPiece);
        boolean isTargetPieceWhite = isWhite(targetSquare);

        if (isWhitePiece == isTargetPieceWhite && targetSquare != Piece.EMPTY) return false;

        if (moveFlag == 0) {
            if (!canMove(move.getFrom(), move.getTo(), movingPiece)) return false;
        } else if (moveFlag == 1) {
            if (!isCastlingMoveLegal(move)) return false;
        } else if (moveFlag == 2) {
            if (isWhite(movingPiece) != isWhite(promotionPiece)) return false;

            if (isWhite(movingPiece) && move.getTo() / 8 != 0) return false;
            if (!isWhite(movingPiece) && move.getTo() / 8 != 7) return false;

            int type = movingPiece & 0b00111;
            if (type != 0b010) return false;

            int promoType = promotionPiece & 0b00111;
            if (promoType < 0b011 || promoType > 0b110) return false;

            return true;
        } else if (moveFlag == 3){
            if ((movingPiece & 0b00111) != 0b010) return false;
            if (move.getTo() != enPassantTarget) return false;
            if (move.getTo() != move.getFrom() - 9 && move.getTo() != move.getFrom() - 7 && move.getTo() != move.getFrom() + 9 && move.getTo() != move.getFrom() + 7) return false;
        }
        return (movingPiece != Piece.WHITE_KING && movingPiece != Piece.BLACK_KING) || !isSquareAttacked(move.getTo(), !isWhitePiece);
    }

    private boolean isCastlingMoveLegal(Move move) {
        switch(move.getTo()) {
            case 2: {
                if (!canCastleBlackQueenside) return false;
                if (Square[1] != Piece.EMPTY || Square[2] != Piece.EMPTY || Square[3] != Piece.EMPTY) return false;
                if (isSquareAttacked(2, true) || isSquareAttacked(3, true) || isSquareAttacked(4, true)) {
                    return false;
                }
                break;
            }
            case 6: {
                if (!canCastleBlackKingside) return false;
                if (Square[5] != Piece.EMPTY || Square[6] != Piece.EMPTY) return false;
                if (isSquareAttacked(6, true) || isSquareAttacked(5, true) || isSquareAttacked(4, true)) {
                    return false;
                }
                break;
            }
            case 58: {
                if (!canCastleWhiteQueenside) return false;
                if (Square[57] != Piece.EMPTY || Square[58] != Piece.EMPTY || Square[59] != Piece.EMPTY) return false;
                if (isSquareAttacked(58, true) || isSquareAttacked(59, true) || isSquareAttacked(60, true)) {
                    return false;
                }
                break;
            }
            case 62: {
                if (!canCastleWhiteKingside) return false;
                if (Square[61] != Piece.EMPTY || Square[62] != Piece.EMPTY) return false;
                if (isSquareAttacked(60, true) || isSquareAttacked(61, true) || isSquareAttacked(62, true)) {
                    //checking if squares are attacked or if the king is in check
                    return false;
                }
                break;
            }
            default: break;
        }
        return true;
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
                if (to == from - 8 && Square[to] == Piece.EMPTY) return true;
                if (from >= 48 && from <= 55 && to == from - 16 && Square[to] == Piece.EMPTY && Square[from - 8] == Piece.EMPTY) return true;
                if (colDiff == 1 && rowDiff == 1 && !isWhite(Square[to]) && Square[to] != Piece.EMPTY) return true;
            }
            case Piece.BLACK_PAWN -> {
                if (to == from + 8 && Square[to] == Piece.EMPTY) return true;
                if (from >= 8 && from <= 15 && to == from + 16 && Square[to] == Piece.EMPTY && Square[from + 8] == Piece.EMPTY) return true;
                if (colDiff == 1 && rowDiff == 1 && isWhite(Square[to]) && Square[to] != Piece.EMPTY) return true;
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