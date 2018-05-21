import javax.swing.*;
import java.awt.*;

public class RadioGameButton extends JRadioButton {

    public RadioGameButton(String text) {
        super(text);
        makeStyle();
    }

    private void makeStyle() {
        setFont(new Font(getFont().getName(), getFont().getStyle(), 28));
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setOpaque(false);
    }
}
