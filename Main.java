import Board.*;

public class Main {
    public static void  main(String[] args) {
        Board board = new Board();
        board.display();
        Move move1 = new Move(14, 6, 2, Piece.WHITE_QUEEN);
        System.out.println(board.isLegalMove(move1));
        board.makeMove(move1);
        board.display();
    }
}
