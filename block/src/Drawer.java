import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Drawer extends JPanel {
    boolean[][] wall=new boolean[20][10];
    int[] score={1,3,6,10};
    final int CELL_SIZE=27;
    private Cells currentOne = Cells.randomOne();//正在下落的Cells
    private Cells nextOne = Cells.randomOne();//下一个Cells
    private int totalScore = 0;//总分
    private int totalLine = 0;//总行数
    private int grade=0;
    //定义游戏的三种状态常量:正在游戏0,暂停1,游戏结束2:
    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAMEOVER = 2;
    private int game_state;
    //这个数组用来显示游戏状态文字:
    String[] showState = { "P[暂停]", "C[继续]", "Enter[重玩]" };


    public void paintCurrentOne(Graphics g) {//画出运动块
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            int x = c.getCol() * CELL_SIZE;
            int y = c.getRow() * CELL_SIZE;
            g.fillRect( x, y,CELL_SIZE,CELL_SIZE);
        }
    }

    public void paintWall(Graphics g) {
        // 外层循环控制行数
        for (int i = 0; i < 20; i++) {
            // 内层循环控制列数
            for (int j = 0; j < 10; j++) {
                if (wall[i][j]) {
                    int x = j * CELL_SIZE;
                    int y = i * CELL_SIZE;
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void paintState(Graphics g) {//在右侧绘制游戏状态
        setFont(new Font("",1,30));
        if (game_state == GAMEOVER) {//游戏结束
            g.drawString(showState[GAMEOVER], 285, 400);
        }
        if (game_state == PLAYING) {//正在游戏
            g.drawString(showState[PLAYING], 285, 400);
        }
        if (game_state == PAUSE) {//暂停游戏
            g.drawString(showState[PAUSE], 285, 400);
        }
    }

    public void paintScore(Graphics g) {//在右侧位置绘制游戏分数
        g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 26));
        g.drawString("分数:" + totalScore, 285, 200);
        g.drawString("已消除行数:" + totalLine, 285, 250);
        g.drawString("速度:" + grade, 285, 300);
    }

    public void paintNextOne(Graphics g) {
        // 获取nextOne对象的四个元素
        Cell[] cells = nextOne.cells;
        for (Cell c : cells) {// 获取每一个元素的行号和列号
            int row = c.getRow();
            int col = c.getCol();// 横坐标和纵坐标
            int x = col * CELL_SIZE + 260;
            int y = row * CELL_SIZE + 50;
            g.drawRect(x,y,CELL_SIZE,CELL_SIZE);
        }
    }

    public void paintBackground(Graphics g){
        for (int i = 0; i < 20; i++) {//第i行
            for (int j = 0; j < 10; j++) {//第j列
                g.drawRect(j*CELL_SIZE,i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
            }
        }
    }





    public boolean leftBoundary(Cells cs) {
        boolean b = false;
        for (Cell c : cs.cells) {
            if (c.getCol() != 0) {
                b = (b || wall[c.getRow()][c.getCol() - 1]);
            }
            else return false;
        }
        return !b;
    }

    public boolean rightBoundary(Cells cs) {
        boolean b = false;
        for (Cell c : cs.cells) {
            if(c.getCol() != 9) {
                b = (b || wall[c.getRow()][c.getCol() + 1]);
            }
            else return false;

        }
        return !b;
    }

    public boolean canDrop(Cells cs){
        boolean b = false;
        for (Cell c : cs.cells) {
            if (c.getRow()==19) {
                for (Cell c1 : cs.cells) {
                    wall[c1.getRow()][c1.getCol()]=true;
                }
                return false;
            }
            else b = (b || wall[c.getRow() + 1][c.getCol()]);
        }
        if (b){
            for (Cell c : cs.cells) {
                wall[c.getRow()][c.getCol()]=true;
            }
            return false;
        }
        else return true;
    }

    public void destroyLine(){
        int sum=0;
        int[] s={-1,-1,-1,-1};
        for (int i = 0; i < 20; i++) {
            boolean b=true;
            for (int j = 0; j < 10; j++) {
                b=(b&&wall[i][j]);
            }
            if (b){
                    for (int j1 = 0; j1 < 10; j1++) {
                        wall[i][j1]=false;
                    }
                    s[sum]=i;
                    sum++;
            }
        }
        if(sum>0){
            //这里可以设置睡眠时间
            for (int i = 0; i < sum; i++) {
                for (int i1 = s[i]; i1 > 0; i1--) {
                    for (int j1 = 0; j1 < 10; j1++){
                        wall[i1][j1]=wall[i1-1][j1];
                    }
                }
            }
            totalLine=totalLine+sum;
            totalScore=totalScore+score[sum-1];
            if (grade<5)
            grade=(totalLine*10+totalScore)/30;
        }

    }

    public boolean gameOver(Cells cs){
            for (Cell c : cs.cells) {
                if (wall[c.getRow()][c.getCol()]){
                    //绘制gameover图片，结束线程
                    return true;
            }

        }
        return false;
    }

    public void moveLeftAction(Cells c){
        if (leftBoundary(c)){
            c.cellsleft();
        }
    }

    public void moveRightAction(Cells c){
        if (rightBoundary(c)){
            c.cellsRight();
        }
    }

    public void moveDropAction(Cells c){
        if (canDrop(c)){
            c.cellsDrop();
        }
    }

    public void rotateAction(Cells c){
        if (c instanceof O){
            return;
        }
        else if ((c instanceof Z)||(c instanceof S)){
            if(0==c.rotateCounter%2)
                c.cellsLRotate();
            else c.cellsRRotate();
            c.rotateCounter++;

        }
        else c.cellsRRotate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(15, 15);
        paintBackground(g);
        paintState(g);
        paintScore(g);
        paintNextOne(g);
        paintWall(g);
        paintCurrentOne(g);
        g.drawString("下一块",280,30);
    }


    public void start() {
        KeyAdapter l = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_P) {//VK_P即表示键盘P键

                    if (game_state == PLAYING) {//状态为PLAYING才能暂停

                        game_state = PAUSE;

                    }

                }
                if (code == KeyEvent.VK_C) {//按C键继续游戏

                    if (game_state == PAUSE) {

                        game_state = PLAYING;

                    }

                }
                if (code == KeyEvent.VK_ENTER) {
                    game_state = PLAYING;
                    for (int i = 0; i < 20; i++) {
                        for (int j = 0; j < 10; j++) {
                            wall[i][j]=false;
                        }
                    }
                    currentOne = Cells.randomOne();
                    nextOne = Cells.randomOne();
                    totalScore = 0;//分数置为0
                    totalLine = 0;//列数置为0
                    grade=0;//等级重置为0
            }
                if (PLAYING==game_state) {
                    switch (code) {

                        case KeyEvent.VK_DOWN://按下缓慢下降

                            moveDropAction(currentOne);

                            break;

                        case KeyEvent.VK_LEFT://按左左移

                            moveLeftAction(currentOne);

                            break;

                        case KeyEvent.VK_RIGHT://按右右移

                            moveRightAction(currentOne);

                            break;

                        case KeyEvent.VK_UP://按上变形

                            rotateAction(currentOne);

                            break;
                        case KeyEvent.VK_SPACE://按空格直接落底
                            while (canDrop(currentOne)) {
                                moveDropAction(currentOne);
                            }
                    }
                }
                updateUI();
            };
        };
        this.addKeyListener(l);
        this.requestFocus();
        while(true){
                try {
                    Thread.sleep(800-grade*100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (game_state == PLAYING) {
                    if (canDrop(currentOne)) {
                        moveDropAction(currentOne);
                    } else {
                        destroyLine();
                        currentOne = nextOne;
                        nextOne = Cells.randomOne();
                        if (gameOver(currentOne)) {
                            game_state = GAMEOVER;
                        }
                    }
                }
            updateUI();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("俄罗斯方块");
        frame.setBackground(Color.white);
        // 创建游戏界面，即面板

        Drawer panel = new Drawer();
        panel.setBackground(Color.white);

        // 将面板嵌入窗口

        frame.add(panel);

        // 2:设置为可见

        frame.setVisible(true);

        // 3:设置窗口的尺寸

        frame.setSize(535, 600);

        // 4:设置窗口居中

        frame.setLocationRelativeTo(null);

        // 5:设置窗口关闭，即程序终止

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 游戏的主要逻辑封装在start方法中

        panel.start();
    }
}
