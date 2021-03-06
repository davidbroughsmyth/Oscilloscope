package de.neemann.oscilloscope.draw.elements;


import de.neemann.oscilloscope.draw.graphics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static de.neemann.oscilloscope.draw.elements.Scaling.SIZE;
import static de.neemann.oscilloscope.draw.elements.Scaling.SIZE2;

/**
 * Abstraction of a selector knob
 *
 * @param <T> type of the items
 */
public class SelectorKnob<T> extends ObservableElement<SelectorKnob<T>> {
    private final int radius;
    private final ArrayList<T> items;
    private final String name;
    private int selectedPosition;

    /**
     * Creates a new selector knob
     *
     * @param name   the name
     * @param radius the size
     */
    public SelectorKnob(String name, int radius) {
        this.name = name;
        items = new ArrayList<>();
        this.radius = radius;
    }

    /**
     * Adds a item to the knob
     *
     * @param item the list of items
     * @return this for chained calls
     */
    public SelectorKnob<T> add(T item) {
        items.add(item);
        return this;
    }

    /**
     * Adds all items to the knob
     *
     * @param items the list of items
     * @return this for chained calls
     */
    public SelectorKnob<T> addAll(Collection<T> items) {
        this.items.addAll(items);
        return this;
    }

    /**
     * Adds all items to the knob
     *
     * @param items the list of items
     * @return this for chained calls
     */
    public SelectorKnob<T> addAll(T[] items) {
        return addAll(Arrays.asList(items));
    }

    /**
     * @return the selected setting
     */
    public T getSelected() {
        return items.get(selectedPosition);
    }

    /**
     * @return the items
     */
    public ArrayList<T> getItems() {
        return items;
    }

    @Override
    public void drawToOrigin(Graphic gr) {
        gr.drawCircle(new Vector(-radius, -radius), new Vector(radius, radius), Style.NORMAL);
        int r = radius - Style.MAXLINETHICK * 2;
        gr.drawCircle(new Vector(-r, -r), new Vector(r, r), Style.SWITCH);

        for (int i = 0; i < items.size(); i++) {
            VectorInterface p1 = getOffset(i, radius + SIZE2 - Style.MAXLINETHICK / 2);
            VectorInterface p2 = getOffset(i, radius + SIZE2 + Style.MAXLINETHICK / 2);
            gr.drawLine(p1, p2, Style.PRINT);

            VectorInterface p3 = getOffset(i, radius + SIZE - Style.MAXLINETHICK);
            gr.drawText(p3, getStringFor(items.get(i)), Orientation.from(p3), Style.PRINT);
        }

        VectorInterface p1 = getOffset(selectedPosition, r);
        VectorInterface p2 = getOffset(selectedPosition, r / 3);
        gr.drawLine(p1, p2, Style.NORMAL);

        gr.drawText(new Vector(0, -radius - SIZE - SIZE2 - Style.MAXLINETHICK - getNameOfs()), name, Orientation.CENTERBOTTOM, Style.PRINT);
    }

    int getNameOfs() {
        return 0;
    }

    String getStringFor(T t) {
        return t.toString();
    }

    VectorInterface getOffset(float n, int radius) {
        double a = 3 * Math.PI / 2 * n / (items.size() - 1) + Math.PI * 3 / 4;
        return new VectorFloat((float) (radius * Math.cos(a)), (float) (radius * Math.sin(a)));
    }

    @Override
    public void down(boolean ctrl) {
        if (selectedPosition < items.size() - 1) {
            selectedPosition++;
            hasChanged();
        }
    }

    @Override
    public void up(boolean ctrl) {
        if (selectedPosition > 0) {
            selectedPosition--;
            hasChanged();
        }
    }

    /**
     * Sets the position of the knob
     *
     * @param i the new position
     */
    public void set(int i) {
        if (i < 0)
            i = 0;
        else if (i >= items.size())
            i = items.size() - 1;

        if (i != selectedPosition) {
            selectedPosition = i;
            hasChanged();
        }
    }
}
