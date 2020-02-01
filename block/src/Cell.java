public class Cell {
    private int col,row;//列和行


    public Cell() {
    }

    public Cell(int col,int row) {
        this.col = col;
        this.row = row;
    }

    public void right(){
        col++;
    }

    public void left(){
        col--;
    }

    public void drop(){
        row++;
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
