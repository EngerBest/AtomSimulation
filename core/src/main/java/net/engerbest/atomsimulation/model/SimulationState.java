package net.engerbest.atomsimulation.model;

public class SimulationState {
    private float animationTime = 0f;
    private boolean isPaused = false;

    /**
     * Advances the internal simulation clock by the specified delta.
     * This is a pure Java method completely independent of LibGDX.
     * @param dt The time elapsed since the last frame in seconds.
     */
    public void advanceTime(float dt) {
        // If simulation is paused, do not increment the clock
        if (isPaused) return;

        // Simply add the fraction of a second to pure mathematical timer
        this.animationTime += dt;
    }

    public float getAnimationTime() {
        return this.animationTime;
    }
}
