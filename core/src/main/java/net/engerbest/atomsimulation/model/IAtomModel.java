package net.engerbest.atomsimulation.model;

import com.badlogic.gdx.math.Vector3;

/**
 * Defines the pure mathematical contract for atomic simulation models.
 */
public interface IAtomModel {
    /**
     * Called once when the mode is enabled
     */
    void activate();

    /**
     * Called every frame for mathematical calculations (dt is the time between frames)
     */
    void update(float dt);

    // Get the current calculated 3D positions of the core components
    Vector3 getNucleusPosition();
    Vector3 getElectronPosition(int index);
    Vector3 getOrbitalPosition();
}
