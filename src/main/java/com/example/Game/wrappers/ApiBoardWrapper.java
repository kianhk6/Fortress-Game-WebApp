package com.example.Game.wrappers;
import static com.example.Game.wrappers.ApiGameWrapper.gameModel;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiBoardWrapper {
    public int boardWidth = 10;
    public int boardHeight = 10;
    public int id;
    public String[][] cellStates = new String[10][10];

    public ApiBoardWrapper(int index) {
        id = index;
        String[][] UI = gameModel.getUIGrid();
        for(int i = 0; i <10; i++){
            System.arraycopy(UI[i], 0, cellStates[i], 0, 10);
        }
    }

    public void updateGameBoard(Boolean cheat){
        if(cheat){
            String[][] UI = gameModel.getCheatGrid();
            for(int i = 0; i <10; i++){
                System.arraycopy(UI[i], 0, cellStates[i], 0, 10);
            }
        }
        else{
            String[][] UI = gameModel.getUIGrid();
            for(int i = 0; i <10; i++){
                System.arraycopy(UI[i], 0, cellStates[i], 0, 10);
            }
        }

    }

    public int getId() {
        return id;
    }
}
