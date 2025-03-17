import Board.Board;
import Board.Move;

public class Main {
    public static void  main(String[] args) {
        Board board = new Board();
        board.setStartingPosition();
        board.display();

        Move move = new Move(8, 16);
        board.makeMove(move);
        System.out.println("after move 1. :\n");
        board.display();
    }
}
