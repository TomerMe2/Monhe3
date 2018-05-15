import jdk.nashorn.internal.scripts.JO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PuzzleGame extends JFrame implements ActionListener, KeyListener{

    private static String _backgroundPath = "Background.jpg";
    private static int _blockMargin = 5;
    private static int _backgrndBorderPixels = 35;
    private static int _backgrndItselfPixels = 402;
    private int _n;
    private int _wholeImgSize;
    private int _singleImgSize;
    private int _padFromBorders;
    private GameBlock[][] _blocks;
    private Stack<Move> _moves;
    private int _moveCounter;
    private JLabel _moveCounterLbl;
    private Image _background;
    private SpringLayout _layout;
    private static final int _SHUFFLE_TIMES = 100;

    public PuzzleGame(int n, URL imagePathURL) {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _layout = new SpringLayout();
        getContentPane().setLayout(_layout);
        _n = n;
        detSize();
        buildBlocks(imagePathURL);
        _background = null;
        try {
            _background = ImageIO.read(getClass().getResource("\\Images\\" + _backgroundPath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        int backgroundSize = (_n)*(_singleImgSize + _blockMargin);
        double backgroundProportion = backgroundSize/_backgrndItselfPixels;
        _padFromBorders = (int) Math.round(_backgrndBorderPixels*backgroundProportion);
        _background = _background.getScaledInstance(backgroundSize + 2*_padFromBorders
                , backgroundSize + 2*_padFromBorders, Image.SCALE_DEFAULT);
        //make the move count label
        _moves = new Stack<>();
        _moveCounter = 0;
        _moveCounterLbl = new JLabel("Moves: " + _moveCounter);
        _moveCounterLbl.setFont(new Font(_moveCounterLbl.getFont().getName(), _moveCounterLbl.getFont().getStyle(), 30));
        _moveCounterLbl.setLocation(0,0);
        //makeButtons();
        shuffle();
        placeButtons();
        placeDirectionsForEmptyImg();
        getContentPane().add(_moveCounterLbl);
        getContentPane().add(new JLabel(new ImageIcon(_background)));
        this.setSize(_background.getWidth(this) + _padFromBorders,
                _background.getHeight(this) + 2*_padFromBorders);
        this.setResizable(false);
        this.setVisible(true);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (_blocks[i][j].getModel().isArmed()) {
                    if (_blocks[i][j].getEmptyImgDirection() != Directions.NONE) {
                        //An image with a blank space near it was clicked
                        //We need to move the image to the blank space
                        movePicture(_blocks[i][j].getEmptyImgDirection(), i, j);
                        if (isThereWin()) {
                            win();
                        }
                    }
                }
            }
        }
    }

    //This method replace the image in i,j with an image in the given direction.
    //ONLY UP OR DOWN
    private void movePictureUpOrDown(Directions dir, int i, int j) {
        int iToReplaceWith;
        GameBlock temp = _blocks[i][j];
        int locX = _blocks[i][j].getX();
        if (dir == Directions.UP) {
            iToReplaceWith = i-1;
        }
        else { //DOWN
            iToReplaceWith = i+1;
        }
        //Replace the blocks
        _blocks[i][j] = _blocks[iToReplaceWith][j];
        _blocks[iToReplaceWith][j] = temp;
        int pad = (iToReplaceWith) * (_singleImgSize + _blockMargin) + _padFromBorders;
        _blocks[iToReplaceWith][j].setLocation(locX, pad);
        _blocks[iToReplaceWith][j].getModel().setArmed(false);
        //now the empty image is in i,j so we will update it's neighbours
        setNeighboursToThisEmptyImage(i,j);
        //now in iToReplaceWith,j there's a not empty image
        setNeighboursToThisNotEmptyImage(iToReplaceWith,j);
    }

    //This method replace the image in i,j with an image in the given direction.
    //ONLY LEFT OR RIGHT
    private void movePictureLeftOrRight(Directions  dir, int i, int j) {
        int jToReplaceWith;
        GameBlock temp = _blocks[i][j];
        int locY = _blocks[i][j].getY();
        if (dir == Directions.LEFT) {
            jToReplaceWith = j-1;
        }
        else { //RIGHT
            jToReplaceWith = j+1;
        }
        _blocks[i][j] = _blocks[i][jToReplaceWith];
        _blocks[i][jToReplaceWith] = temp;
        int pad = (jToReplaceWith) * (_singleImgSize + _blockMargin) + _padFromBorders;
        _blocks[i][jToReplaceWith].setLocation(pad, locY);
        _blocks[i][jToReplaceWith].getModel().setArmed(false);
        //now the empty image is in i,j so we will update it's neighbours
        setNeighboursToThisEmptyImage(i,j);
        //now in i,jToReplaceWith there's a not empty image
        setNeighboursToThisNotEmptyImage(i,jToReplaceWith);
    }

    //This method moves the picture at i,j to the empty location
    private void movePicture(Directions dir, int i, int j) {
        if (dir == Directions.UP) {
            movePictureUpOrDown(Directions.UP, i, j);
        }
        if (dir == Directions.DOWN) {
            movePictureUpOrDown(Directions.DOWN, i, j);
        }
        if (dir == Directions.LEFT) {
            movePictureLeftOrRight(Directions.LEFT, i, j);
        }
        if (dir == Directions.RIGHT) {
            movePictureLeftOrRight(Directions.RIGHT, i, j);
        }
        //Add the move to the moves stack
        _moves.add(new Move(dir, _blocks[i][j]));
        //Count this move
        _moveCounter++;
        //TODO: THIS WONT FKING WORK. TRY MIG LAYOUT MAYBE
        //_moveCounterLbl.setText("Moves: " + _moveCounter);
    }

    //The image at i,j is not empty
    private void setNeighboursToThisNotEmptyImage(int i, int j) {
        if (i-1 >= 0) {
            _blocks[i-1][j].setEmptyImgDir(Directions.NONE);
        }
        if (i+1 < _n) {
            _blocks[i+1][j].setEmptyImgDir(Directions.NONE);
        }
        if (j-1 >= 0) {
            _blocks[i][j-1].setEmptyImgDir(Directions.NONE);
        }
        if (j+1 < _n) {
            _blocks[i][j+1].setEmptyImgDir(Directions.NONE);
        }
    }

    //The empty image is at i,j
    private void setNeighboursToThisEmptyImage(int i, int j) {
        if (i-1 >= 0) {
            _blocks[i-1][j].setEmptyImgDir(Directions.DOWN);
        }
        if (i+1 < _n) {
            _blocks[i+1][j].setEmptyImgDir(Directions.UP);
        }
        if (j-1 >= 0) {
            _blocks[i][j-1].setEmptyImgDir(Directions.RIGHT);
        }
        if (j+1 < _n) {
            _blocks[i][j+1].setEmptyImgDir(Directions.LEFT);
        }
    }

    //This method builds all the blocks in our board
    private void buildBlocks(URL absolutePathURL) {
        _blocks = new GameBlock[_n][_n];
        Image img = null;
        try {
            img = ImageIO.read(absolutePathURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //scale the image to the size that we want
        img = img.getScaledInstance(_wholeImgSize, _wholeImgSize, Image.SCALE_DEFAULT);
        BufferedImage fullBfrd = new BufferedImage(img.getWidth(this), img.getHeight(this),
                BufferedImage.TYPE_INT_ARGB);
        //We must draw the fullBfrd image in order for this to work
        Graphics gr = fullBfrd.getGraphics();
        gr.drawImage(img, 0, 0, this);
        gr.dispose();
        int counter = 0;
        Random rand = new Random();
        int notInGameIndex = rand.nextInt(_n*_n);
        //TODO: MAKE SOMETHING FROM A PIC NOT IN GAME
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                BufferedImage cropped = fullBfrd.getSubimage(j*_singleImgSize, i*_singleImgSize,
                        _singleImgSize, _singleImgSize);
                GameBlock blk = new GameBlock(cropped, counter);
                blk.setFocusable(false);
                blk.addActionListener(this);
                _blocks[i][j] = blk;
                //Pick our missing piece
                if (notInGameIndex == counter) {
                    _blocks[i][j].setIsInGame(false);
                }
                counter++;
            }
        }
    }

    //We want the size closest to prefSize, but divideable by _n
    private void detSize() {
        int prefSize = 600; //This is the preffereable window size
        // find the quotient
        int q = prefSize / _n;

        // 1st possible closest number
        int option1 = _n * q;

        // 2nd possible closest number
        int option2;
        if (prefSize*_n > 0) {
            option2 = _n*(q+1);
        }
        else {
            option2 = _n*(q-1);
        }
        if (Math.abs(prefSize - option1) < Math.abs(prefSize - option2))
            _wholeImgSize = option1;
        else {
            _wholeImgSize = option2;
        }
        _singleImgSize = _wholeImgSize/_n;
    }

    public void placeButtons() {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                //Calculating the pads from north and west
                int padNorth = i * (_singleImgSize + _blockMargin) + _padFromBorders;
                int padWest = j * (_singleImgSize + _blockMargin) + _padFromBorders;
                if (_blocks[i][j].getIsInGame()) {
                    //Place the button
                    getContentPane().add(_blocks[i][j]);
                    _layout.putConstraint(SpringLayout.NORTH, _blocks[i][j], padNorth, SpringLayout.NORTH, getContentPane());
                    _layout.putConstraint(SpringLayout.WEST, _blocks[i][j], padWest, SpringLayout.WEST, getContentPane());
                }
            }
        }
    }

    //Find the empty image and set the directions for it's neighbours
    public void placeDirectionsForEmptyImg() {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (!_blocks[i][j].getIsInGame()) {
                    setNeighboursToThisEmptyImage(i,j);
                }
            }
        }
    }


    //This method will shuffle our board
    private void shuffle() {
        Random rnd = new Random();
        int x1, x2, y1, y2;
        GameBlock temp;
        for (int i=0; i<_n*_SHUFFLE_TIMES; i++) {
            x1 = rnd.nextInt(_n);
            x2 = rnd.nextInt(_n);
            y1 = rnd.nextInt(_n);
            y2 = rnd.nextInt(_n);
            temp = _blocks[x1][y1];
            _blocks[x1][y1] = _blocks[x2][y2];
            _blocks[x2][y2] = temp;
        }
    }

    //This method checks if all the images are in the correct order
    private boolean isThereWin() {
        int prev = -1;
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (prev != -1) {  //If it's not the first block
                    if (prev + 1 != _blocks[i][j].getIndex()) {
                        return false;
                    }
                }
                prev = _blocks[i][j].getIndex();
            }
        }
        return true;
    }

    //This method will pop an announcment about the winning
    private void win() {
        JOptionPane.showMessageDialog(this, "WOW! YOU WON!");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Do Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Directions dir = null;
        if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
            dir = Directions.RIGHT;
        }
        else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
            dir = Directions.LEFT;
        }
        else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
            dir = Directions.DOWN;
        }
        else if(e.getKeyCode()== KeyEvent.VK_UP) {
            dir = Directions.UP;
        }
        moveBlockWithEmptyDir(dir);
    }

    //If such block with such empty image dir exists, this method will move him to the empty dir
    public void moveBlockWithEmptyDir(Directions dir) {
        //TODO: FIX WHOLE LINE IS MOVING INSTEAD OF ONE
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (_blocks[i][j].getEmptyImgDirection() == dir) {
                    movePicture(dir, i, j);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do Nothing
    }
}
