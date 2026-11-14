package net.engerbest.atomsimulation.model;

/**
 * Stores pure application states, flags, and the simulation clock.
 */
public class SimulationState {
    private float animationTime = 0f;
    private boolean isPaused = false;

    private boolean isSettingsOpen = false;

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

    /**
     * Toggles the pause state of the simulation.
     */
    public void togglePause() {
        this.isPaused = !this.isPaused;
    }

    /**
     * Toggles the visibility of Settings Window of the simulation.
     */
    public void toggleSettings() {
        this.isSettingsOpen = !this.isSettingsOpen;
    }

    /**
     * Returns whether the simulation is currently paused.
     */
    public boolean isPaused() {
        return this.isPaused;
    }

    /**
     * Returns whether the Settings Window is currently shown.
     */
    public boolean isSettingsOpen() {
        return this.isSettingsOpen;
    }
}
