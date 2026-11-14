package net.engerbest.atomsimulation.controller;

import net.engerbest.atomsimulation.model.SimulationState;
import net.engerbest.atomsimulation.model.impl.BohrAtomModel;

/**
 * Mediates between the simulation models and the visual presentation layer.
 * Controls simulation lifecycle updates and handles user actions.
 */
public class SimulationController {
    private final SimulationState simulationState;
    private final BohrAtomModel bohrModel;

    public SimulationController(float orbitRadius, float rotationSpeed) {
        simulationState = new SimulationState();
        bohrModel = new BohrAtomModel(orbitRadius, rotationSpeed);
    }

    /**
     * Advances the internal simulation state and mathematical atom calculations.
     * @param dt Delta time in seconds since the last frame.
     */
    public void update(float dt) {
        // Advance the master simulation timer
        simulationState.advanceTime(dt);

        // Only update electron physics if the simulation is not paused
        if (!simulationState.isPaused()) {
            bohrModel.update(dt);
        }
    }

    public BohrAtomModel getBohrModel() {
        return bohrModel;
    }

    public boolean isSettingsOpen() {
        return simulationState.isSettingsOpen();
    }

    public boolean isPaused() {
        return simulationState.isPaused();
    }

    public void onSettingsClicked() {
        simulationState.toggleSettings();
    }

    public void onPauseClicked() {
        simulationState.togglePause();
    }
}
