package de.neemann.oscilloscope.draw.elements;

import de.neemann.oscilloscope.draw.graphics.Graphic;
import de.neemann.oscilloscope.draw.graphics.Style;
import de.neemann.oscilloscope.draw.graphics.Vector;

/**
 * A power switch.
 * Adds a LED beside the switch
 */
public class PowerSwitch extends OnOffSwitch {
    private static final Vector RAD = new Vector(Scaling.SIZE2, Scaling.SIZE2);
    private static final Vector POS = new Vector(Scaling.SIZE * 2, Scaling.SIZE);

    /**
     * Creates a new switch
     */
    public PowerSwitch() {
        super("Power");
    }

    @Override
    public void drawToOrigin(Graphic gr) {
        super.drawToOrigin(gr);
        if (getSelected() == OffOn.On)
            gr.drawCircle(POS.sub(RAD), POS.add(RAD), Style.LED);
        gr.drawCircle(POS.sub(RAD), POS.add(RAD), Style.NORMAL);
    }
}
