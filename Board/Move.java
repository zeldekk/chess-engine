package Board;

public class Move {

    private int from, to, moveFlag;

    public Move(int from, int to, int moveFlag) { //0 = normal move, 1 = castling, 2 = promotion, 3 = en passant
        this.from = from;
        this.to = to;
        this.moveFlag = moveFlag;
    }

    public int getFrom() { return from; }
    public int getTo() { return to; }
    public int getMoveFlag() { return  moveFlag; }
}
