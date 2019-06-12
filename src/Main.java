import javax.swing.*;
import java.awt.*;

/**
 * Główna klasa, która inicjalizuje UI i klasę odpowiedzialną za gre
 */
public class Main extends JFrame {

    public Main() {

        initUI();
    }

    private void initUI() {
        add(new Board(700, 750));

        setTitle("Main");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(930, 930);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}