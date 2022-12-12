import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int DELAY = 50;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int snakeLength = 1;
    int applesEaten;
    int appleX, badAppleX;
    int appleY, badAppleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Image apple, head, body, enemy;
    Audio audio = new Audio();

    Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new keysPressed());
        audio.playSound();
        loadImages();
        startGame();
    }
    public void loadImages() {
        ImageIcon appl = new ImageIcon("src/resources/apple.png");
        apple = appl.getImage();
        ImageIcon hed = new ImageIcon("src/resources/head.png");
        head = hed.getImage();
        ImageIcon bod = new ImageIcon("src/resources/body.png");
        body = bod.getImage();
        ImageIcon enem = new ImageIcon("src/resources/enemy.png");
        enemy = enem.getImage();
    }

    public void startGame() {
        newApple();
        badApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            //drawing apple
            g.drawImage(apple, appleX, appleY, this);

            //drawing enemy (bad apple)
            g.drawImage(enemy, badAppleX, badAppleY, this);

            //drawing body parts of snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("arial", Font.PLAIN, 30));
            g.drawString("Score: " + applesEaten, 0, g.getFont().getSize());
        } else gameOver(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void newApple() {
        //generates a new apple
        appleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for (int i = 0; i < snakeLength; i++) {
            if (x[i] == appleX && y[i] == appleY) {
                newApple();
            }
        }
    }

    public void badApple() {
        //generates an enemy apple (bad apple)
        badAppleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        badAppleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for (int i = 0; i < snakeLength; i++) {
            if (x[i] == badAppleX && y[i] == badAppleY) {
                badApple();
            }
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeLength++;
            applesEaten++;
            newApple();
        } else if ((x[0] == badAppleX) && (y[0] == badAppleY)) {
            snakeLength--;
            applesEaten--;
            badApple();
        }
    }

    public void checkCollisions() {
        //checks if snake head collides with body
        for (int i = snakeLength; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        //checks if snake head touches borders
        if (x[0] < 0 || x[0] > WIDTH - UNIT_SIZE || y[0] < 0 || y[0] > HEIGHT - UNIT_SIZE) {
            running = false;
        }
        //checks timer
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //displays score
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        //setting game over text
        g.setColor(Color.red);
        g.setFont(new Font("Rockwell", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics2.stringWidth("Game Over")) / 2, HEIGHT / 2);
        //setting up restart text
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 25));
        g.drawString("Press SPACE to restart.", 160, HEIGHT / 2 + 50);


    }
    //whenever the game restarts a new window is created
    //audio is also restarted
    public void restartGame() {
        setVisible(false);
        new GameFrame();
        audio.stopSound();
        dispose();
    }

    public void dispose() {
        //closes last window
        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();
    }

    //keyboard input to control snake
    public class keysPressed extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            //controlling the snake
            if (running) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            } else {
                //if user pressed SPACE, game will restart
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    restartGame();
                }
            }
        }
    }
}
