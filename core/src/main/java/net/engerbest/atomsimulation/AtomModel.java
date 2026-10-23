package net.engerbest.atomsimulation;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface AtomModel {
    // Called once when the mode is enabled
    void activate();

    // Called every frame for mathematical calculations (delta is the time between frames)
    void update(float delta);

    // Called every frame to render 3D objects
    void render(ModelBatch modelBatch, PerspectiveCamera camera);

    // Memory deallocation upon destruction or mode change
    void dispose();
}
