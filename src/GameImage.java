import java.awt.image.BufferedImage;

public class GameImage extends BufferedImage {
    boolean _isInGame;
    int _index;

    public GameImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public int getIndex() {
        return _index;
    }

    public boolean getIsInGame() {
        return _isInGame;
    }

    public void setIndex (int index) {
        _index = index;
    }

    public void setIsInGame (boolean isInGame) {
        _isInGame = isInGame;
    }
}
