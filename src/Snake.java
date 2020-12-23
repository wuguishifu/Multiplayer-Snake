import java.util.ArrayList;

public class Snake {

    // the length of the snake
    private int length = 0;
    private static final int minLength = 20;

    // the positions of the snake segments
    private ArrayList<int[]> positions = new ArrayList<>();

    // the current position of the head of the snake
    private int x, y;

    /**
     * default constructor
     * @param x - the x position
     * @param y - the y position
     */
    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter method
     * @return - the x position of the head
     */
    public int getX() {
        return this.x;
    }

    /**
     * getter method
     * @return - the y position of the head
     */
    public int getY() {
        return this.y;
    }

    /**
     * getter method
     * @return - the segments of this snake
     */
    public ArrayList<int[]> getSegments() {
        return this.positions;
    }

    /**
     * moves the head of the snake
     * @param dx - the change in x
     * @param dy - the change in y
     */
    private void moveHead(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * moves in a direction
     * @param up - if going up
     * @param down - if going down
     * @param left - if going left
     * @param right - if going right
     */
    public void moveHead(boolean up, boolean down, boolean left, boolean right) {
        if (left) {
            this.moveHead(-1, 0);
        }
        if (right) {
            this.moveHead(1, 0);
        }
        if (up) {
            this.moveHead(0, -1);
        }
        if (down) {
            this.moveHead(0, 1);
        }
    }

    /**
     * moves the snake by 1 unit
     * @param consumed - if the snake has consumed a pellet
     */
    public void move(boolean consumed) {
        // add a new snake segment at the position of the head
        this.positions.add(0, new int[]{x, y});

        if (!consumed && length > minLength) {
            length++;
            this.positions.remove(positions.size() - 1); // remove the last element unless the snake just ate a pellet
        } else {
            length++;
        }
    }
}