package de.neemann.oscilloscope.draw.elements;

import de.neemann.oscilloscope.draw.graphics.*;

import java.awt.event.MouseEvent;

/**
 * Base class of all the gui elements.
 *
 * @param <T> the type of the implementing class
 */
public abstract class Element<T extends Element<?>> {
    private Transform transform = Transform.IDENTITY;
    private GraphicMinMax minMax;
    private Container<?> parent;

    /**
     * Used to draw the element relative to the origin
     *
     * @param gr Graphic instance to draw to
     */
    public abstract void drawToOrigin(Graphic gr);

    /**
     * Draws the component to the given Graphic instance
     *
     * @param gr Graphic instance to draw to
     */
    public final void draw(Graphic gr) {
        drawToOrigin(new GraphicTransform(gr, transform));
    }

    /**
     * Sets the transform
     *
     * @param transform the transform
     * @return this for chained calls
     */
    public T setTransform(Transform transform) {
        this.transform = transform;
        minMax = null;
        return (T) this;
    }

    /**
     * @return the transformation
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Sets the position
     *
     * @param x x pos
     * @param y y pos
     * @return this for chained calls
     */
    public T setPos(int x, int y) {
        return setTransform(new TransformTranslate(x, y));
    }

    /**
     * @return the bounding box
     */
    public GraphicMinMax getBoundingBox() {
        if (minMax == null) {
            minMax = new GraphicMinMax(false);
            draw(minMax);
        }
        return minMax;
    }

    /**
     * Returns the position on screen
     *
     * @param pos position in local coordinate system
     * @return screen coordinates
     */
    public Vector getScreenPos(Vector pos) {
        if (parent != null)
            pos = parent.getScreenPos(pos);
        return transform.transform(pos);
    }

    /**
     * Called if the mouse wheel is turned upwards.
     *
     * @param ctrl ctrl is pressed
     */
    public void up(boolean ctrl) {
    }

    /**
     * Called if the mouse wheel is turned downwards.
     *
     * @param ctrl ctrl is pressed
     */
    public void down(boolean ctrl) {
    }

    /**
     * Called if the mouse buttons are pressed.
     *
     * @param button the button
     * @param ctrl   ctrl is pressed
     */
    public void clicked(int button, boolean ctrl) {
        if (button == MouseEvent.BUTTON1)
            up(ctrl);
        else if (button == MouseEvent.BUTTON3)
            down(ctrl);
    }

    /**
     * Sets te parent container
     *
     * @param parent the parent container
     */
    void setParent(Container<?> parent) {
        this.parent = parent;
    }

    /**
     * Closes down the element.
     * Stops timers, thread and so on
     */
    public void close() {
    }

    /**
     * Implements the visitor pattern
     *
     * @param visitor the visitor
     */
    public void traverse(Visitor visitor) {
        visitor.visit(this);
    }
}
