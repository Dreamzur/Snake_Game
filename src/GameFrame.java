import javax.swing.*;

public class GameFrame extends JFrame {
    //creates the window for the snake game
    GameFrame() {

        this.add(new Game());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
