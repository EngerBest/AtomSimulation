package net.engerbest.atomsimulation.model.impl;

import com.badlogic.gdx.math.Vector3;
import net.engerbest.atomsimulation.model.IAtomModel;

import static com.badlogic.gdx.math.MathUtils.*;

/**
 * Mathematical model of the Bohr atom with circular electron orbit.
 */
public class BohrAtomModel implements IAtomModel {
    // 'static' allows passing these constants into this() inside the default constructor
    private static final float DEFAULT_RADIUS = 4f;
    private static final float DEFAULT_SPEED = 2f;

    private float angle;
    private final float radius;
    // Removed 'final' so the speed can be dynamically adjusted via UI slider
    private float speed;

    // Default constructor using standard orbital parameters
    public BohrAtomModel() {
        this(DEFAULT_RADIUS, DEFAULT_SPEED);
    }

    // Constructor to initialize custom orbital properties for this atomic system
    public BohrAtomModel(float radius, float speed) {
        this.radius = radius;
        this.speed = speed;
        this.angle = 0f; // Start the electron at the beginning of the circular path
    }

    @Override
    public void activate() {
        // Reserved for future initialization logic when switching simulation modes
    }

    @Override
    public void update(float dt) {
        angle = angle + (speed * dt);
        // Check if the accumulated angle has completed a full circular rotation
        if (angle >= PI2) angle = 0f;
    }

    @Override
    public Vector3 getNucleusPosition() {
        return new Vector3(0f, 0f, 0f);
    }

    @Override
    // {int index} Reserved for future possibilities of multielectron models
    public Vector3 getElectronPosition(int index) {
        float x = radius * cos(angle);
        float y = radius * sin(angle);

        return new Vector3(x, y, 0f);
    }

    @Override
    public Vector3 getOrbitalPosition() {
        return new Vector3(0f, 0f, 0f);
    }

    public float getRadius() {
        return radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        // Clamp speed to non-negative values to prevent reverse rotation
        this.speed = Math.max(0f, speed);
    }
}
