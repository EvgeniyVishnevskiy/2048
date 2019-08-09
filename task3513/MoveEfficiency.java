package com.javarush.task.task35.task3513;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public Move getMove() {
        return move;
    }

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        /*int x = numberOfEmptyTiles - o.numberOfEmptyTiles;
        int y = score - o.score;
        if(x != 0) {return x;}
        else { if (y != 0) {return y;}
        else {return 0;}}*/
        Integer x = Integer.compare(this.numberOfEmptyTiles, o.numberOfEmptyTiles);
        Integer y = Integer.compare(this.score, o.score);
        if (x != 0) return x;
        else if(y != 0) return y;
        else return 0;

    }
}
