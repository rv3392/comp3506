/**
 * A 2D cartesian plane implemented as with an array. Each (x,y) coordinate can
 * hold a single item of type <T>.
 *
 * @param <T> The type of element held in the data structure
 */
public class ArrayCartesianPlane<T> implements CartesianPlane<T> {

    private Object[][] plane;

    private int minimumX;
    private int maximumX;
    private int minimumY;
    private int maximumY;

    private int width;
    private int height;

    /**
     * Constructs a new ArrayCartesianPlane object with given minimum and
     * maximum bounds.
     *
     * Note that these bounds are allowed to be negative.
     *
     * @param minimumX A new minimum bound for the x values of
     *         elements.
     * @param maximumX A new maximum bound for the x values of
     *         elements.
     * @param minimumY A new minimum bound for the y values of
     *         elements.
     * @param maximumY A new maximum bound for the y values of
     *         elements.
     * @throws IllegalArgumentException if the x minimum is greater
     *         than the x maximum (and resp. with y min/max)
     */
    public ArrayCartesianPlane(int minimumX, int maximumX, int minimumY,
            int maximumY) throws IllegalArgumentException {

        if (minimumX > maximumX || minimumY > maximumY) {
            throw new IllegalArgumentException();
        }

        this.minimumX = minimumX;
        this.maximumX = maximumX;
        this.minimumY = minimumY;
        this.maximumY = maximumY;

        this.width = maximumX - minimumX + 1;
        this.height = maximumY - minimumY + 1;

        plane = new Object[this.height][this.width];
    }

    @Override
    public void add(int x, int y, T element) throws IllegalArgumentException {
        if (x > maximumX || x < minimumX) {
            throw new IllegalArgumentException();
        }

        if (y > maximumY || y < minimumY) {
            throw new IllegalArgumentException();
        }

        this.plane[y - minimumY][x - minimumX] = element;
    }

    @Override
    public T get(int x, int y) throws IndexOutOfBoundsException {
        if (x > maximumX || x < minimumX) {
            throw new IndexOutOfBoundsException();
        }

        if (y > maximumY || y < minimumY) {
            throw new IndexOutOfBoundsException();
        }

        return (T) this.plane[y - minimumY][x - minimumX];
    }

    @Override
    public boolean remove(int x, int y) throws IndexOutOfBoundsException {
        if (x > maximumX || x < minimumX) {
            throw new IndexOutOfBoundsException();
        }

        if (y > maximumY || y < minimumY) {
            throw new IndexOutOfBoundsException();
        }

        if (this.plane[y - minimumY][x - minimumX] == null) {
            return false;
        }

        this.plane[y - minimumY][x - minimumX] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.plane[y - minimumY][x - minimumX] = null;
            }
        }
    }

    @Override
    public void resize(int newMinimumX, int newMaximumX, int newMinimumY, int newMaximumY) throws IllegalArgumentException {
        if (newMinimumX > newMaximumX || newMinimumY > newMaximumY) {
            throw new IllegalArgumentException();
        }

        int newWidth = newMaximumX - newMinimumX + 1;
        int newHeight = newMaximumY - newMinimumY + 1;
        Object[][] newPlane = new Object[newHeight][newWidth];

        // Iterate through the grid
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (x >= newMinimumX - minimumX && x <= newMaximumX - minimumX
                        && y >= newMinimumY - minimumY && y <= newMaximumY - minimumY) {
                    // Copy old plane to new plane if x,y are within the new minimum and maximum bounds
                    newPlane[y - newMinimumY][x - newMinimumX] = this.plane[y - minimumY][x - minimumX];
                } else {
                    // Otherwise if the old plane contains a non-null outside of the new bounds, throw an exception
                    if (this.plane[y - minimumY][x - minimumX] != null) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        // If the method reaches here then the plane was successfully copied and all the new values should be copied
        // over
        this.width = newWidth;
        this.height = newHeight;
        this.plane = newPlane;

        this.maximumX = newMaximumX;
        this.minimumX = newMinimumX;
        this.maximumY = newMaximumY;
        this.minimumY = newMinimumY;
    }
}

