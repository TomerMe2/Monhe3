import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Puzzelito extends JFrame implements ActionListener {

    private MainMenu _main;

    public Puzzelito() {
        super("Puzzle");
        _main = new MainMenu();

    }
    public void actionPerformed(ActionEvent e) {

    }


}