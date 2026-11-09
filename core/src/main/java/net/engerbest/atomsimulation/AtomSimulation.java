package net.engerbest.atomsimulation;

import com.badlogic.gdx.Game;
import net.engerbest.atomsimulation.view.impl.SimulationScreen;

/**
 * Core game application controller that delegates loop execution to the active Screen.
 */
public class AtomSimulation extends Game {
    private SimulationScreen simulationScreen;

    @Override
    public void create() {
        // Instantiate the main simulation view screen
        simulationScreen = new SimulationScreen();

        // Transfer application control directly to the new screen
        setScreen(simulationScreen);
    }

    @Override
    public void dispose() {
        // Delegate resources cleanup to the active screen context
        if (simulationScreen != null) simulationScreen.dispose();
    }
}
