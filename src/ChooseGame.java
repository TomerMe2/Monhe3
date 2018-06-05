import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ChooseGame extends JFrame implements ActionListener {

    private JButton _pokemonBtn, _batmanBtn, _sonicBtn, _avengersBtn;
    private static String _pokemonPath = "Pokemon.png",
            _batmanPath = "Batman.jpg",
            _sonicPath = "Sonic.jpg",
            _avengersPath = "Avengers.jpeg",
            _backgroundPath = "Background.jpg";
    private JTextField _txtBxN;
    private RadioGameButton _rdioRandom;
    private RadioGameButton _rdioCSV;

    public ChooseGame() {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);
        //N Label
        JLabel nLbl = new JLabel("Choose your board size!");
        nLbl.setFont(new Font(nLbl.getFont().getName(), nLbl.getFont().getStyle(), 30));
        //Text Field for N
        _txtBxN = new JTextField(3);
        _txtBxN.setPreferredSize(new Dimension(40,60));
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
        //Browse btn
        MenuBtn browse = new MenuBtn("Browse for your own images");
        browse.addActionListener(this);
        //Radio buttons
        _rdioRandom = new RadioGameButton("Random Puzzle");
        _rdioRandom.setSelected(true);
        _rdioCSV = new RadioGameButton("Puzzle from CSV");
        ButtonGroup grp = new ButtonGroup();
        grp.add(_rdioCSV);
        grp.add(_rdioRandom);
        //Load the background
        Image bkrnd = getBkrng();
        //Adding Jobjects to content pane
        getContentPane().add(nLbl);
        getContentPane().add(_txtBxN);
        getContentPane().add(help);
        getContentPane().add(_pokemonBtn);
        getContentPane().add(_batmanBtn);
        getContentPane().add(_sonicBtn);
        getContentPane().add(_avengersBtn);
        getContentPane().add(browse);
        getContentPane().add(_rdioCSV);
        getContentPane().add(_rdioRandom);
        getContentPane().add(new JLabel(new ImageIcon(bkrnd)));
        //Locating nLbl
        layout.putConstraint(SpringLayout.NORTH, nLbl, 20, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.WEST, nLbl, 130, SpringLayout.WEST, getContentPane());
        //Locating txtBxN
        layout.putConstraint(SpringLayout.WEST, _txtBxN, 15, SpringLayout.EAST, nLbl);
        layout.putConstraint(SpringLayout.NORTH, _txtBxN, -15, SpringLayout.NORTH, nLbl);
        //Locating help
        layout.putConstraint(SpringLayout.NORTH, help, 7, SpringLayout.SOUTH, _txtBxN);
        layout.putConstraint(SpringLayout.WEST, help, -75, SpringLayout.WEST, nLbl);
        //Locating Radio Buttons
        layout.putConstraint(SpringLayout.NORTH, _rdioRandom, 10, SpringLayout.SOUTH, help);
        layout.putConstraint(SpringLayout.WEST, _rdioRandom, 85, SpringLayout.WEST, help);
        layout.putConstraint(SpringLayout.NORTH, _rdioCSV, 0, SpringLayout.NORTH, _rdioRandom);
        layout.putConstraint(SpringLayout.WEST, _rdioCSV, 20, SpringLayout.EAST, _rdioRandom);
        //Locating images
        layout.putConstraint(SpringLayout.NORTH, _pokemonBtn, 70, SpringLayout.NORTH, _rdioRandom);
        layout.putConstraint(SpringLayout.WEST, _pokemonBtn, 120, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.WEST, _batmanBtn, 80, SpringLayout.EAST, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _batmanBtn, 0, SpringLayout.NORTH, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _sonicBtn, 50, SpringLayout.SOUTH, _pokemonBtn);
        layout.putConstraint(SpringLayout.WEST, _sonicBtn, 0, SpringLayout.WEST, _pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, _avengersBtn, 0, SpringLayout.NORTH, _sonicBtn);
        layout.putConstraint(SpringLayout.WEST, _avengersBtn, 80, SpringLayout.EAST, _sonicBtn);
        //Locating browse
        layout.putConstraint(SpringLayout.NORTH, browse, 15, SpringLayout.SOUTH, _sonicBtn);
        layout.putConstraint(SpringLayout.WEST, browse, 70, SpringLayout.WEST, _sonicBtn);
        this.setSize(800,830);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (_pokemonBtn.getModel().isArmed()) {
            openPuzzle("Images/" + _pokemonPath);
        }
        if (_batmanBtn.getModel().isArmed()) {
            openPuzzle("Images/" + _batmanPath);
        }
        if (_sonicBtn.getModel().isArmed()) {
            openPuzzle("Images/" + _sonicPath);
        }
        if (_avengersBtn.getModel().isArmed()) {
            openPuzzle("Images/" + _avengersPath);
        }
        if (e.getActionCommand() == "Browse for your own images") {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(this);
            if (chooser.getSelectedFile() != null) {
                try {
                    openPuzzle(chooser.getSelectedFile().getPath());
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, "There's a problem with your image, " +
                            "please try again");
                }
            }
        }
    }
    private Image getBkrng() {
        Image bkrnd = null;
        try {
            bkrnd = ImageIO.read(new File("Images/" + _backgroundPath));

        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            System.exit(1);
            return null; //Will never execute
        }
        bkrnd = bkrnd.getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        return bkrnd;
    }
    private JButton buildImageBtn(String imageRelativePath) {
        Image img = null;
        try {
            img = ImageIO.read(new File("Images/" + imageRelativePath));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            System.exit(1);
        }
        img = img.getScaledInstance(200,200,Image.SCALE_DEFAULT);
        JButton btn = new JButton();
        btn.setIcon(new ImageIcon(img));
        btn.addActionListener(this);
        return btn;
    }

    private void openPuzzle(String imgPath) {
        int n = 0;
        try {
            n = Integer.parseInt(_txtBxN.getText());
            if (n==0 | n==1) {
                throw new Exception();
            }
            if (_rdioCSV.isSelected()) {  //Game based on CSV
                int[][] locs = handleCSVOperation();
                if (locs != null) {
                    new PuzzleGame(n, imgPath, locs);
                }
            }
            else { //Randomized game
                new PuzzleGame(n, imgPath, null);
            }
            dispose();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "You didn't insert a number, or it's not a legal number!");
        }
    }

    private int[][] handleCSVOperation() {
        int[][] locations = null;
        try {
            locations = CSVReader.getPermutation(Integer.parseInt(_txtBxN.getText()));
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            System.exit(1);
            return null; //Will never execute
        }
        if (locations == null) {
            JOptionPane.showMessageDialog(this, "There's no such board the the CSV file. Try Again!");
            return null; //We will stop this method
        }
        return locations;
    }
}
