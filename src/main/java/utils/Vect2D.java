/**
 * Vect2D is a simple class for storing a two dimensional vertex.
 * @author  Igor H. Torati Ruy
 * */

package utils;

public class Vect2D {

    /**
     * This variable stores the position in the X-axis: {@link #x}
     * */
    public double x;

    /**
     * This variable stores the position in the Y-axis: {@link #y}
     * */
    public double y;

    /**
     * Constructor for Vect2D.
     * @param x     value to be stored as the X-axis value.
     * @param y     value to be stored as the Y-axis value.
     * */
    public Vect2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for Vect2D initializing values as zero.
     * */
    public Vect2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Copy method for Vect2D.
     * @param v     the Vect2D to be copied.
     * */
    public void copy(Vect2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Set method for Vect2D.
     * @param x     the value to be copied to x.
     * @param y     the value to be copied to y.
     * */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Place bigger values on p2.
     * @param p1    source to have values compared and possibly
     *              changed.
     * @param p2    destination to have values compared and possibly
     *              changed, keeping the bigger values from itself
     *              and p1.
     * */
    public static void swapBiggerXY(Vect2D p1, Vect2D p2) {
        double aux;
        if (p1.x > p2.x) {
            aux = p1.x;
            p1.x = p2.x;
            p2.x = aux;
        }
        if (p1.y > p2.y) {
            aux = p1.y;
            p1.y = p2.y;
            p2.y = aux;
        }
    }

    /**
     * Compare two Vect2D, will swap their x and y values if p1.x > p2.x.
     * @param p1    source to have values compared and possibly
     *              changed.
     * @param p2    destination to have values compared and possibly
     *              changed if p1.x > p2.x.
     * */
    public static void swapByBiggerX(Vect2D p1, Vect2D p2) {
        if (p1.x > p2.x) {
            Vect2D aux = new Vect2D(p1.x, p1.y);
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = aux.x;
            p2.y = aux.y;
        }
    }

    /**
     * Compare two Vect2D, will swap their x and y values if p1.y > p2.y.
     * @param p1    source to have values compared and possibly
     *              changed.
     * @param p2    destination to have values compared and possibly
     *              changed if p1.y > p2.y.
     * */
    public static void swapByBiggerY(Vect2D p1, Vect2D p2) {
        if (p1.y > p2.y) {
            Vect2D aux = new Vect2D(p1.x, p1.y);
            p1.x = p2.x;
            p1.y = p2.y;
            p2.x = aux.x;
            p2.y = aux.y;
        }
    }

    @Override
    public String toString() {
        return "Vect2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
