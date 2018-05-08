import java.awt.image.BufferedImage;

public class GameImage {
    BufferedImage _bfrdImg;
    boolean _isInGame;
    int _index;
    Directions _emptyImgDir;
    public GameImage(BufferedImage bfrdImg, int index, Directions emptyImgDirection) {
        _bfrdImg = bfrdImg;
        _index = index;
        _emptyImgDir = emptyImgDirection;
    }

    public int getIndex() {
        return _index;
    }

    public boolean getIsInGame() {
        return _isInGame;
    }

    public BufferedImage getBfrdImg() {
        return _bfrdImg;
    }

    public Directions getEmptyImgDirection() {
        return _emptyImgDir;
    }

    public void setIndex (int index) {
        _index = index;
    }

    public void setIsInGame (boolean isInGame) {
        _isInGame = isInGame;
    }

    public void setEmptyImgDir(Directions dir) {
        _emptyImgDir = dir;
    }
}
