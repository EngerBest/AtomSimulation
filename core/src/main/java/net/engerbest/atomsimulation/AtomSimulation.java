package net.engerbest.atomsimulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import net.engerbest.atomsimulation.model.SimulationState;
import net.engerbest.atomsimulation.model.impl.BohrAtomModel;
import net.engerbest.atomsimulation.view.AtomPartsFactory;
import net.engerbest.atomsimulation.view.IAtomRenderer;
import net.engerbest.atomsimulation.view.impl.BohrAtomRenderer;

import static com.badlogic.gdx.Gdx.*;

public class AtomSimulation extends ApplicationAdapter {
    // Declare the specific instance variable for pure mathematical model
    private SimulationState simulationState;

    private BohrAtomModel bohrModel;
    private AtomPartsFactory partsFactory;
    private IAtomRenderer atomRenderer;

    private PerspectiveCamera camera;
    private ModelBatch batch;

    private final float orbitRadius = 4f;

    @Override
    public void create() {
        // Instantiate the application state container
        simulationState = new SimulationState();

        bohrModel = new BohrAtomModel(orbitRadius, 2f);
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
    public void render() {
        // 1. Get the hardware delta time from LibGDX
        float dt = graphics.getDeltaTime();

        // 2. Safely pass the hardware delta clock into our instance variable
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
    }

    @Override
    public void dispose() {
        // Clean up unmanaged graphics resources to prevent memory leaks
        if (batch != null) batch.dispose();
        if (atomRenderer != null) atomRenderer.dispose();
    }
}
