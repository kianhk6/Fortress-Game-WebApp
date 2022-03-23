package com.example.Game.model;
/**
 * The Grid class contains the Cell grid upon which the GameLogic operates on.
 * Data included is a 10x10 grid of Cells. Function responsibilities return the
 * status of individual cells based on a given set of x and y coordinates.
 */
public class Cell {
    //Cell
    private boolean isTank;
    private boolean isHit;
    private final int x;
    private final int y;
    private Cell parent;

    //there are 5 tanks, and we need to decide which tank the cell
    // is for the cheat part to
    // print the right letter
    private int whichTank;
    // Cell Constructor, sets the coordinates and booleans
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        isTank = false;
        isHit = false;
        //-1 in beginning since we haven't decided if it's a tank
        whichTank = -1;
    }

    protected void setNotTank(){
        isTank = false;
    }

    protected void setWhichTank(int tankNum){
        whichTank = tankNum;
    }

    protected int getWhichTank(){
        return whichTank;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected boolean getIsTank() {
        return isTank;
    }

    protected boolean getIsHit() {
        return isHit;
    }

    protected void setTank() {
        isTank = true;
    }

    protected void setHit() {
        isHit = true;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell cell){
        parent = cell;
    }
}
