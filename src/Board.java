import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Klasa odpowiedzialna za grę, łączy ona wszystkie klasy użyte w grze: blinky, pacman i tile
 */
public class Board extends JPanel implements ActionListener {
    private boolean inGame, pause, sound;
    private int board_x, board_y, board_width, board_height;
    private int score;
    private int tile_width, tile_height;
    private KeyListener keyListener;
    private Timer timer;

    private BufferedImage lives;
    private AudioInputStream stream;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip clip;

    private Pacman pacman;
    private Blinky blinky1;
    private Blinky blinky2;
    private Blinky blinky3;
    private Blinky blinky4;

    private Tile[][] tiles = new Tile[28][31];
    private final int[][] tilesRepresentation = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 7, 6, 6, 7, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 6, 6, 6, 6, 6, 6, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 2, 6, 6, 7, 1, 6, 6, 6, 6, 6, 6, 1, 7, 6, 6, 2, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 6, 6, 6, 6, 6, 6, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 7, 6, 6, 6, 6, 6, 6, 6, 6, 7, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 1, 1, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 0, 0, 1, 1, 2, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 2, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    /**
     * Konstruktor klasy
     *
     * @param board_width
     * @param board_height
     */
    public Board(int board_width, int board_height) {
        loadImages();
        loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_chomp.wav"));
        initBoard(board_width, board_height);
        initVariables();
        inGame = true;
    }

    /**
     * Ustawia odpowiednie koordynaty, szerokość oraz wysokość planszy
     *
     * @param width
     * @param height
     */
    private void initBoard(int width, int height) {
        KeyListener();
        addKeyListener(keyListener);
        setFocusable(true);
        setBackground(Color.black);
        this.board_x = 90;
        this.board_y = 40;
        this.board_width = width;
        this.board_height = height;
        this.tile_width = width / 25;
        this.tile_height = height / 30;
    }

    private void loadImages() {
        try {
            lives = ImageIO.read(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Images\\PacMan3left.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Klasa odpowiedzialna za sterowanie dźwiękiem
     *
     * @param sound Jest to plik, który będzie odtwarzany
     */
    private void loadSound(File sound) {
        try {
            stream = AudioSystem.getAudioInputStream(sound);

            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);

            clip = (Clip) AudioSystem.getLine(info);


            clip.open(stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicjalizuje zmienne wykorzystywane w grze, szczególnie płytki
     */
    private void initVariables() {
        int y = board_y, x = board_x - tile_width, i = 0, j = 0;
        for (int[] row : tilesRepresentation) {
            for (int tile : row) {
                if (tile == 0) {
                    this.tiles[i][j] = new Tile(x, y, 28, 25);
                    this.tiles[i][j].dot = true;
                } else if (tile == 1) {
                    this.tiles[i][j] = new Tile(x, y, 28, 25);
                    this.tiles[i][j].wall = true;
                } else if (tile == 2) {
                    this.tiles[i][j] = new Tile(x, y, 28, 25);
                    this.tiles[i][j].junction = true;
                    this.tiles[i][j].dot = true;
                } else if (tile == 6) {
                    this.tiles[i][j] = new Tile(x, y, 28, 25);
                    this.tiles[i][j].eaten = true;
                } else if (tile == 7) {
                    this.tiles[i][j] = new Tile(x, y, 28, 25);
                    this.tiles[i][j].eaten = true;
                    this.tiles[i][j].junction = true;
                } else if (tile == 8) {
                    this.tiles[i][j] = new Tile(x + 2, y + 2, 28, 25);
                    this.tiles[i][j].bigDot = true;
                }
                i++;
                x += tile_width;
            }
            j++;
            i = 0;
            x = board_x - tile_width;
            y += tile_height;
        }
        pacman = new Pacman(tiles[14][0].getX(), tiles[0][17].getY(), 3);
        blinky1 = new Blinky(tiles[1][0].getX(), tiles[0][1].getY());
        blinky2 = new Blinky(tiles[1][0].getX(), tiles[0][26].getY());
        blinky3 = new Blinky(tiles[26][0].getX(), tiles[0][1].getY());
        blinky4 = new Blinky(tiles[26][0].getX(), tiles[0][29].getY());

        timer = new Timer(40, this);
        timer.start();
        this.score = 0;
        inGame = true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * Klasa odpowiedzialna za rysowanie i przesuwanie aktorów
     *
     * @param g
     */
    private void doDrawing(Graphics g) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        drawMap((Graphics2D) g);
        drawPoints((Graphics2D) g);
        pacman.drawAnimation((Graphics2D) g);
        blinky1.drawAnimation((Graphics2D) g);
        blinky2.drawAnimation((Graphics2D) g);
        blinky3.drawAnimation((Graphics2D) g);
        blinky4.drawAnimation((Graphics2D) g);
        drawScore((Graphics2D) g);
        drawLives((Graphics2D) g);
        if (pause) {
            try {
                clip.close();
                stream.close();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (inGame) {
            pacman.move(tiles, this);
            blinky1.move(tiles, pacman, this);
            blinky2.move(tiles, pacman, this);
            blinky3.move(tiles, pacman, this);
            blinky4.move(tiles, pacman, this);
        } else if (!inGame && pacman.getLives() == 0) {
            if (!sound) {
                try {
                    clip.close();
                    stream.close();
                    loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_beginning.wav"));
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sound = true;
            }
            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
            g.drawString("Koniec gry, przegrałeś!", board_x + 70, board_y + board_height / 2);
            g.drawString("Score: " + score, board_x + 70, board_y + board_height / 2 + tile_height * 3);
        } else {
            if (!sound) {
                try {
                    clip.close();
                    stream.close();
                    loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_intermission.wav"));
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sound = true;
            }
            g.setColor(Color.yellow);
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
            g.drawString("Koniec gry, wygrałeś!", board_x + 70, board_y + board_height / 2);
            g.drawString("Score: " + score, board_x + 70, board_y + board_height / 2 + tile_height * 3);
        }
    }

    private void drawMap(Graphics2D g2d) {
        g2d.setColor(Color.yellow);
        g2d.drawRect(board_x - tile_width - 1, board_y - 1, board_width + 3 * tile_width + 1, board_height + tile_height + 1);
    }

    private void drawPoints(Graphics2D g2d) {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.show(g2d);
            }
        }
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.yellow);
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        g2d.drawString("Score:" + score, board_x + board_width - tile_width, board_y + board_height + 2 * tile_height);
    }

    private void drawLives(Graphics2D g2d) {
        int x = 20;
        for (int i = pacman.getLives(); i > 0; i--) {
            g2d.drawImage(lives, null, board_x + x, board_y + board_height + tile_height + 5);
            x += 20;
        }
    }

    void death() {
        try {
            clip.close();
            stream.close();
            loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_death.wav"));
            clip.start();
            TimeUnit.MILLISECONDS.sleep(750);
            clip.close();
            stream.close();
            loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_chomp.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pacman.getLives() == 0) {
            inGame = false;
        } else {
            pacman = new Pacman(tiles[14][0].getX(), tiles[0][17].getY(), pacman.getLives());
            blinky1 = new Blinky(tiles[1][0].getX(), tiles[0][1].getY());
            blinky2 = new Blinky(tiles[1][0].getX(), tiles[0][26].getY());
            blinky3 = new Blinky(tiles[26][0].getX(), tiles[0][1].getY());
            blinky4 = new Blinky(tiles[26][0].getX(), tiles[0][29].getY());
        }
    }

    private void KeyListener() {
        keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if (inGame) {
                    if (key == KeyEvent.VK_LEFT) {
                        pacman.setMove(0);
                    } else if (key == KeyEvent.VK_RIGHT) {
                        pacman.setMove(1);
                    } else if (key == KeyEvent.VK_UP) {
                        pacman.setMove(2);
                    } else if (key == KeyEvent.VK_DOWN) {
                        pacman.setMove(3);
                    } else if (key == KeyEvent.VK_ESCAPE) {
                        pause = !pause;
                        try {
                            clip.close();
                            stream.close();
                            loadSound(new File("C:\\Users\\AnarosPC\\IdeaProjects\\Pacman\\src\\Sound\\pacman_chomp.wav"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    if (key == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
