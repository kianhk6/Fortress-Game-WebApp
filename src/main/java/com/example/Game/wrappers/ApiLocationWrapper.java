package com.example.Game.wrappers;


/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiLocationWrapper {
    public int row;
    public int col;
    public int[] coordinates = new int[2];

    public ApiLocationWrapper(int row, int col) {
        this.row = row;
        this.col = col;
        coordinates[0] = row;
        coordinates[1] = col;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
