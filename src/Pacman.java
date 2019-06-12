import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Klasa, która tworzy pacmana, postać sterowaną przez gracza
 */
public class Pacman {
    private int x, y, move, lastMove, newMove, speed, correction, lives, animation;
    private int[] leftupperCorner, rightupperCorner, leftdownCorner, rightdownCorner;
    boolean collisionFlag;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
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

    public void setMove(int newMove) {
        collisionFlag = false;
        this.lastMove = move;
        this.move = newMove;
    }

    private BufferedImage pacman1;
    private BufferedImage pacman2up;
    private BufferedImage pacman3up;
    private BufferedImage pacman4up;
    private BufferedImage pacman2down;
    private BufferedImage pacman3down;
    private BufferedImage pacman4down;
    private BufferedImage pacman2left;
    private BufferedImage pacman3left;
    private BufferedImage pacman4left;
    private BufferedImage pacman2right;
    private BufferedImage pacman3right;
    private BufferedImage pacman4right;

    /**
     * Konstruktor Pacman, wczytuje obrazy do animacji i ustawia koordynaty rogów kwadratu, w której postać znajduje się
     * @param x
     * @param y
     * @param lives
     */
    public Pacman(int x, int y, int lives) {
        try {
            pacman1 = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan1.gif"));
            pacman2up = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan2up.gif"));
            pacman3up = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan3up.gif"));
            pacman4up = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan4up.gif"));
            pacman2down = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan2down.gif"));
            pacman3down = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan3down.gif"));
            pacman4down = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan4down.gif"));
            pacman2left = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan2left.gif"));
            pacman3left = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan3left.gif"));
            pacman4left = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan4left.gif"));
            pacman2right = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan2right.gif"));
            pacman3right = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan3right.gif"));
            pacman4right = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan4right.gif"));
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
        rightupperCorner = new int[]{x - correction + pacman1.getWidth(), y + correction};
        leftdownCorner = new int[]{x + correction, y - correction + pacman1.getHeight()};
        rightdownCorner = new int[]{x - correction + pacman1.getWidth(), y - correction + pacman1.getHeight()};
        move = -1;
        lastMove = -1;
        newMove = -1;
        this.lives = lives;
        animation = 0;
    }

    /**
     * Metoda odpowiedzialna za przesuwanie pacmana po mapie, odpowiada też za kolizję
     * @param tiles Obiekt płytek
     * @param game Główny obiekt gry
     */
    void move(Tile[][] tiles, Board game) {
        switch (move) {
            case 0:
                for (Tile[] row : tiles) {
                    for (Tile tile : row) {
                        if (leftupperCorner[0] - speed <= tile.getRightdownCorner()[0] && leftupperCorner[0] - speed > tile.getLeftdownCorner()[0] && leftupperCorner[1] < tile.getRightdownCorner()[1] && leftupperCorner[1] > tile.getRightupperCorner()[1] && tile.wall
                                || leftdownCorner[0] - speed <= tile.getRightupperCorner()[0] && leftdownCorner[0] - speed > tile.getLeftupperCorner()[0] && leftdownCorner[1] > tile.getRightupperCorner()[1] && leftdownCorner[1] < tile.getRightdownCorner()[1] && tile.wall) {
                            if (lastMove != 1) {
                                collisionFlag = true;
                                newMove = move;
                                move = lastMove;
                            }
                            return;
                        } else if (leftupperCorner[0] - speed < tile.getRightdownCorner()[0] && leftupperCorner[0] - speed > tile.getLeftdownCorner()[0] && leftupperCorner[1] < tile.getRightdownCorner()[1] && leftdownCorner[1] > tile.getRightupperCorner()[1] && tile.dot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 1);
                        } else if (leftupperCorner[0] - speed < tile.getRightdownCorner()[0] && leftupperCorner[0] - speed > tile.getLeftdownCorner()[0] && leftupperCorner[1] < tile.getRightdownCorner()[1] && leftdownCorner[1] > tile.getRightupperCorner()[1] && tile.bigDot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 1);
                        }
                    }
                }
                x -= speed;
                break;
            case 1:
                for (Tile[] row : tiles) {
                    for (Tile tile : row) {
                        if (rightupperCorner[0] + speed >= tile.getLeftdownCorner()[0] && rightupperCorner[0] + speed < tile.getRightdownCorner()[0] && rightupperCorner[1] < tile.getLeftdownCorner()[1] && rightupperCorner[1] > tile.getLeftupperCorner()[1] && tile.wall
                                || rightdownCorner[0] + speed >= tile.getLeftupperCorner()[0] && rightdownCorner[0] + speed < tile.getRightupperCorner()[0] && rightdownCorner[1] > tile.getLeftupperCorner()[1] && rightdownCorner[1] < tile.getLeftdownCorner()[1] && tile.wall) {
                            if (lastMove != 0) {
                                collisionFlag = true;
                                newMove = move;
                                move = lastMove;
                            }
                            return;
                        } else if (rightupperCorner[0] + speed > tile.getLeftdownCorner()[0] && rightupperCorner[0] + speed < tile.getRightdownCorner()[0] && rightupperCorner[1] < tile.getLeftdownCorner()[1] && rightdownCorner[1] > tile.getLeftupperCorner()[1] && tile.dot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 1);
                        } else if (rightupperCorner[0] + speed > tile.getLeftdownCorner()[0] && rightupperCorner[0] + speed < tile.getRightdownCorner()[0] && rightupperCorner[1] < tile.getLeftdownCorner()[1] && rightdownCorner[1] > tile.getLeftupperCorner()[1] && tile.bigDot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 15);
                        }
                    }
                }
                x += speed;
                break;
            case 2:
                for (Tile[] row : tiles) {
                    for (Tile tile : row) {
                        if (leftupperCorner[1] - speed <= tile.getRightdownCorner()[1] && leftupperCorner[1] - speed > tile.getRightupperCorner()[1] && leftupperCorner[0] < tile.getRightdownCorner()[0] && leftupperCorner[0] >= tile.getLeftdownCorner()[0] && tile.wall
                                || rightupperCorner[1] - speed <= tile.getLeftdownCorner()[1] && rightupperCorner[1] - speed > tile.getLeftupperCorner()[1] && rightupperCorner[0] > tile.getLeftdownCorner()[0] && rightupperCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                            if (lastMove != 3) {
                                collisionFlag = true;
                                newMove = move;
                                move = lastMove;
                            }
                            return;
                        } else if (leftupperCorner[1] - speed < tile.getRightdownCorner()[1] && leftupperCorner[1] - speed > tile.getRightupperCorner()[1] && leftupperCorner[0] < tile.getRightdownCorner()[0] && rightupperCorner[0] > tile.getLeftdownCorner()[0] && tile.dot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 1);
                        } else if (leftupperCorner[1] - speed < tile.getRightdownCorner()[1] && leftupperCorner[1] - speed > tile.getRightupperCorner()[1] && leftupperCorner[0] < tile.getRightdownCorner()[0] && rightupperCorner[0] > tile.getLeftdownCorner()[0] && tile.bigDot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 15);
                        }
                    }
                }
                y -= speed;
                break;
            case 3:
                for (Tile[] row : tiles) {
                    for (Tile tile : row) {
                        if (leftdownCorner[1] + speed >= tile.getRightupperCorner()[1] && leftdownCorner[1] + speed < tile.getRightdownCorner()[1] && leftdownCorner[0] <= tile.getRightupperCorner()[0] && leftdownCorner[0] >= tile.getLeftupperCorner()[0] && tile.wall
                                || rightdownCorner[1] + speed >= tile.getLeftupperCorner()[1] && rightdownCorner[1] + speed < tile.getLeftdownCorner()[1] && rightdownCorner[0] >= tile.getLeftupperCorner()[0] && rightdownCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                            if (lastMove != 2) {
                                collisionFlag = true;
                                newMove = move;
                                move = lastMove;
                            }
                            return;
                        } else if (leftdownCorner[1] + speed >= tile.getRightupperCorner()[1] && leftdownCorner[1] + speed < tile.getRightdownCorner()[1] && leftdownCorner[0] < tile.getRightupperCorner()[0] && rightdownCorner[0] > tile.getLeftupperCorner()[0] && tile.dot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 1);
                        } else if (leftdownCorner[1] + speed >= tile.getRightupperCorner()[1] && leftdownCorner[1] + speed < tile.getRightdownCorner()[1] && leftdownCorner[0] < tile.getRightupperCorner()[0] && rightdownCorner[0] > tile.getLeftupperCorner()[0] && tile.bigDot && !tile.eaten) {
                            tile.eaten = true;
                            game.setScore(game.getScore() + 15);
                        }
                    }
                }
                y += speed;
                break;
        }
        leftupperCorner[0] = x + correction;
        leftupperCorner[1] = y + correction;

        rightupperCorner[0] = x - correction + pacman1.getWidth();
        rightupperCorner[1] = y + correction;

        leftdownCorner[0] = x + correction;
        leftdownCorner[1] = y - correction + pacman1.getHeight();

        rightdownCorner[0] = x - correction + +pacman1.getWidth();
        rightdownCorner[1] = y - correction + pacman1.getHeight();
        if (collisionFlag) {
            switch (newMove) {
                case 0:
                    for (Tile[] row : tiles) {
                        for (Tile tile : row) {
                            if (leftupperCorner[0] - speed <= tile.getRightdownCorner()[0] && leftupperCorner[0] - speed > tile.getLeftdownCorner()[0] && leftupperCorner[1] < tile.getRightdownCorner()[1] && leftupperCorner[1] > tile.getRightupperCorner()[1] && tile.wall
                                    || leftdownCorner[0] - speed <= tile.getRightupperCorner()[0] && leftdownCorner[0] - speed > tile.getLeftupperCorner()[0] && leftdownCorner[1] > tile.getRightupperCorner()[1] && leftdownCorner[1] < tile.getRightdownCorner()[1] && tile.wall) {
                                return;
                            }
                        }
                    }
                    collisionFlag = false;
                    lastMove = -1;
                    move = newMove;
                    break;
                case 1:
                    for (Tile[] row : tiles) {
                        for (Tile tile : row) {
                            if (rightupperCorner[0] + speed >= tile.getLeftdownCorner()[0] && rightupperCorner[0] + speed < tile.getRightdownCorner()[0] && rightupperCorner[1] < tile.getLeftdownCorner()[1] && rightupperCorner[1] >= tile.getLeftupperCorner()[1] && tile.wall
                                    || rightdownCorner[0] + speed >= tile.getLeftupperCorner()[0] && rightdownCorner[0] + speed < tile.getRightupperCorner()[0] && rightdownCorner[1] > tile.getLeftupperCorner()[1] && rightdownCorner[1] <= tile.getLeftdownCorner()[1] && tile.wall) {
                                return;
                            }
                        }
                    }
                    collisionFlag = false;
                    lastMove = -1;
                    move = newMove;
                    break;
                case 2:
                    for (Tile[] row : tiles) {
                        for (Tile tile : row) {
                            if (leftupperCorner[1] - speed <= tile.getRightdownCorner()[1] && leftupperCorner[1] - speed > tile.getRightupperCorner()[1] && leftupperCorner[0] < tile.getRightdownCorner()[0] && leftupperCorner[0] >= tile.getLeftdownCorner()[0] && tile.wall
                                    || rightupperCorner[1] - speed <= tile.getLeftdownCorner()[1] && rightupperCorner[1] - speed > tile.getLeftupperCorner()[1] && rightupperCorner[0] > tile.getLeftdownCorner()[0] && rightupperCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                                return;
                            }
                        }
                    }
                    collisionFlag = false;
                    lastMove = -1;
                    move = newMove;
                    break;
                case 3:
                    for (Tile[] row : tiles) {
                        for (Tile tile : row) {
                            if (leftdownCorner[1] + speed >= tile.getRightupperCorner()[1] && leftdownCorner[1] + speed < tile.getRightdownCorner()[1] && leftdownCorner[0] <= tile.getRightupperCorner()[0] && leftdownCorner[0] >= tile.getLeftupperCorner()[0] && tile.wall
                                    || rightdownCorner[1] + speed >= tile.getLeftupperCorner()[1] && rightdownCorner[1] + speed < tile.getLeftdownCorner()[1] && rightdownCorner[0] >= tile.getLeftupperCorner()[0] && rightdownCorner[0] <= tile.getRightdownCorner()[0] && tile.wall) {
                                return;
                            }
                        }
                    }
                    collisionFlag = false;
                    lastMove = -1;
                    move = newMove;
                    break;
            }
        }
    }

    /**
     * Metoda odpowiedzialna za animacje, działa poprzez naprzemienne ładowanie 4 obrazków na kierunek ruchu
     * @param g2d Grafika 2d
     */
    void drawAnimation(Graphics2D g2d) {
        if (move == -1) {
            g2d.drawImage(pacman1, null, x, y);
            return;
        }
        switch (animation) {
            case 0:
                g2d.drawImage(pacman1, null, x, y);
                animation++;
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                switch (move) {
                    case 0:
                        g2d.drawImage(pacman2left, null, x, y);
                        break;
                    case 1:
                        g2d.drawImage(pacman2right, null, x, y);
                        break;
                    case 2:
                        g2d.drawImage(pacman2up, null, x, y);
                        break;
                    case 3:
                        g2d.drawImage(pacman2down, null, x, y);
                        break;
                }
                animation++;
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                switch (move) {
                    case 0:
                        g2d.drawImage(pacman3left, null, x, y);
                        break;
                    case 1:
                        g2d.drawImage(pacman3right, null, x, y);
                        break;
                    case 2:
                        g2d.drawImage(pacman3up, null, x, y);
                        break;
                    case 3:
                        g2d.drawImage(pacman3down, null, x, y);
                        break;
                }
                animation++;
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                switch (move) {
                    case 0:
                        g2d.drawImage(pacman4left, null, x, y);
                        break;
                    case 1:
                        g2d.drawImage(pacman4right, null, x, y);
                        break;
                    case 2:
                        g2d.drawImage(pacman4up, null, x, y);
                        break;
                    case 3:
                        g2d.drawImage(pacman4down, null, x, y);
                        break;
                }
                animation = 0;
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Error in animation");
        }
    }
}