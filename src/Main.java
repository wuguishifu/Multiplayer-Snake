import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    // the frame and panel and keylistener
    private static JFrame frame;
    private static JPanel panel;
    private static KeyListener keyListener;

    // game variables
    private static boolean windowShouldClose = false;
    private static boolean gameOver = false;
    private static long startTime = System.currentTimeMillis();
    private static int FPS = 100;
    private static int score = 0;

    // the grid dimensions
    private static int gridSize = 16;
    private static int squareSize = 50;
    private static int width = gridSize * squareSize;
    private static int height = gridSize * squareSize;

    // the snake
    private static Snake snake;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean up = false;
    private static boolean down = false;
    private static boolean hasMoved = false;
    private static boolean consumed = false;

    // the pellet
    private static int px, py;

    // the color pallet
    private static final Color SNAKE_COLOR = new Color(210, 195, 216);
    private static final Color BACKGROUND_COLOR = new Color(246, 242, 236);
    private static final Color PELLET_COLOR = new Color(248, 203, 209);

    /**
     * the main runnable
     * @param args - arguments
     */
    public static void main(String[] args) {

        // create the snake
        snake = new Snake(gridSize/2, gridSize/2);

        // create the frame
        frame = new JFrame("Multiplayer Snake!");
        frame.setSize(new Dimension(gridSize * squareSize, gridSize * squareSize));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // create a randomly placed pellet
        px = Math.abs((int)(gridSize * Math.random()));
        py = Math.abs((int)(gridSize * Math.random()));

        // create the panel
        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {

                // clear the entire panel
                g.clearRect(0, 0, width, height);

                // draw the snake
                g.setColor(SNAKE_COLOR);
                for (int[] position : snake.getSegments()) {
                    g.fillRect(position[0] * squareSize, position[1] * squareSize, squareSize, squareSize);
                }

                // draw the pellet
                g.setColor(PELLET_COLOR);
                g.fillRect(px * squareSize, py * squareSize, squareSize, squareSize);

                // draw the grid lines
                g.setColor(Color.BLACK);
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        g.drawLine(i * squareSize, j * squareSize, i * squareSize, height);
                        g.drawLine(i * squareSize, j * squareSize, width, j * squareSize);
                    }
                }
            }
        };
        panel.setSize(new Dimension(gridSize * squareSize, gridSize * squareSize));
        panel.setBackground(BACKGROUND_COLOR);

        // create the key listener
        keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if (!down && !hasMoved) {
                            up = true;
                            hasMoved = true;
                            left = false;
                            right = false;
                            down = false;
                        }
                        break;
                    case KeyEvent.VK_A:
                        if (!right && !hasMoved) {
                            left = true;
                            hasMoved = true;
                            up = false;
                            right = false;
                            down = false;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (!up && !hasMoved) {
                            down = true;
                            hasMoved = true;
                            up = false;
                            right = false;
                            left = false;
                        }
                        break;
                    case KeyEvent.VK_D:
                        if (!left && !hasMoved) {
                            right = true;
                            hasMoved = true;
                            left = false;
                            up = false;
                            down = false;
                        }
                        break;
                    case KeyEvent.VK_ESCAPE: windowShouldClose = true; break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        // add the panel to the frame and set it visible
        frame.add(panel);
        frame.addKeyListener(keyListener);
        frame.setVisible(true);

        // fix the frame size
        Dimension actualSize = frame.getContentPane().getSize();
        int extraW = width - actualSize.width;
        int extraH = height - actualSize.height;
        frame.setSize(width + extraW, height + extraH);

        // the main game loop
        while (!windowShouldClose) {
            while (!gameOver && !windowShouldClose) {

                // main game
                // move the snake
                snake.moveHead(up, down, left, right);
                snake.move(consumed);
                consumed = false;
                hasMoved = false;

                // check if the snake ate a pellet
                checkPelletCollision();

                panel.repaint();

                startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(FPS);
                } catch (InterruptedException ignored) {
                }
            }

            // test for restarting the game after game over

            startTime = System.currentTimeMillis();
            try {
                Thread.sleep(FPS);
            } catch (InterruptedException ignored) {
            }
        }

        frame.dispose();
    }

    private static void checkPelletCollision() {
        // check if the head is in the same space as the pellet
        if (snake.getX() == px && snake.getY() == py) {
            consumed = true;
            score++;

            // create a new pellet randomly
            boolean isNewSpace = false;
            while (!isNewSpace) {
                // create a randomly placed pellet
                px = Math.abs((int)(gridSize * Math.random()));
                py = Math.abs((int)(gridSize * Math.random()));

                // check if it is in the same location as a snake segment, if it is loop
                for (int[] position : snake.getSegments()) {
                    if (position[0] == px && position[1] == py) {
                        break;
                    }
                }
                isNewSpace = true;
            }
        }
    }
}
