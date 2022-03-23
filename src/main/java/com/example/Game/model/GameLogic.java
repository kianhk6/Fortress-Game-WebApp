package com.example.Game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.abs;
/**
 * GameLogic class is responsible for processing data from the UI and making a decision afterwards.
 * Data included is the fortress health, the number of tanks, a 2D array for the UI, a 2D cheat UI array,
 * an array of tank identifiers, and a grid for game logic. The primary function of GameLogic is to generate
 * tanks and manage the user's turn.
 */
public class GameLogic {
    public final int MAX_ROWS = 10;
    public final int MAX_COLUMNS = 10;
    public final int MAX_FORTRESS_HEALTH = 2500;
    public final int MAX_CORNER = 9;
    public final int MIN_CORNER = 0;
    private int FortressHealth;
    private final int numOfTanks;
    protected String[][] UIGrid = new String[MAX_ROWS][MAX_COLUMNS];
    protected String[][] cheatGrid = new String[MAX_ROWS][MAX_COLUMNS];
    Grid logicGrid;

    List<Tank> tankArray = new ArrayList<>();

    public void setUIGrid(String[][] UIGrid) {
        this.UIGrid = UIGrid;
    }

    public void setCheatGrid(String[][] cheatGrid) {
        this.cheatGrid = cheatGrid;
    }

    public Grid getLogicGrid() {
        return logicGrid;
    }

    public void setLogicGrid(Grid logicGrid) {
        this.logicGrid = logicGrid;
    }



    public void setTankArray(List<Tank> tankArray) {
        this.tankArray = tankArray;
    }

    public List<Tank> getTankArray() {
        return tankArray;
    }

    public GameLogic(int numOfTanks, Grid grid){
        setFortressHealth(MAX_FORTRESS_HEALTH);
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                UIGrid[i][j] = "fog";
                cheatGrid[i][j] = "field";
            }
        }
        this.numOfTanks = numOfTanks;
        logicGrid = grid;
        generateTanks();

    }

    private void updateGameboard(){
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                if(logicGrid.isCellTank(i,j)){
                    cheatGrid[i][j] = "tank";
                }
                if(logicGrid.isCellHit(i, j) && !logicGrid.isCellTank(i, j)){
                    cheatGrid[i][j] = "miss";
                    UIGrid[i][j] =  "miss";
                }
                else if(logicGrid.isCellHit(i, j) && logicGrid.isCellTank(i, j)){
                    cheatGrid[i][j] = "hit";
                    UIGrid[i][j] = "hit";
                }
            }
        }
    }

    //Game Logic
    private ArrayList<Cell> getChildren(int x, int y){
        ArrayList<Cell> children = new ArrayList<>();
        if(x < MAX_CORNER){
            if(!logicGrid.getCell(x+1,y).getIsTank()){
                children.add(logicGrid.getCell(x+1,y));
            }
        }
        if( y < MAX_CORNER){
            if(!logicGrid.getCell(x,y+1).getIsTank()){
                children.add(logicGrid.getCell(x, y+1));
            }
        }
        if( x > MIN_CORNER){
            if( !logicGrid.isCellTank(x-1,y)){
                children.add(logicGrid.getCell(x-1, y));
            }
        }
        if(y > MIN_CORNER){
            if(!logicGrid.getCell(x,y-1).getIsTank()){
                children.add(logicGrid.getCell(x, y-1));
            }
        }
        return children;
    }

    public void generateTanks(){
        for(int i = 0; i < numOfTanks; i++){
            Random rand = new Random();
            Tank tank = new Tank();
            int x = rand.nextInt() & Integer.MAX_VALUE %MAX_ROWS;
            int y = rand.nextInt() & Integer.MAX_VALUE %MAX_COLUMNS;
            while (logicGrid.isCellTank(x, y)){
                x = rand.nextInt() & Integer.MAX_VALUE %MAX_ROWS;
                y = rand.nextInt() & Integer.MAX_VALUE %MAX_COLUMNS;
            }
            ArrayList<Cell> Children = getChildren(x, y);
            //generate random cell till the starting point has the least one child or neighbour
            while (Children.size() == 0) {
                x = abs(rand.nextInt()%MAX_ROWS);
                y = abs(rand.nextInt()%MAX_COLUMNS);
                Children = getChildren(x, y);
            }

            //make it a tank
            logicGrid.setTank(x,y);
            tank.addCell(logicGrid.getCell(x, y));
            logicGrid.getCell(x, y).setWhichTank(i);
            Cell current = addFrom(logicGrid.getCell(x, y), tank, i);

            while(tank.tankCells.size() < 5){
                //adding children from this place
                ArrayList<Cell> nextChildChildren = getChildren(current.getX(), current.getY());
                if(nextChildChildren.size() != 0){
                    //current = current->next
                    current = addFrom(current, tank, i);
                }
                else{
                    while(getChildren(current.getX(), current.getY()).size() == 0 && current.getParent() != null ){
                        current = current.getParent();
                    }
                    current = addFrom(current, tank, i);
                    if(current == null){
                        regenerateTank();
                        return;
                    }
                }
            }
            tankArray.add(tank);
        }
        updateGameboard();
    }
    private void regenerateTank(){
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLUMNS; j++){
                logicGrid.setNotTank(i,j);
            }
        }
        for(Tank tank : tankArray){
            tank.removeCells();
        }
        tankArray.clear();
        updateGameboard();
        generateTanks();
        updateGameboard();
    }

    private Cell addFrom(Cell origin, Tank tank, int tankId) {
        //select which child
        Random rand = new Random();
        ArrayList<Cell> Children = getChildren(origin.getX(), origin.getY());

        int size = Children.size();
        if (size < 1) {
            return null;
        }

        int randomChildIndex = rand.nextInt(size); // size have to be at least 1.
        //make the cell to tan
        Cell child = Children.get(randomChildIndex);

        logicGrid.setTank(child.getX(), child.getY());
        child.setWhichTank(tankId);
        tank.addCell(child);
        child.setParent(origin);
        return child;
    }


    //returns if its users turn
    public Boolean userTurn(int[] coordinates){
        int x = coordinates[0];
        int y =  coordinates[1];

        if(logicGrid.isCellTank(x, y)){
            logicGrid.setHit(x, y);
            updateGameboard();
            return true;
        }
        else{
            logicGrid.setHit(x, y);
            updateGameboard();
            return false;
        }
    }


    public int getFortressHealth() {
        return FortressHealth;
    }

    public void setFortressHealth(int fortressHealth) {
        FortressHealth = fortressHealth;
    }

    public int getNumOfTanks() {
        return numOfTanks;
    }


    public Boolean getUserTheWinner() {
        return areAllTanksDead();
    }



    public Boolean getGameFinished() {
        return areAllTanksDead() || FortressHealth <= 0;
    }

    private boolean areAllTanksDead() {
        if(tankArray.size() == 0){
            return false;
        }
        for(Tank tank : tankArray){
            if(tank.getTankHealth() != 0){
                return false;
            }
        }
        return true;
    }


    public String[][] getUIGrid() {
        return UIGrid;
    }



    public String[][] getCheatGrid() {
        return cheatGrid;
    }




}
