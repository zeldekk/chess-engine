import Board.Board;
import Board.Move;
import Utilities.FenUtilities;
import java.util.Arrays;

public class Main {
    public static void  main(String[] args) {
        Board board = new Board();
        board.display();

        Move move = new Move(8, 16);
        board.makeMove(move);
        System.out.println("after move 1. :\n");
        board.display();

        System.out.println(Arrays.toString(FenUtilities.fenToArray(FenUtilities.startingPositionFen)));
    }
}
