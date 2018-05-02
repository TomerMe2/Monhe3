import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PuzzleGame extends JFrame implements ActionListener{

    private int _n;
    private int _imgSize;
    private GameImage[][] _images;

    public PuzzleGame(int n, URL imagePathURL) {
        super("Puzzelito");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _n = n;
        detSize();
        cropImage(imagePathURL);
        this.setSize((int)(_imgSize*1.15),(int)(_imgSize*1.15));
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    }

    private void cropImage(URL absolutePathURL) {
        _images = new GameImage[_n][_n];
        Image img = null;
        try {
            img = ImageIO.read(absolutePathURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = img.getScaledInstance(_imgSize,_imgSize,Image.SCALE_DEFAULT);
        BufferedImage bfrdImg = (BufferedImage) img;
        int rectSize = _imgSize / _n;   //It will divide correctly cus we made imageSize that way
        int counter = 0;
        //TODO: MAKE SOMETHING NOT IN GAME
        for (int i=0; i<_n; i++) {
            for (int j=0; j<_n; j++) {
                BufferedImage cropped = bfrdImg.getSubimage(i*rectSize, j*rectSize, rectSize, rectSize);
                _images[i][j] = (GameImage)cropped;
                _images[i][j].setIndex(counter);
                _images[i][j].setIsInGame(true);
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
            _imgSize = option1;
        else {
            _imgSize = option2;
        }
    }
}
