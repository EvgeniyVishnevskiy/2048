package com.javarush.task.task35.task3513;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private Model model;
    private View view;

    public View getView() {
        return view;
    }

    private final static int WINNING_TILE = 2048;

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    void resetGame() {
        view.isGameLost = false;
        view.isGameWon = false;
        model.score = 0;
        model.resetGameTiles();
        model.maxTile = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_ESCAPE)  resetGame();
        if(!model.canMove()) view.isGameLost = true;
        if(!view.isGameLost && !view.isGameWon) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT: model.left();
                break;
                case KeyEvent.VK_RIGHT: model.right();
                break;
                case KeyEvent.VK_UP: model.up();
                break;
                case KeyEvent.VK_DOWN: model.down();
                break;
                case KeyEvent.VK_Z: model.rollback();
                break;
                case KeyEvent.VK_R: model.randomMove();
                break;
                case KeyEvent.VK_A: model.autoMove();
                break;
            }
        }
        if(model.maxTile == WINNING_TILE) view.isGameWon = true;
        view.repaint();
        //super.keyPressed(e);
    }
}
