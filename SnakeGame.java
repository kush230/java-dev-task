import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int GRID_SIZE = 20;
    private static final int TILE_SIZE = 20;
    private static final int GAME_SPEED = 150; // milliseconds

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private class Snake {
        private LinkedList<Point> body;
        private Direction direction;

        public Snake() {
            body = new LinkedList<>();
            body.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
            direction = Direction.RIGHT;
        }

        public void move() {
            Point head = body.getFirst();
            Point newHead = new Point(head);

            switch (direction) {
                case UP:
                    newHead.y--;
                    break;
                case DOWN:
                    newHead.y++;
                    break;
                case LEFT:
                    newHead.x--;
                    break;
                case RIGHT:
                    newHead.x++;
                    break;
            }

            body.addFirst(newHead);
            if (!eatFood()) {
                body.removeLast();
            }

            checkCollision();
        }

        public boolean eatFood() {
            Point head = body.getFirst();
            if (head.equals(food.getPosition())) {
                food.spawnFood();
                return true;
            }
            return false;
        }

        public LinkedList<Point> getBody() {
            return body;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void checkCollision() {
            Point head = body.getFirst();

            if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE || body.indexOf(head) > 0) {
                gameOver();
            }
        }
    }

    private class Food {
        private Point position;

        public Food() {
            position = new Point();
            spawnFood();
        }

        public Point getPosition() {
            return position;
        }

        public void spawnFood() {
            Random random = new Random();
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            position.setLocation(x, y);

            while (snake.getBody().contains(position)) {
                x = random.nextInt(GRID_SIZE);
                y = random.nextInt(GRID_SIZE);
                position.setLocation(x, y);
            }
        }
    }

    private Snake snake;
    private Food food;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);

        snake = new Snake();
        food = new Food();

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.getBody().size() - 1),
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Restart Game",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            snake = new Snake();
            food = new Food();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.clearRect(0, 0, getWidth(), getHeight());

        // Draw Snake
        g.setColor(Color.GREEN);
        for (Point point : snake.getBody()) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw Food
        g.setColor(Color.RED);
        g.fillRect(food.getPosition().x * TILE_SIZE, food.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (snake.direction != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (snake.direction != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (snake.direction != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (snake.direction != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
}
