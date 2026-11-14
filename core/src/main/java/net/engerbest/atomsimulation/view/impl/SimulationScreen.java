package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import net.engerbest.atomsimulation.controller.SimulationController;
import net.engerbest.atomsimulation.view.AtomPartsFactory;
import net.engerbest.atomsimulation.view.IAtomRenderer;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;

/**
 * Handles the main execution logic, asset rendering, and math updates for the atom simulation.
 */
public class SimulationScreen implements Screen {
    // Orchestrates simulation logic and mediates between Model and View
    private SimulationController simulationController;
    private SimulationUI simulationUI;

    private AtomPartsFactory partsFactory;
    private IAtomRenderer atomRenderer;

    private PerspectiveCamera camera;
    private ModelBatch batch;

    private final float orbitRadius = 4f;
    private final float rotationSpeed = 2f;

    @Override
    public void show() {
        // Instantiate the application state container
        simulationController = new SimulationController(orbitRadius, rotationSpeed);
        simulationUI = new SimulationUI();

        partsFactory = new AtomPartsFactory(orbitRadius);
        atomRenderer = new BohrAtomRenderer(partsFactory, simulationController.getBohrModel());

        batch = new ModelBatch();

        // The quantum world is tiny, so we set an appropriate render distance (from 0.1 to 100 units).
        camera = new PerspectiveCamera(67, graphics.getWidth(), graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Move the camera back by 10 units along the Z-axis
        camera.lookAt(0f, 0f, 0f); // Make camera look strictly toward the center of the scene
        camera.near = 1f;
        camera.far = 300f;
    }

    @Override
    public void render(float dt) {
        simulationController.update(dt);

        // Clear the screen (fill it with a deep space color) and clear the depth buffer for correct 3D rendering.
        gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Update the camera matrix so it knows where to look
        camera.update();

        // Starting 3D rendering
        batch.begin(camera);

        // Let’s kick our renderer into action! It will reach into bohrModel on its own,
        // grab the new coordinates, move the spheres, and draw them.
        atomRenderer.render(batch);

        batch.end();

        // Update UI actor logic and draw the 2D overlay on top of the 3D scene
        simulationUI.render(dt);
    }

    @Override
    public void resize(int width, int height) {
        simulationUI.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Clean up unmanaged graphics resources to prevent memory leaks
        if (batch != null) batch.dispose();
        if (atomRenderer != null) atomRenderer.dispose();
        if (simulationUI != null) simulationUI.dispose();
    }
}
