import javax.swing.*;
import java.awt.*;

public class MenuBtn extends JButton {

    public MenuBtn(String text) {
        super(text);
        makeStyle();
    }

    private void makeStyle() {
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setOpaque(false);
        setFont(new Font(getFont().getName(), getFont().getStyle(), 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(
                new Point(0, 0),
                Color.WHITE,
                new Point(0, getHeight()),
                Color.ORANGE.darker()));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
