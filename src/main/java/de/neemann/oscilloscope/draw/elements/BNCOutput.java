package de.neemann.oscilloscope.draw.elements;

import de.neemann.oscilloscope.signal.PeriodicSignal;
import de.neemann.oscilloscope.signal.SignalProvider;

/**
 * A BNC output connector
 */
public class BNCOutput extends BNC<BNCOutput> {
    private final SignalProvider signalProvider;

    /**
     * Creates an input.
     *
     * @param name           the name of the input
     * @param signalProvider the provider connected to this output
     */
    public BNCOutput(String name, SignalProvider signalProvider) {
        super(name);
        this.signalProvider = signalProvider;
    }

    /**
     * @return the signal generated by this output
     */
    public SignalProvider getOutputProvider() {
        return signalProvider;
    }

    /**
     * @return the signal provided by this connector
     */
    public PeriodicSignal getSignal() {
        return signalProvider.getSignal();
    }
}
