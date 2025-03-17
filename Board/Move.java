package Board;

public class Move {

    private int from, to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() { return from; }
    public int getTo() { return to; }

    public boolean isLegalMove() {
        if (!(from >= 0 && from < 64 && to >= 0 && to < 64)) {
            return false;
        }
        switch (1) {}
        return false;
    }
}
