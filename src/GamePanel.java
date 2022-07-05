import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // Game window width and height
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = ((SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE);

    // the higher the delay the slower the game
    static final int DELAY = 75;

    // the snake body parts array, holds coordinates of snake parts
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;

    // initialized at 0 with no values given
    int applesEaten;
    int appleX;
    int appleY;

    // Strings use "" while char uses '', char is a singular character
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    // game panel constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // starts game, called in the constructor
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        // Draws apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE,UNIT_SIZE);

        // Drawing snake, body + head
        for(int i = 0; i < bodyParts; i++){
            if(i == 0){
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else{
                g.setColor(new Color(45,180,0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    // creates a new apple on screen
    public void newApple(){
        // the instance variables are being modified in this method
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i>0; i--){
            // shifting coordinates over by 1 spot
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // move controls
        switch(direction){
            case 'U':
                y[0] = (y[0] - UNIT_SIZE);
                break;
            case 'D':
                y[0] = (y[0] + UNIT_SIZE);
                break;
            case 'L':
                x[0] = (x[0] - UNIT_SIZE);
                break;
            case 'R':
                x[0] = (x[0] + UNIT_SIZE);
                break;
        }
    }

    public void checkApple(){
        // if touched apple with head, add body part and add +1 to apple eaten
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        // checking if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        // checking if head touches left border
        if(x[0] < 0){
            running = false;
        }

        // checking if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        // checking if head touches top border
        if(y[0] < 0){
            running = false;
        }

        // checking if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        // stops timer if game is not running
        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){

    }

    @Override
    public void actionPerformed(ActionEvent e){
        // check if running == true
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
