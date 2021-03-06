import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameBlock extends JButton {
    private BufferedImage _bfrdImg;
    private boolean _isInGame;
    private int _index;
    private Directions _emptyImgDir;

    public GameBlock(BufferedImage bfrdImg, int index) {
        super();
        _bfrdImg = bfrdImg;
        _index = index;
        _isInGame = true;
        _emptyImgDir = Directions.NONE;
        setIcon(new ImageIcon(bfrdImg));
        makeButtonCoolLooking();
    }

    public int getIndex() {
        return _index;
    }

    public boolean getIsInGame() {
        return _isInGame;
    }

    public Directions getEmptyImgDirection() {
        return _emptyImgDir;
    }

    public void setIsInGame (boolean isInGame) {
        _isInGame = isInGame;
    }

    public void setEmptyImgDir(Directions dir) {
        _emptyImgDir = dir;
    }

    //Makes the button look like an image
    private void makeButtonCoolLooking() {
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }
}
