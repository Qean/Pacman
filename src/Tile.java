import java.awt.*;

/**
 * Klasa płytek
 * Mapa gry jest podzielona na płtyki, które są potem dzielone na ściany, punkty i owoce
 */
class Tile {
    boolean wall = false;
    boolean dot = false;
    boolean bigDot = false;
    boolean eaten = false;
    boolean collision = false;
    boolean junction = false;

    private int x, y, width, height;
    private int[] leftupperCorner, rightupperCorner, leftdownCorner, rightdownCorner;

    Tile(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        leftupperCorner = new int[]{x, y};
        rightupperCorner = new int[]{x + width, y};
        leftdownCorner = new int[]{x, y + height};
        rightdownCorner = new int[]{x + +width, y + height};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getLeftupperCorner() {
        return leftupperCorner;
    }

    public int[] getRightupperCorner() {
        return rightupperCorner;
    }

    public int[] getLeftdownCorner() {
        return leftdownCorner;
    }

    public int[] getRightdownCorner() {
        return rightdownCorner;
    }

    /**Klasa odpowiedzialna za rysowanie punktów i ścian
     * @param g2d Grafika 2D
     */
    public void show(Graphics2D g2d) {
        if (dot) {
            if (!eaten) {
                g2d.setColor(Color.white);
                if (collision)
                    g2d.setColor(Color.green);
                g2d.fillOval(x + 6, y + 5, width - 18, height - 15);
            }
        } else if (bigDot) {
            if (!eaten) {
                g2d.setColor(Color.red);
                if (collision)
                    g2d.setColor(Color.green);
                g2d.fillOval(x + 1, y, width - 13, height - 10);
            }
        } else if (wall) {
            g2d.setColor(Color.blue);
            if (collision)
                g2d.setColor(Color.green);
            g2d.fillRect(x, y, width, height);
        }
    }
}