import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu extends JFrame implements ActionListener {

    public MainMenu() {
        super("Puzzle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);
        //Puzzelito Label
        JLabel puzzelitoLbl = new JLabel("Puzzelito");
        puzzelitoLbl.setFont(new Font(puzzelitoLbl.getFont().getName(), puzzelitoLbl.getFont().getStyle(), 60));
        //Start Puzzelito Button
        JButton startGame = new JButton("Start Puzzelito!");
        startGame.setPreferredSize(new Dimension(250, 60));
        startGame.setFont(new Font(startGame.getFont().getName(), startGame.getFont().getStyle(), 30));
        startGame.addActionListener(this);
        //Exit Button
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(250,60));
        exit.setFont(new Font(exit.getFont().getName(), exit.getFont().getStyle(), 30));
        exit.addActionListener(this);
        //Adding Jobjects to contentPane
        getContentPane().add(puzzelitoLbl);
        getContentPane().add(startGame);
        getContentPane().add(exit);
        //Locating puzzelitoLbl
        layout.putConstraint(SpringLayout.NORTH, puzzelitoLbl, 45, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.WEST, puzzelitoLbl, 240, SpringLayout.WEST, getContentPane());
        //Locating startGame button
        layout.putConstraint(SpringLayout.NORTH, startGame, 130, SpringLayout.NORTH, puzzelitoLbl);
        layout.putConstraint(SpringLayout.WEST, startGame, 230, SpringLayout.WEST, getContentPane());
        //Location exit button
        layout.putConstraint(SpringLayout.NORTH, exit,130, SpringLayout.NORTH, startGame);
        layout.putConstraint(SpringLayout.WEST, exit, 230, SpringLayout.WEST, getContentPane());
        //Set this frame properties
        this.setSize(800, 500);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            exit();
        }
        if (e.getActionCommand().equals("Start Puzzelito!")) {
            openGame();
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void openGame() {
        this.dispose();
        ChooseGame pzlGame = new ChooseGame();
    }
}
