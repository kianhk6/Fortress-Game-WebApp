package com.example.Game.wrappers;

import com.example.Game.model.GameLogic;
import com.example.Game.model.Grid;
import com.example.Game.model.Tank;
import java.util.List;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiGameWrapper {
    public final int MAX_HEALTH = 2500;
    public final int MAX_TANK = 5;
    public final int MAX_DMG = 20;
    public int gameNumber;
    public boolean isGameWon = false;
    public boolean isGameLost = false;
    public int fortressHealth = MAX_HEALTH;
    public int numTanksAlive = MAX_TANK;
    static GameLogic gameModel;
    public boolean cheat = false;
    // Amount of damage that the tanks did on the last time they fired.
    // If tanks have not yet fired, then it should be an empty array (0 size).

    public int[] lastTankDamages = new int[MAX_TANK];

    public boolean isCheat() {
        return cheat;
    }

    public void setCheat(boolean cheat) {
        this.cheat = cheat;
    }

    public ApiGameWrapper(int gameNumber) {
        this.gameNumber = gameNumber;
        for(int i = 0; i < MAX_TANK; i++){
            lastTankDamages[i] = MAX_DMG;
        }
        gameModel = new GameLogic(MAX_TANK, new Grid());
    }

    public void userTurn(int[] coordinates){
        if(gameModel.getLogicGrid().isCellHit(coordinates[0], coordinates[1])){
            return;
        }
        gameModel.userTurn(coordinates);
        updateLastTankDamages();
        damageFortress();
        updateResult();
    }

    public void updateLastTankDamages(){
        List<Tank> tankList = gameModel.getTankArray();
        int numAliveTanks = 0;
        int i = 0;
        for(Tank tank : tankList){
            tank.calcTankHP();
            if(tank.getTankHealth() != 0){
                numAliveTanks++;
            }
            tank.calcTankAttackDMG();
            lastTankDamages[i] = tank.getTankAttackDMG();
            i++;
        }

        numTanksAlive = numAliveTanks;
    }

    public void damageFortress(){
        int damages = 0;
        for(int i = 0; i < MAX_TANK; i++){
            damages = lastTankDamages[i] + damages;
        }
        fortressHealth = fortressHealth - damages;
        gameModel.setFortressHealth(fortressHealth);
    }

    public void updateResult(){
        if(gameModel.getGameFinished()){
            if(gameModel.getUserTheWinner()){
                isGameWon = true;
            }
            else{
                isGameLost = true;
            }
        }
    }

    public int getId() {
        return gameNumber;
    }
}
