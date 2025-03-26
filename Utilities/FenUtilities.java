package Utilities;

import Board.Piece;

public class FenUtilities {

    public static final String startingPositionFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static class BoardState {
        public int[] board;
        public boolean whiteToMove;
        public boolean canCastleWhiteKingside, canCastleWhiteQueenside;
        public boolean canCastleBlackKingside, canCastleBlackQueenside;
        public int enPassantTarget;
        public int halfmoveClock;
        public int fullmoveNumber;
    }

    public static BoardState fenToBoardState(String fen) {
        BoardState state = new BoardState();

        String[] parts = fen.split(" ");
        if (parts.length != 6) throw new IllegalArgumentException("Invalid FEN");

        state.board = parseBoard(parts[0]);
        state.whiteToMove = parts[1].equals("w");
        state.canCastleWhiteKingside = parts[2].contains("K");
        state.canCastleWhiteQueenside = parts[2].contains("Q");
        state.canCastleBlackKingside = parts[2].contains("k");
        state.canCastleBlackQueenside = parts[2].contains("q");
        state.enPassantTarget = parseEnPassant(parts[3]);
        state.halfmoveClock = Integer.parseInt(parts[4]);
        state.fullmoveNumber = Integer.parseInt(parts[5]);

        return state;
    }

    private static int[] parseBoard(String boardFen) {
        int[] board = new int[64];
        int index = 0;

        for (char c : boardFen.toCharArray()) {
            if (Character.isDigit(c)) {
                index += Character.getNumericValue(c);
            } else if (c != '/') {
                board[index++] = pieceFromChar(c);
            }
        }
        return board;
    }

    private static int parseEnPassant(String enPassant) {
        if (enPassant.equals("-")) return -1;
        int file = enPassant.charAt(0) - 'a';
        int rank = enPassant.charAt(1) - '1';
        return rank * 8 + file;
    }

    private static int pieceFromChar(char c) {
        return switch (c) {
            case 'K' -> Piece.WHITE_KING;
            case 'P' -> Piece.WHITE_PAWN;
            case 'N' -> Piece.WHITE_KNIGHT;
            case 'B' -> Piece.WHITE_BISHOP;
            case 'R' -> Piece.WHITE_ROOK;
            case 'Q' -> Piece.WHITE_QUEEN;
            case 'k' -> Piece.BLACK_KING;
            case 'p' -> Piece.BLACK_PAWN;
            case 'n' -> Piece.BLACK_KNIGHT;
            case 'b' -> Piece.BLACK_BISHOP;
            case 'r' -> Piece.BLACK_ROOK;
            case 'q' -> Piece.BLACK_QUEEN;
            default -> Piece.EMPTY;
        };
    }
}
