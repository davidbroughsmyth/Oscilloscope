package de.neemann.oscilloscope.signal;

import de.neemann.oscilloscope.draw.elements.Poti;

/**
 * Transfers a voltage ti a pixel number on the screen
 */
public class ValueToScreen implements ToScreen {
    private final Poti pos;
    private final int divs;
    private final int ofs;

    /**
     * Creates a new screen transformation
     *
     * @param pos  the pos poti
     * @param divs the number of divs on the screen
     */
    public ValueToScreen(Poti pos, int divs) {
        this.pos = pos;
        this.divs = divs;
        ofs = divs / 2;
    }

    @Override
    public int v(double v, int pixels) {
        double div = v + ofs + (pos.get() - 0.5) * 20;
        return (int) (div * pixels / divs);
    }
}
