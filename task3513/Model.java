package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private final static int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    int score;
    int maxTile;
    Stack<Tile[][]> previousStates = new Stack();
    Stack<Integer> previousScores = new Stack();
    boolean isSaveNeeded = true;

    private void saveState(Tile[][] tiles) {
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                temp[i][j] = new Tile(gameTiles[i][j].value);
            }
        }
        previousStates.push(temp);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }


    public Model() {
        resetGameTiles();
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (list.size() == 0) return;
        Tile randomTile = list.get((int) (Math.random() * list.size()));
        randomTile.value = Math.random() < 0.9 ? 2 : 4;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) list.add(gameTiles[i][j]);
            }
        }
        return list;
    }

    void resetGameTiles() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 0;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean flag = false;
        for (int j = 0; j < FIELD_WIDTH - 1; j++)
            for (int i = 0; i < FIELD_WIDTH - 1; i++) {
                if (tiles[i].value == 0) {
                    if (tiles[i + 1].value == 0) continue;
                    tiles[i].value = tiles[i + 1].value;
                    tiles[i + 1].value = 0;
                    flag = true;
                }
            }
        return flag;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean flag = false;
        for (int i = 0; i < FIELD_WIDTH - 1; i++) {
            if (tiles[i].value == tiles[i + 1].value) {
                if (tiles[i].value == 0) continue;
                tiles[i].value = tiles[i].value * 2;
                tiles[i + 1].value = 0;
                if (tiles[i].value > maxTile) maxTile = tiles[i].value;
                score = score + tiles[i].value;
                compressTiles(tiles);
                flag = true;
            }
        }
        return flag;
    }

    void left() {
        if(isSaveNeeded) saveState(gameTiles);
        boolean flag1 = false;
        boolean flag2 = false;
        for (Tile[] arr : gameTiles) {
            if (compressTiles(arr)) flag1 = true;
            if (mergeTiles(arr)) flag2 = true;
        }
        if (flag1 || flag2) addTile();
        isSaveNeeded = true;
    }

    Tile[][] turnToRight(Tile[][] array) {
        int m = FIELD_WIDTH;
        for (int k = 0; k < m / 2; k++) {
            for (int j = k; j < m - 1 - k; j++) {
                Tile tmp = array[k][j];
                array[k][j] = array[j][m - 1 - k];
                array[j][m - 1 - k] = array[m - 1 - k][m - 1 - j];
                array[m - 1 - k][m - 1 - j] = array[m - 1 - j][k];
                array[m - 1 - j][k] = tmp;
            }
        }
        return array;
    }

    void up() {
        saveState(gameTiles);
        turnToRight(gameTiles);
        left();
        turnToRight(gameTiles);
        turnToRight(gameTiles);
        turnToRight(gameTiles);
    }

    void right() {
        saveState(gameTiles);
        turnToRight(gameTiles);
        turnToRight(gameTiles);
        left();
        turnToRight(gameTiles);
        turnToRight(gameTiles);
    }

    void down() {
        saveState(gameTiles);
        turnToRight(gameTiles);
        turnToRight(gameTiles);
        turnToRight(gameTiles);
        left();
        turnToRight(gameTiles);
    }

    public boolean canMove() {
        if (getEmptyTiles().size() != 0) return true;
        for (int i = 0; i < FIELD_WIDTH - 1; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (gameTiles[i][j].value == gameTiles[i + 1][j].value ||
                        gameTiles[i][j].value == gameTiles[i][j + 1].value) return true;
            }

        }
        return false;
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0: left();
            break;
            case 1: right();
            break;
            case 2: up();
            break;
            case 3: down();
            break;
        }
    }

    public boolean hasBoardChanged() {
        boolean flag = false;
        Tile[][] peek = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value != peek[i][j].value) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        if(!hasBoardChanged()) {
            return new MoveEfficiency(-1, 0, move);
        }
        else {
            rollback();
            return new MoveEfficiency(getEmptyTiles().size(), score, move);
        }
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> pq = new PriorityQueue(4, Collections.reverseOrder());
        pq.offer(getMoveEfficiency(this::left));
        pq.offer(getMoveEfficiency(this::right));
        pq.offer(getMoveEfficiency(this::up));
        pq.offer(getMoveEfficiency(this::down));
        pq.peek().getMove().move();
    }
}
