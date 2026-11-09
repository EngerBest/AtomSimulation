package net.engerbest.atomsimulation.model.impl;

import com.badlogic.gdx.math.Vector3;
import net.engerbest.atomsimulation.model.IAtomModel;

import static com.badlogic.gdx.math.MathUtils.*;

public class BohrAtomModel implements IAtomModel {
    private float angle;
    private final float radius;
    private final float speed;

    // Constructor to initialize custom orbital properties for this atomic system
    public BohrAtomModel(float radius, float speed) {
        this.radius = radius;
        this.speed = speed;
        this.angle = 0f; // Start the electron at the beginning of the circular path
    }

    @Override
    public void activate() {}

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
    public Vector3 getElectronPosition(int index) {
        float x = radius * cos(angle);
        float y = radius * sin(angle);

        return new Vector3(x, y, 0f);
    }

    @Override
    public Vector3 getOrbitalPosition() {
        return new Vector3(0f, 0f, 0f);
    }
}
