package com.example.Game.model;
/**
 * The Grid class contains the Cell grid upon which the GameLogic operates on.
 * Data included is a 10x10 grid of Cells. Function responsibilities return the
 * status of individual cells based on a given set of x and y coordinates.
 */
public class Grid {
    public final int MAX_ROWS = 10;
    public final int MAX_COLUMNS = 10;
    //Grid
    Cell[][] grid = new Cell[10][10];

    public Grid() {
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public boolean isCellTank(int x, int y) {
        return grid[x][y].getIsTank();
    }

    public boolean isCellHit(int x, int y) {
        return grid[x][y].getIsHit();
    }

    public void setTank(int x, int y) {
        Cell cell = grid[x][y];
        cell.setTank();
    }

    public void setHit(int x, int y) {
        Cell cell = grid[x][y];
        cell.setHit();
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public void setNotTank(int i, int j) {
        Cell cell = grid[i][j];
        cell.setNotTank();
    }
}
