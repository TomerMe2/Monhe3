import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class PuzzleGame extends JFrame implements ActionListener{

    public PuzzleGame(int n) {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800,800);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
