import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ChooseGame extends JFrame implements ActionListener {

    public ChooseGame() {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        getContentPane().setLayout(layout);
        //N Label
        JLabel nLbl = new JLabel("Choose your board size!");
        nLbl.setFont(new Font(nLbl.getFont().getName(), nLbl.getFont().getStyle(), 30));
        //Text Field for N
        JTextField txtBxN = new JTextField(3);
        txtBxN.setPreferredSize(new Dimension(40,70));
        txtBxN.setFont(new Font(txtBxN.getFont().getName(), txtBxN.getFont().getStyle(), 30));
        //help Label
        JLabel help = new JLabel("for example: if you want a board with the size 4x4 insert 4");
        help.setFont(new Font(help.getFont().getName(), help.getFont().getStyle(), 25));
        //Images
        JButton pokemonBtn = null, sonicBtn = null, batmanBtn = null, avengersBtn = null;
        pokemonBtn = buildImageBtn("Pokemon.png");
        batmanBtn = buildImageBtn("Batman.jpg");
        sonicBtn = buildImageBtn("Sonic.jpg");
        avengersBtn = buildImageBtn("Avengers.jpeg");
        //Adding Jobjects to content pane
        getContentPane().add(nLbl);
        getContentPane().add(txtBxN);
        getContentPane().add(help);
        getContentPane().add(pokemonBtn);
        getContentPane().add(batmanBtn);
        getContentPane().add(sonicBtn);
        getContentPane().add(avengersBtn);

        //Locating nLbl
        layout.putConstraint(SpringLayout.NORTH, nLbl, 20, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.WEST, nLbl, 130, SpringLayout.WEST, getContentPane());
        //Locating txtBxN
        layout.putConstraint(SpringLayout.WEST, txtBxN, 15, SpringLayout.EAST, nLbl);
        layout.putConstraint(SpringLayout.NORTH, txtBxN, -15, SpringLayout.NORTH, nLbl);
        //Locating help
        layout.putConstraint(SpringLayout.NORTH, help, 7, SpringLayout.SOUTH, txtBxN);
        layout.putConstraint(SpringLayout.WEST, help, -100, SpringLayout.WEST, nLbl);
        //Locating images
        layout.putConstraint(SpringLayout.NORTH, pokemonBtn, 100, SpringLayout.NORTH, help);
        layout.putConstraint(SpringLayout.WEST, pokemonBtn, 120, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.WEST, batmanBtn, 80, SpringLayout.EAST, pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, batmanBtn, 0, SpringLayout.NORTH, pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, sonicBtn, 50, SpringLayout.SOUTH, pokemonBtn);
        layout.putConstraint(SpringLayout.WEST, sonicBtn, 0, SpringLayout.WEST, pokemonBtn);
        layout.putConstraint(SpringLayout.NORTH, avengersBtn, 0, SpringLayout.NORTH, sonicBtn);
        layout.putConstraint(SpringLayout.WEST, avengersBtn, 80, SpringLayout.EAST, sonicBtn);
        this.setSize(800,1200);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

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
}
