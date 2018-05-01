import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Puzzelito extends JFrame implements ActionListener {

    private MainMenu main;

    public Puzzelito() {
        super("Puzzle");
        main = new MainMenu();

    }
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String args[]) {
        Puzzelito gm = new Puzzelito();
    }
}