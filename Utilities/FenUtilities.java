package Utilities;

import Board.Piece;

public class FenUtilities {

    public static final String startingPositionFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static boolean isValidFEN(String fen) {
        String[] parts = fen.split(" ");

        if (parts.length != 6) return false;

        String piecePlacement = parts[0];
        String activeColor = parts[1];
        String castlingRights = parts[2];
        String enPassant = parts[3];
        String halfMove = parts[4];
        String fullMove = parts[5];

        String[] rows = piecePlacement.split("/");
        if (rows.length != 8) return false;

        for (String row : rows) {
            if (!isValidRow(row)) return false;
        }

        if (!activeColor.equals("w") && !activeColor.equals("b")) return false;

        if (!castlingRights.matches("K?Q?k?q?|-")) return false;

        if (!enPassant.matches("([a-h][36]|-)")) return false;

        if (!halfMove.matches("\\d+")) return false;

        return fullMove.matches("[1-9]\\d*");
    }

    private static boolean isValidRow(String row) {
        int count = 0;

        for (char ch : row.toCharArray()) {
            if ("rnbqkpRNBQKP".indexOf(ch) != -1) {
                count++;
            } else if (Character.isDigit(ch)) {
                count += Character.getNumericValue(ch);
            } else {
                return false;
            }

            if (count > 8) return false;
        }

        return count == 8;
    }

    public static int[] fenToArray (String fen) {

        if (!isValidFEN(fen)) {
            return new int[0];
        }

        String position = fen.split(" ")[0];

        int[] board = new int[64];
        int index = 0;

        for (char c : position.toCharArray()) {
            if (Character.isDigit(c)) {
                index += Character.getNumericValue(c); // Skip empty squares
            } else if (c != '/') {
                board[index++] = pieceFromChar(c);
            }
        }

        return board;
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
