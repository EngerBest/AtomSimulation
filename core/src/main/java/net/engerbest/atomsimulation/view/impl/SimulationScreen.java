package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.engerbest.atomsimulation.model.SimulationState;
import net.engerbest.atomsimulation.model.impl.BohrAtomModel;
import net.engerbest.atomsimulation.view.AtomPartsFactory;
import net.engerbest.atomsimulation.view.IAtomRenderer;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;

/**
 * Handles the main execution logic, asset rendering, and math updates for the atom simulation.
 */
public class SimulationScreen implements Screen {
    // Declare the specific instance variable for pure mathematical model
    private SimulationState simulationState;
    private Stage stage; // Core 2D scene graph container for UI elements and layouts
    private Table rootTable; // Root layout container that organizes UI elements into a responsive grid

    private BohrAtomModel bohrModel;
    private AtomPartsFactory partsFactory;
    private IAtomRenderer atomRenderer;

    private PerspectiveCamera camera;
    private ModelBatch batch;

    private final float orbitRadius = 4f;
    private final float rotationSpeed = 2f;

    @Override
    public void show() {
        // Instantiate the application state container
        simulationState = new SimulationState();

        stage = new Stage();

        // Instantiate the root table layout
        rootTable = new Table();

        // Force the table to automatically match and follow the exact dimensions of the parent Stage
        rootTable.setFillParent(true);

        // Optional: Enable visual lines around cells for easy layout positioning and debugging
        // rootTable.debug();

        stage.addActor(rootTable);

        bohrModel = new BohrAtomModel(orbitRadius, rotationSpeed);
        partsFactory = new AtomPartsFactory(orbitRadius);
        atomRenderer = new BohrAtomRenderer(partsFactory, bohrModel);

        batch = new ModelBatch();

        camera = new PerspectiveCamera(67, graphics.getWidth(), graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Move the camera back by 10 units along the Z-axis
        camera.lookAt(0f, 0f, 0f); // Make camera look strictly toward the center of the scene
        camera.near = 1f;
        camera.far = 300f;
    }

    @Override
    public void render(float dt) {
        // Safely pass the hardware delta clock into our instance variable
        simulationState.advanceTime(dt);
        bohrModel.update(dt);

        // Clear the screen to prevent ghosting
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
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Guardrail to prevent WebGL/Desktop runtime mathematical layout errors on 0x0 frames
        if (width <= 0 || height <= 0) return;

        // Synchronize the 2D UI viewport scales with the newly updated window boundaries
        stage.getViewport().update(width, height, true);
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
        if (stage != null) stage.dispose();
    }
}
