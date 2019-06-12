import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Klasa ducha, odpowiada za jego logikę, kolizję oraz rysowanie
 */
public class Blinky {
    private int x, y, move, speed, correction, pacmanX, pacmanY, junctionControl;
    private int[] leftupperCorner, rightupperCorner, leftdownCorner, rightdownCorner, center;
    boolean collisionFlag, scaredFlag, junctionFound;
    Random random;

    private BufferedImage blinky;
    private BufferedImage scared;

    /**
     * Konstruktor klasy
     *
     * @param x
     * @param y
     */
    public Blinky(int x, int y) {
        try {
            blinky = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\Blinky.gif"));
            scared = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\GhostScared.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x++;
        y++;
        this.x = x;
        this.y = y;
        this.speed = 4;
        correction = speed / 2;
        leftupperCorner = new int[]{x + correction, y + correction};
        rightupperCorner = new int[]{x - correction + blinky.getWidth(), y + correction};
        leftdownCorner = new int[]{x + correction, y - correction + blinky.getHeight()};
        rightdownCorner = new int[]{x - correction + blinky.getWidth(), y - correction + blinky.getHeight()};
        center = new int[]{(rightupperCorner[0] + leftupperCorner[0]) / 2, (leftdownCorner[1] + leftupperCorner[1]) / 2};
        move = 1;
        scaredFlag = false;
        random = new Random();
        collisionFlag = true;
    }

    /**
     * Metoda odpowiedzialna za przesuwanie i logikę ducha
     * Ruchy ducha są pseudolosowe, ponieważ preferuje ruchy, które zbliżą go do pacmana
     *
     * @param tiles  Obiekt płytek
     * @param pacman Obiekt pacmana
     * @param game   Główny obiekt gry
     */
    public void move(Tile[][] tiles, Pacman pacman, Board game) {
        int i = 0, j = 0;
        pacmanX = pacman.getX();
        pacmanY = pacman.getY();
        if (center[0] >= pacman.getLeftupperCorner()[0] - correction && center[0] <= pacman.getRightupperCorner()[0] + correction && center[1] >= pacman.getLeftupperCorner()[1] - correction && center[1] <= pacman.getLeftdownCorner()[1] + correction) {
            pacman.setLives(pacman.getLives() - 1);
            game.death();
        }
        switch (move) {
            case 1:
                for (Tile[] row : tiles) {
                    if (junctionFound) {
                        break;
                    }
                    for (Tile tile : row) {
                        if (leftupperCorner[0] - speed <= tile.getRightdownCorner()[0] && leftupperCorner[0] - speed > tile.getLeftdownCorner()[0] && leftupperCorner[1] < tile.getRightdownCorner()[1] && leftupperCorner[1] > tile.getRightupperCorner()[1] && tile.wall
                                || leftdownCorner[0] - speed <= tile.getRightupperCorner()[0] && leftdownCorner[0] - speed > tile.getLeftupperCorner()[0] && leftdownCorner[1] > tile.getRightupperCorner()[1] && leftdownCorner[1] < tile.getRightdownCorner()[1] && tile.wall) {
                            i = i + 1;
                            while (move == 2 || move == 1) {
                                move = 1 + random.nextInt(4);
                                if (move == 3 && tiles[i][j - 1].wall) {
                                    move = 2;
                                } else if (move == 4 && tiles[i][j + 1].wall) {
                                    move = 2;
                                }
                            }
                            return;
                        }
                        if (center[0] >= tile.getLeftupperCorner()[0] && center[0] <= tile.getRightupperCorner()[0] && center[1] >= tile.getLeftupperCorner()[1] && center[1] <= tile.getLeftdownCorner()[1] && tile.junction) {
                            move = 2;
                            while (move == 2) {
                                move = 1 + random.nextInt(4);
                                if (move == 1 && tiles[i - 1][j].wall)
                                    move = 2;
                                else if (move == 3 && tiles[i][j - 1].wall || (pacmanY > y && move == 3))
                                    move = 2;
                                else if (move == 4 && tiles[i][j + 1].wall || (pacmanY < y && move == 4))
                                    move = 2;
                            }
                            junctionFound = true;
                            break;
                        }
                        j++;
                    }
                    j = 0;
                    i++;
                }
                x -= speed;
                break;
            case 2:
                for (Tile[] row : tiles) {
                    if (junctionFound) {
                        break;
                    }
                    for (Tile tile : row) {
                        if (rightupperCorner[0] + speed >= tile.getLeftdownCorner()[0] && rightupperCorner[0] + speed < tile.getRightdownCorner()[0] && rightupperCorner[1] < tile.getLeftdownCorner()[1] && rightupperCorner[1] > tile.getLeftupperCorner()[1] && tile.wall
                                || rightdownCorner[0] + speed >= tile.getLeftupperCorner()[0] && rightdownCorner[0] + speed < tile.getRightupperCorner()[0] && rightdownCorner[1] > tile.getLeftupperCorner()[1] && rightdownCorner[1] < tile.getLeftdownCorner()[1] && tile.wall) {
                            i = i - 1;
                            while (move == 2 || move == 1) {
                                move = 1 + random.nextInt(4);
                                if (move == 3 && tiles[i][j - 1].wall) {
                                    move = 2;
                                } else if (move == 4 && tiles[i][j + 1].wall) {
                                    move = 2;
                                }
                            }
                            return;
                        }
                        if (center[0] - 12 >= tile.getLeftupperCorner()[0] && center[0] - 12 <= tile.getRightupperCorner()[0] && center[1] >= tile.getLeftupperCorner()[1] && center[1] <= tile.getLeftdownCorner()[1] && tile.junction) {
                            move = 1;
                            while (move == 1) {
                                move = 1 + random.nextInt(4);
                                if (move == 2 && tiles[i + 1][j].wall)
                                    move = 1;
                                else if (move == 3 && tiles[i][j - 1].wall || (pacmanY > y && move == 3))
                                    move = 1;
                                else if (move == 4 && tiles[i][j + 1].wall || (pacmanY < y && move == 4))
                                    move = 1;
                            }
                            junctionFound = true;
                            break;
                        }
                        j++;
                    }
                    j = 0;
                    i++;
                }
                x += speed;
                break;
            case 3:
                for (Tile[] row : tiles) {
                    if (junctionFound) {
                        break;
                    }
                    for (Tile tile : row) {
                        if (leftupperCorner[1] - speed <= tile.getRightdownCorner()[1] && leftupperCorner[1] - speed > tile.getRightupperCorner()[1] && leftupperCorner[0] < tile.getRightdownCorner()[0] && leftupperCorner[0] >= tile.getLeftdownCorner()[0] && tile.wall
                                || rightupperCorner[1] - speed <= tile.getLeftdownCorner()[1] && rightupperCorner[1] - speed > tile.getLeftupperCorner()[1] && rightupperCorner[0] > tile.getLeftdownCorner()[0] && rightupperCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                            j = j + 1;
                            while (move == 3 || move == 4) {
                                move = 1 + random.nextInt(4);
                                if (move == 1 && tiles[i - 1][j].wall) {
                                    move = 3;
                                } else if (move == 2 && tiles[i + 1][j].wall) {
                                    move = 3;
                                }
                            }
                            return;
                        }
                        if (center[0] >= tile.getLeftupperCorner()[0] && center[0] <= tile.getRightupperCorner()[0] && center[1] >= tile.getLeftupperCorner()[1] && center[1] <= tile.getLeftdownCorner()[1] && tile.junction) {
                            move = 4;
                            while (move == 4) {
                                move = 1 + random.nextInt(4);
                                if (move == 1 && tiles[i - 1][j].wall || (pacmanX > x && move == 1))
                                    move = 4;
                                else if (move == 2 && tiles[i + 1][j].wall || (pacmanX < x && move == 2))
                                    move = 4;
                                else if (move == 3 && tiles[i][j - 1].wall)
                                    move = 4;
                            }
                            junctionFound = true;
                            break;
                        }
                        j++;
                    }
                    j = 0;
                    i++;
                }
                y -= speed;
                break;
            case 4:
                for (Tile[] row : tiles) {
                    if (junctionFound) {
                        break;
                    }
                    for (Tile tile : row) {
                        if (leftdownCorner[1] + speed >= tile.getRightupperCorner()[1] && leftdownCorner[1] + speed < tile.getRightdownCorner()[1] && leftdownCorner[0] <= tile.getRightupperCorner()[0] && leftdownCorner[0] >= tile.getLeftupperCorner()[0] && tile.wall
                                || rightdownCorner[1] + speed >= tile.getLeftupperCorner()[1] && rightdownCorner[1] + speed < tile.getLeftdownCorner()[1] && rightdownCorner[0] >= tile.getLeftupperCorner()[0] && rightdownCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                            j = j - 1;
                            while (move == 3 || move == 4) {
                                move = 1 + random.nextInt(4);
                                if (move == 1 && tiles[i - 1][j].wall) {
                                    move = 3;
                                } else if (move == 2 && tiles[i + 1][j].wall) {
                                    move = 3;
                                }
                            }
                            return;
                        }
                        if (center[0] >= tile.getLeftupperCorner()[0] && center[0] <= tile.getRightupperCorner()[0] && center[1] - 12 >= tile.getLeftupperCorner()[1] && center[1] - 12 < tile.getLeftdownCorner()[1] && tile.junction) {
                            move = 3;
                            while (move == 3) {
                                move = 1 + random.nextInt(4);
                                if (move == 1 && tiles[i - 1][j].wall || (pacmanX > x && move == 1))
                                    move = 3;
                                else if (move == 2 && tiles[i + 1][j].wall || (pacmanX < x && move == 2))
                                    move = 3;
                                else if (move == 4 && tiles[i][j + 1].wall)
                                    move = 3;
                            }
                            junctionFound = true;
                            break;
                        }
                        j++;
                    }
                    j = 0;
                    i++;
                }
                y += speed;
                break;
        }
        leftupperCorner[0] = x + correction;
        leftupperCorner[1] = y + correction;

        rightupperCorner[0] = x - correction + blinky.getWidth();
        rightupperCorner[1] = y + correction;

        leftdownCorner[0] = x + correction;
        leftdownCorner[1] = y - correction + blinky.getHeight();

        rightdownCorner[0] = x - correction + blinky.getWidth();
        rightdownCorner[1] = y - correction + blinky.getHeight();

        center[0] = x + rightupperCorner[0] - leftupperCorner[0];
        center[1] = y + leftdownCorner[1] - leftupperCorner[1];
        if (junctionFound)
            junctionControl++;
        if (junctionControl == 10) {
            junctionControl = 0;
            junctionFound = false;
        }
    }

    /**Metoda rysująca ducha
     * @param g2d Grafika 2D
     */
    public void drawAnimation(Graphics2D g2d) {
        g2d.drawImage(blinky, null, x, y);
    }

}