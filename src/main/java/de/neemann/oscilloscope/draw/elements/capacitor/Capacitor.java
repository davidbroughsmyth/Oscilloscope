package de.neemann.oscilloscope.draw.elements.capacitor;

import de.neemann.oscilloscope.draw.elements.BNCInput;
import de.neemann.oscilloscope.draw.elements.BNCOutput;
import de.neemann.oscilloscope.draw.elements.Container;
import de.neemann.oscilloscope.draw.graphics.Graphic;
import de.neemann.oscilloscope.draw.graphics.Polygon;
import de.neemann.oscilloscope.draw.graphics.Style;
import de.neemann.oscilloscope.draw.graphics.Vector;

import java.awt.*;

import static de.neemann.oscilloscope.draw.elements.Switch.SIZE;
import static de.neemann.oscilloscope.draw.elements.Switch.SIZE2;

/**
 * The capacitor experimental setup
 */
public class Capacitor extends Container<Capacitor> {

    private final BNCInput input;
    private final BNCOutput uc;
    private final BNCOutput ur;

    /**
     * Creates a new capacitor setup
     */
    public Capacitor() {
        super(SIZE * 10, SIZE * 10);

        CapacitorModel m = new CapacitorModel();

        input = new BNCInput("");
        uc = new BNCOutput("");
        ur = new BNCOutput("");

        add(input.setPos(SIZE, SIZE * 5).setInputSetter(m::setInput));
        add(uc.setPos(SIZE * 9, SIZE).setOutput(m::getVoltageCapacitor));
        add(ur.setPos(SIZE * 9, SIZE * 9).setOutput(m::getVoltageResistor));

        setBackground(Color.WHITE);
    }

    /**
     * @return the input connector
     */
    public BNCInput getInput() {
        return input;
    }

    /**
     * @return the resistance voltage connector
     */
    public BNCOutput getVoltRes() {
        return ur;
    }

    /**
     * @return the diode voltage connector
     */
    public BNCOutput getVoltCapacitor() {
        return uc;
    }

    @Override
    public void drawToOrigin(Graphic gr) {
        super.drawToOrigin(gr);

        int pad = 5;
        gr.drawPolygon(new Polygon(false)
                .add(SIZE, SIZE * 5)
                .add(SIZE, SIZE)
                .add(SIZE * 5, SIZE)
                .add(SIZE * 5, SIZE * 2 + pad), Style.PRINT);
        gr.drawPolygon(new Polygon(false)
                .add(SIZE * 5, SIZE * 8)
                .add(SIZE * 5, SIZE * 9)
                .add(SIZE, SIZE * 9)
                .add(SIZE, SIZE * 6), Style.PRINT);

        gr.drawPolygon(new Polygon(true)
                .add(SIZE * 5 - SIZE2, SIZE * 5)
                .add(SIZE * 5 + SIZE2, SIZE * 5)
                .add(SIZE * 5 + SIZE2, SIZE * 8)
                .add(SIZE * 5 - SIZE2, SIZE * 8), Style.PRINT);

        gr.drawLine(new Vector(SIZE * 5 - SIZE, SIZE * 3 - pad), new Vector(SIZE * 5 + SIZE, SIZE * 3 - pad), Style.PRINT);
        gr.drawLine(new Vector(SIZE * 5 - SIZE, SIZE * 2 + pad), new Vector(SIZE * 5 + SIZE, SIZE * 2 + pad), Style.PRINT);

        gr.drawLine(new Vector(SIZE * 5, SIZE * 3 - pad), new Vector(SIZE * 5, SIZE * 5), Style.PRINT);


        gr.drawLine(new Vector(SIZE * 5, SIZE * 4), new Vector(SIZE * 9, SIZE * 4), Style.PRINT);
        gr.drawLine(new Vector(SIZE * 9, SIZE * 2), new Vector(SIZE * 9, SIZE * 8), Style.PRINT);


        gr.drawLine(new Vector(SIZE * 5, SIZE), new Vector(SIZE * 9, SIZE), Style.PRINT);
        gr.drawLine(new Vector(SIZE * 5, SIZE * 9), new Vector(SIZE * 9, SIZE * 9), Style.PRINT);

        dot(gr, SIZE * 5, SIZE);
        dot(gr, SIZE * 5, SIZE * 4);
        dot(gr, SIZE * 9, SIZE * 4);
        dot(gr, SIZE * 5, SIZE * 9);
    }

    private static final int RAD = 3;

    private void dot(Graphic gr, int x, int y) {
        gr.drawCircle(new Vector(x - RAD, y - RAD), new Vector(x + RAD, y + RAD), Style.PRINT);
    }
}