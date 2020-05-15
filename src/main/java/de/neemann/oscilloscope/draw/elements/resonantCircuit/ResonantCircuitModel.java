package de.neemann.oscilloscope.draw.elements.resonantCircuit;

import de.neemann.oscilloscope.draw.elements.OnOffSwitch;
import de.neemann.oscilloscope.gui.Observer;
import de.neemann.oscilloscope.signal.InterpolateLinear;
import de.neemann.oscilloscope.signal.PeriodicSignal;
import de.neemann.oscilloscope.signal.SignalProvider;
import de.neemann.oscilloscope.signal.primitives.Sine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The model of the capacitor
 */
public class ResonantCircuitModel implements Observer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResonantCircuitModel.class);

    private static final int POINTS = 10000;

    // parasitic resistance of the inductor
    private static final double RL = 50;

    private final SignalProvider resistorVoltageSignal;
    private final SignalProvider input;
    private double resistor;
    private double capacitor;
    private double inductor;
    private OnOffSwitch debugSwitch;

    /**
     * Creates a new diode model
     *
     * @param input the input signal
     */
    public ResonantCircuitModel(SignalProvider input) {
        this.input = input;
        input.addObserver(this);
        resistorVoltageSignal = new SignalProvider();
    }

    /**
     * Sets the debug switch
     *
     * @param debugSwitch the debug switch
     * @return the switch
     */
    public OnOffSwitch setDebugSwitch(OnOffSwitch debugSwitch) {
        this.debugSwitch = debugSwitch;
        debugSwitch.addObserver(this);
        return debugSwitch;
    }

    /**
     * @return the signal describing the resistance voltage
     */
    public SignalProvider getVoltageResistor() {
        return resistorVoltageSignal;
    }

    @Override
    public void hasChanged() {
        inputSignalHasChanged();
    }

    private void inputSignalHasChanged() {
        PeriodicSignal in = input.getSignal();
        if (in instanceof Sine) {
            if (debugSwitch == null || debugSwitch.isOff())
                resistorVoltageSignal.setSignal(createSines((Sine) in));
            else
                resistorVoltageSignal.setSignal(solveDGL(in));
        } else {
            resistorVoltageSignal.setSignal(solveDGL(in));
        }
    }

    private PeriodicSignal solveDGL(PeriodicSignal input) {
        double t = Math.sqrt(inductor * capacitor) * 2 * Math.PI;
        LOGGER.info("recalculate resonant circuit, f0=" + 1 / t + "Hz");


        double period = input.period();
        int points = (int) (period / t * 1000);
        if (points < POINTS)
            points = POINTS;
        else if (points > 10000)
            points = 10000;

        double[] resistorVoltage = new double[points];
        double dt = period / points;
        double didt = 0;
        double i = 0;
        double lastUGes = 0;
        for (int l = 0; l < 8; l++) {
            for (int j = 0; j < points; j++) {
                double uGes = input.v(period * j / points);

                resistorVoltage[j] = -i * resistor;

                double d2idt2 = -(resistor + RL) / inductor * didt - i / inductor / capacitor - (uGes - lastUGes) / dt / inductor;
                i += didt * dt;
                didt += d2idt2 * dt;

                lastUGes = uGes;
            }
        }
        return new InterpolateLinear(period, resistorVoltage);
    }

    /**
     * Sets the used resistor
     *
     * @param res the resistor
     */
    public void setResistor(int res) {
        this.resistor = res;
        inputSignalHasChanged();
    }

    /**
     * Sets the used capacitor
     *
     * @param cap the capacitor in nF
     */
    public void setCapacitor(int cap) {
        this.capacitor = cap * 1e-9;
        inputSignalHasChanged();
    }

    /**
     * Sets the used capacitor
     *
     * @param ind the capacitor in mH
     */
    public void setInductor(int ind) {
        this.inductor = ind * 1e-3;
        inputSignalHasChanged();
    }

    private PeriodicSignal createSines(Sine sine) {
        LOGGER.info("create sine");
        double w = sine.getOmega();
        double a = sine.getAmplitude() * (resistor + RL) / Math.sqrt(sqr(resistor + RL) + sqr(w * inductor - 1 / (w * capacitor)));
        double ampl = a * resistor / (resistor + RL);

        double phi = Math.atan((w * inductor - 1 / (w * capacitor)) / (resistor + RL));

        double phase = sine.getPhase() - phi;

        return new Sine(ampl, w, phase, 0);
    }

    private static double sqr(double v) {
        return v * v;
    }

}
