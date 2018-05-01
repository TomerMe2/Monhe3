import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Game extends JFrame implements ActionListener {
    public Game() {
        super("Puzzle");
    }
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String args[]) {
        Game gm = new Game();
    }
}