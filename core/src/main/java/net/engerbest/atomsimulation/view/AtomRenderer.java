package net.engerbest.atomsimulation.view;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface AtomRenderer {
    /**
     * Called every frame to render 3D objects
     * @param batch This is a group of 3D models that need to be rendered.
     */
    void render(ModelBatch batch);

    /**
     * Memory deallocation upon destruction or mode change
     */
    void dispose();
}
