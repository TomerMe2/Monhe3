import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ChooseGame extends JFrame implements ActionListener {

    private JButton _pokemonBtn, _batmanBtn, _sonicBtn, _avengersBtn;
    private static String _pokemonPath = "Pokemon.png",
            _batmanPath = "Batman.jpg",
            _sonicPath = "Sonic.jpg",
            _avengersPath = "Avengers.jpeg";
    private JTextField _txtBxN;
    private PuzzleGame pzlItself;
    public ChooseGame() {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);
        pzlItself = null;
        //N Label
        JLabel nLbl = new JLabel("Choose your board size!");
        nLbl.setFont(new Font(nLbl.getFont().getName(), nLbl.getFont().getStyle(), 30));
        //Text Field for N
        _txtBxN = new JTextField(3);
        _txtBxN.setPreferredSize(new Dimension(40,70));
        _txtBxN.setFont(new Font(_txtBxN.getFont().getName(), _txtBxN.getFont().getStyle(), 30));
        //help Label
        JLabel help = new JLabel("for example: if you want a board with the size 4x4 insert 4");
        help.setFont(new Font(help.getFont().getName(), help.getFont().getStyle(), 25));
        //Images
        _pokemonBtn = null; _sonicBtn = null; _batmanBtn = null; _avengersBtn = null;
        _pokemonBtn = buildImageBtn(_pokemonPath);
        _batmanBtn = buildImageBtn(_batmanPath);
        _sonicBtn = buildImageBtn(_sonicPath);
        _avengersBtn = buildImageBtn(_avengersPath);
        //Adding Jobjects to content pane
        getContentPane().add(nLbl);
        getContentPane().add(_txtBxN);
        getContentPane().add(help);
        getContentPane().add(_pokemonBtn);
        getContentPane().add(_batmanBtn);
        getContentPane().add(_sonicBtn);
        getContentPane().add(_avengersBtn);
        //Locating nLbl
        layout.putConstraint(SpringLayout.NORTH, nLbl, 20, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.WEST, nLbl, 130, SpringLayout.WEST, getContentPane());
        //Locating txtBxN
        layout.putConstraint(SpringLayout.WEST, _txtBxN, 15, SpringLayout.EAST, nLbl);
        layout.putConstraint(SpringLayout.NORTH, _txtBxN, -15, SpringLayout.NORTH, nLbl);
        //Locating help
        layout.putConstraint(SpringLayout.NORTH, help, 7, SpringLayout.SOUTH, _txtBxN);
        layout.putConstraint(SpringLayout.WEST, help, -100, SpringLayout.WEST, nLbl);
        //Locating images
        layout.putConstraint(SpringLayout.NORTH, _pokemonBtn, 100, SpringLayout.NORTH, help);
        layout.putConstraint(SpringLayout.WEST, _pokemonBtn, 120, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.WEST, _batmanBtn, 80, SpringLayout.EAST, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _batmanBtn, 0, SpringLayout.NORTH, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _sonicBtn, 50, SpringLayout.SOUTH, _pokemonBtn);
        layout.putConstraint(SpringLayout.WEST, _sonicBtn, 0, SpringLayout.WEST, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _avengersBtn, 0, SpringLayout.NORTH, _sonicBtn);
        layout.putConstraint(SpringLayout.WEST, _avengersBtn, 80, SpringLayout.EAST, _sonicBtn);
        this.setSize(800,1200);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (_pokemonBtn.getModel().isArmed()) {
            openPuzzle(getClass().getResource("\\Images\\" + _pokemonPath));
        }
        if (_batmanBtn.getModel().isArmed()) {
            openPuzzle(getClass().getResource("\\Images\\" + _batmanPath));
        }
        if (_sonicBtn.getModel().isArmed()) {
            openPuzzle(getClass().getResource("\\Images\\" + _sonicPath));
        }
        if (_avengersBtn.getModel().isArmed()) {
            openPuzzle(getClass().getResource("\\Images\\" + _avengersPath));
        }
    }

    private JButton buildImageBtn(String imageRelativePath) {
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("\\Images\\" + imageRelativePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(200,200,Image.SCALE_DEFAULT);
        JButton btn = new JButton();
        btn.setIcon(new ImageIcon(img));
        btn.addActionListener(this);
        return btn;
    }

    private void openPuzzle(URL imgPath) {
        int n = 0;
        try {
            n = Integer.parseInt(_txtBxN.getText());
        }
        catch (Exception e) {
            //The value is not legal
        }
        if (n != 0) {
            pzlItself = new PuzzleGame(n, imgPath);
        }
    }
}
