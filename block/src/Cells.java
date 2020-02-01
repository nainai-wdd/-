import java.util.Random;

public class Cells {
    Cell[] cells=new Cell[4];
    int rotateCounter=1000;

    public void cellsRight(){
        for (Cell c : cells) {
            c.right();
        }
    }

    public void cellsleft(){
        for (Cell c : cells) {
            c.left();
        }
    }

    public  void cellsDrop(){
        for (Cell c : cells) {
            c.drop();
        }
    }

    public void cellsLRotate(){
        int[] x=new int[3];
        int[] y=new int[3];
        for (int i = 0; i < 3; i++) {
            y[i] = cells[0].getRow() - cells[i + 1].getCol() + cells[0].getCol();
            x[i] = cells[0].getCol() + cells[i + 1].getRow() - cells[0].getRow();
            if (0 > x[i] || x[i] > 9 || 0 > y[i] || y[i] > 19) {
                return;
            }
        }
            for (int i1 = 0; i1 < 3; i1++) {
                cells[i1 + 1].setRow(y[i1]);
                cells[i1+ 1].setCol(x[i1]);
            }
    }

    public void cellsRRotate(){
        int[] x=new int[3];
        int[] y=new int[3];
        for (int i = 0; i < 3; i++) {
            y[i]=cells[0].getRow()+cells[i+1].getCol()-cells[0].getCol();
            x[i]=cells[0].getCol()-cells[i+1].getRow()+cells[0].getRow();
            if (0>x[i]||x[i]>9||0>y[i]||y[i]>19){
                return;
            }
        }
            for (int i1 = 0; i1 < 3; i1++) {
                cells[i1 + 1].setRow(y[i1]);
                cells[i1+ 1].setCol(x[i1]);
            }
    }

    public static Cells randomOne(){
        Random random = new Random();
        switch (random.nextInt(7)){
            case 0:return new I();
            case 1:return new T();
            case 2:return new S();
            case 3:return new Z();
            case 4:return new J();
            case 5:return new J();
            case 6:return new O();
        }
        return new Z();
    }
}
