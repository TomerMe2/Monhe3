import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu extends JFrame implements ActionListener {

    private static String _backgroundPath = "Background.jpg";

    public MainMenu() {
        super("Puzzle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);
        //Load the background
        Image bkrnd = getBkrnd();
        //Puzzelito Label
        JLabel puzzelitoLbl = new JLabel("Puzzelito");
        puzzelitoLbl.setFont(new Font(puzzelitoLbl.getFont().getName(), puzzelitoLbl.getFont().getStyle(), 60));
        //Start Puzzelito Button
        MenuBtn startGame = createBtn("Start Puzzelito!");
        //Exit Button
        MenuBtn exit = createBtn("Exit");
        //Adding Jobjects to contentPane
        getContentPane().add(puzzelitoLbl);
        getContentPane().add(startGame);
        getContentPane().add(exit);
        //Add the background
        getContentPane().add(new JLabel(new ImageIcon(bkrnd)));
        //Locating puzzelitoLbl
        layout.putConstraint(SpringLayout.NORTH, puzzelitoLbl, 80, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.WEST, puzzelitoLbl, 180, SpringLayout.WEST, getContentPane());
        //Locating startGame button
        layout.putConstraint(SpringLayout.NORTH, startGame, 150, SpringLayout.NORTH, puzzelitoLbl);
        layout.putConstraint(SpringLayout.WEST, startGame, 180, SpringLayout.WEST, getContentPane());
        //Location exit button
        layout.putConstraint(SpringLayout.NORTH, exit,130, SpringLayout.NORTH, startGame);
        layout.putConstraint(SpringLayout.WEST, exit, 180, SpringLayout.WEST, getContentPane());
        //Set this frame properties
        this.setSize(600,650);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            exit();
        }
        if (e.getActionCommand().equals("Start Puzzelito!")) {
            openGame();
        }
    }
    private Image getBkrnd() {
        Image bkrnd = null;
        try {
            bkrnd = ImageIO.read(new File("Images/" + _backgroundPath));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            System.exit(1);
            return null; //Wil never execute
        }
        bkrnd = bkrnd.getScaledInstance(600, 600, Image.SCALE_DEFAULT);
        return bkrnd;
    }

    private MenuBtn createBtn(String text) {
        MenuBtn btn = new MenuBtn(text);
        btn.setPreferredSize(new Dimension(250, 60));
        btn.addActionListener(this);
        return btn;
    }
    private void exit() {
        System.exit(0);
    }

    private void openGame() {
        new ChooseGame();
        dispose();
    }

    public static void main(String args[]) {
        new MainMenu();
    }
}
