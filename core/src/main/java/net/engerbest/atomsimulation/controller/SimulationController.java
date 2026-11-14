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
        // Safely pass the hardware delta clock into our instance variable
        simulationState.advanceTime(dt);
        bohrModel.update(dt);
    }

    public BohrAtomModel getBohrModel() {
        return bohrModel;
    }
}
