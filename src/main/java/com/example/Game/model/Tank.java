package com.example.Game.model;

import java.util.ArrayList;
import java.util.List;
/**
 * The tank class models which cells on the Gameboard are grouped together as individual tanks.
 * Every tank has health, attack damage, and an arrayList of cells from the Grid.
 * Functions provided calculate the health of the tank, and how much damage it will do
 * based on its own health.
 */
public class Tank {
    public final int MAX_TANK_HEALTH = 5;
    public final int MAX_DMG = 20;
    public final int LOW_DMG = 2;
    public final int MEDIUM_LOW_DMG = 3;
    public final int MEDIUM_DMG = 5;
    public final int IMPOSSIBLE_SIZE = 6;
    private int tankHealth;
    private int tankAttackDMG;
    List<Cell> tankCells = new ArrayList<>();

    public Tank() {
        tankHealth = MAX_TANK_HEALTH;
        tankAttackDMG = MAX_DMG;
    }

    public void addCell(Cell cell){
        if(!tankCells.contains(cell)){
            tankCells.add(cell);
        }
    }

    public void removeCells(){
        tankCells.clear();

    }

    public void calcTankAttackDMG() {
        switch (tankHealth) {
            case 0:
                tankAttackDMG = 0;
                break;

            case 1:
                tankAttackDMG = 1;
                break;

            case 2:
                tankAttackDMG = LOW_DMG;
                break;

            case 3:
                tankAttackDMG = MEDIUM_LOW_DMG;
                break;

            case 4:
                tankAttackDMG = MEDIUM_DMG;
                break;

            case 5:
                tankAttackDMG = MAX_DMG;
                break;

            default:
                assert tankCells.size() < IMPOSSIBLE_SIZE;
        }
    }

    public void calcTankHP() {
        int aliveCells = 0;
        for (Cell cell : tankCells) {
            if (!cell.getIsHit()) {
                aliveCells++;
            }
        }
        tankHealth = aliveCells;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public int getTankAttackDMG() {
        return tankAttackDMG;
    }
}
