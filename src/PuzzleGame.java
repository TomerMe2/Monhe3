import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;

import static java.lang.System.exit;

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
    private MigLayout _layout;
    private Timer _timr;
    private JLabel _timerLbl;
    private static final int _SHUFFLE_TIMES = 100;
    private int _seconds;
    private int _minutes;
    private boolean _hasWon;

    public PuzzleGame(int n, String imagePath, int[][] blocksPermutation) {
        super("Puzzelito");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _layout = new MigLayout();
        getContentPane().setLayout(_layout);
        _n = n;
        detSize();
        boolean isRandomized = blocksPermutation == null;
        buildBlocks(imagePath, isRandomized);
        Image background = getBkrngImg();
        if (background == null) {
            exit(1);
        }
        _moves = new Stack<>();
        _moveCounter = 0;
        _moveCounterLbl = new JLabel("Moves: " + _moveCounter);
        _moveCounterLbl.setFont(new Font(_moveCounterLbl.getFont().getName(), _moveCounterLbl.getFont().getStyle(), 30));
        if (blocksPermutation == null) {
            shuffle();
        }
        else {
           handleCSVBoard(blocksPermutation);
        }
        placeButtons();
        placeDirectionsForEmptyImg();
        //Small Buttons
        addSmallBtn("Undo", 310, 0);
        addSmallBtn("Main Menu", 160, 0);
        addSmallBtn("Choose Menu", 390, 0);
        timerOperation();
        _timr.start();
        add(_moveCounterLbl, "pos 0px 0px");
        add(new JLabel(new ImageIcon(background)), "pos 0px 0px");
        this.setSize((int) (background.getWidth(this) + 0.25*_padFromBorders),
                (int) (background.getHeight(this) + 1.5*_padFromBorders));
        this.setResizable(false);
        this.setVisible(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        _hasWon = false;
        if (isThereWin()) {   //because it's can be ranodimzed, there's a chance for a win
            win();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!_hasWon) {
            for (int i = 0; i < _n; i++) {
                for (int j = 0; j < _n; j++) {
                    if (_blocks[i][j].getModel().isArmed()) {
                        if (_blocks[i][j].getEmptyImgDirection() != Directions.NONE) {
                            //An image with a blank space near it was clicked
                            //We need to move the image to the blank space
                            movePicture(_blocks[i][j].getEmptyImgDirection(), i, j, false);
                            if (isThereWin()) {
                                win();
                            }
                        }
                    }
                }
            }
            if (e.getActionCommand().equals("Undo")) {
                if (!_moves.isEmpty()) {
                    Move last = _moves.pop();
                    moveBlockBySpecificBlock(last.getOpossiteDir(), last.getBlock(), true);
                }
            }
        }
        if (e.getActionCommand().equals("Choose Menu")) {
            new ChooseGame();
            dispose();
        }
        if (e.getActionCommand().equals("Main Menu")) {
            new MainMenu();
            dispose();
        }
    }

    private void timerOperation() {
        //Create the Label
        _timerLbl = new JLabel("0:0");
        _timerLbl.setFont(new Font(_timerLbl.getFont().getName(), _timerLbl.getFont().getStyle(), 30));
        add(_timerLbl, "pos 600px 0px");
        _seconds = 0;
        _minutes = 0;
        //Timer itself
        _timr = new Timer(1000, e -> {
            _seconds++;
            if (_seconds == 60) {
                _seconds = 0;
                _minutes++;
            }
            _timerLbl.setText(_minutes + ":" + _seconds);
        });
    }

    private void handleCSVBoard(int[][] locations) {
        GameBlock[][] tempBlocks = new GameBlock[_n][_n];
        int iEmpty = 0, jEmpty = 0;
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (locations[i][j] != 0) {
                    tempBlocks[i][j] = findBlockGivenIndex(locations[i][j]);
                }
                else {
                    //save the empty locs
                    iEmpty = i;
                    jEmpty = j;
                }
            }
        }
        //After we located everything, we will locate the empty block
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (!isBlockInArr(_blocks[i][j], tempBlocks)) {
                    tempBlocks[iEmpty][jEmpty] = _blocks[i][j];
                    _blocks[i][j].setIsInGame(false);
                }
            }
        }
        _blocks = tempBlocks;
    }

    private boolean isBlockInArr(GameBlock blk, GameBlock[][] arr) {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (arr[i][j] != null && arr[i][j].equals(blk)) {
                    return true;
                }
            }
        }
        return false;
    }
    private GameBlock findBlockGivenIndex(int index) {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (_blocks[i][j].getIndex() == index) {
                    return _blocks[i][j];
                }
            }
        }
        return null;
    }

    private Image getBkrngImg() {
        Image toReturn;
        try {
            toReturn = ImageIO.read(new File("Images/" + _backgroundPath));

        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            exit(1);
            return null; //will never execute
        }
        int backgroundSize = (_n)*(_singleImgSize + _blockMargin);
        double backgroundProportion = backgroundSize/_backgrndItselfPixels;
        _padFromBorders = (int) Math.round(_backgrndBorderPixels*backgroundProportion);
        toReturn = toReturn.getScaledInstance(backgroundSize + 2*_padFromBorders
                , backgroundSize + 2*_padFromBorders, Image.SCALE_DEFAULT);
        return toReturn;
    }

    private void addSmallBtn(String text, int posX, int posY) {
        MenuBtn btn = new MenuBtn(text);
        btn.setFont(new Font(btn.getFont().getName(), btn.getFont().getStyle(), 25));
        btn.setFocusable(false);
        btn.addActionListener(this);
        add(btn, "pos " + posX + "px " + posY + "px");
    }

    private void moveBlockBySpecificBlock(Directions dir, GameBlock blk, boolean isUndo) {
        boolean isDone = false;
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (_blocks[i][j].equals(blk)) {
                    if (!isDone) {
                        movePicture(dir, i, j, isUndo);
                        isDone = true;
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
        _layout.setComponentConstraints(_blocks[iToReplaceWith][j], "pos " + locX + "px " + pad + "px");
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
       // _blocks[i][jToReplaceWith].setLocation(pad, locY);
        _layout.setComponentConstraints(_blocks[i][jToReplaceWith], "pos " + pad + "px " + locY + "px");
        _blocks[i][jToReplaceWith].getModel().setArmed(false);
        //now the empty image is in i,j so we will update it's neighbours
        setNeighboursToThisEmptyImage(i,j);
        //now in i,jToReplaceWith there's a not empty image
        setNeighboursToThisNotEmptyImage(i,jToReplaceWith);
    }

    //This method moves the picture at i,j to the empty location
    private void movePicture(Directions dir, int i, int j, boolean isUndo) {
        //Count this move
        if (!isUndo) {
            _moveCounter++;
            _moveCounterLbl.setText("Moves: " + _moveCounter);
            //Add this move to the stack
            _moves.push(new Move(dir, _blocks[i][j]));
        }
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
    private void buildBlocks(String imgPath, boolean isRandomized) {
        _blocks = new GameBlock[_n][_n];
        Image img = null;
        try {
            img = ImageIO.read(new File(imgPath));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The program has encountered a critical problem " +
                    "and will shut down");
            exit(1);
        }
        //scale the image to the size that we want
        img = img.getScaledInstance(_wholeImgSize, _wholeImgSize, Image.SCALE_DEFAULT);
        BufferedImage fullBfrd = new BufferedImage(img.getWidth(this), img.getHeight(this),
                BufferedImage.TYPE_INT_ARGB);
        //We must draw the fullBfrd image in order for this to work
        Graphics gr = fullBfrd.getGraphics();
        gr.drawImage(img, 0, 0, this);
        gr.dispose();
        int counter = 1;
        Random rand = new Random();
        int notInGameIndex = rand.nextInt(_n*_n);
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                BufferedImage cropped = fullBfrd.getSubimage(j*_singleImgSize, i*_singleImgSize,
                        _singleImgSize, _singleImgSize);
                GameBlock blk = new GameBlock(cropped, counter);
                blk.setFocusable(false);
                blk.addActionListener(this);
                _blocks[i][j] = blk;
                //Pick our missing piece
                if (isRandomized && notInGameIndex == counter) {
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

    private void placeButtons() {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                //Calculating the pads from north and west
                int padNorth = i * (_singleImgSize + _blockMargin) + _padFromBorders;
                int padWest = j * (_singleImgSize + _blockMargin) + _padFromBorders;
                if (_blocks[i][j].getIsInGame()) {
                    //Place the button
                    add(_blocks[i][j], "pos " + padWest + "px " + padNorth + "px");
                }
            }
        }
    }

    private void placeNotInGameImage() {
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                //Calculating the pads from north and west
                int padNorth = i * (_singleImgSize + _blockMargin) + _padFromBorders;
                int padWest = j * (_singleImgSize + _blockMargin) + _padFromBorders;
                if (!_blocks[i][j].getIsInGame()) {
                    //Place the button
                    add(_blocks[i][j], "pos " + padWest + "px " + padNorth + "px");
                    return;
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

    //This method will pop an announcment about the winning and handle winning things
    private void win() {
        _timr.stop();
        _hasWon = true;
        placeNotInGameImage();
        JOptionPane.showMessageDialog(this, "WOW! YOU WON! \n" +
            "It took you " + _minutes + " minutes, " + _seconds + " seconds " +
            "and " + _moveCounter + " moves!");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Do Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (_hasWon) {
            return;
        }
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
        else if (e.getKeyCode() == KeyEvent.VK_Z &&
                (e.getModifiers() & KeyEvent.CTRL_MASK) != 0){ //if UNDO
            if (!_moves.isEmpty()) {
                Move last = _moves.pop();
                moveBlockBySpecificBlock(last.getOpossiteDir(), last.getBlock(), true);
            }
        }
        moveBlockWithEmptyDir(dir);
        if (isThereWin()) {
            win();
        }
    }

    //If such block with such empty image dir exists, this method will move him to the empty dir
    private void moveBlockWithEmptyDir(Directions dir) {
        boolean isDone = false;
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                if (_blocks[i][j].getEmptyImgDirection() == dir && !isDone) {
                    isDone = true;
                    movePicture(dir, i, j, false);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do Nothing
    }
}
