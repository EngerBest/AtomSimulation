package net.engerbest.atomsimulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.ScreenUtils;
import net.engerbest.atomsimulation.models.BohrModel;
import net.engerbest.atomsimulation.models.SchrodingerModel;

public class AtomSimulation extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private ModelBatch modelBatch;

    private SimulationMode currentMode;
    private AtomModel currentModel;

    @Override
    public void create() {
        // 1. 3D camera initialization
        // The quantum world is tiny, so we set an appropriate render distance (from 0.1 to 100 units).
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Move the camera back along the Z axis
        camera.lookAt(0, 0, 0); // Look straight at the center (at the atomic nucleus)
        camera.near = 0.1f;
        camera.far = 100f;
        camera.update();

        // 2. Controller for rotation (left mouse button), movement (right mouse button), and zooming (scroll wheel)
        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        // 3. 3D Model Batch Renderer
        modelBatch = new ModelBatch();

        // 4. Setting up the initial regime (e.g., the Bohr model)
        switchMode(SimulationMode.BOHR);
    }

    @Override
    public void render() {
        // Processing user input for switching modes (Keys 1 and 2)
        handleInput();

        // Updating the camera controller (smoothness of rotation)
        cameraController.update();

        // Updating the mathematical logic of the current model
        if (currentModel != null) {
            currentModel.update(Gdx.graphics.getDeltaTime());
        }

        // Clear the screen (fill it with a deep space color) and clear the depth buffer for correct 3D rendering.
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Starting the 3D rendering process
        modelBatch.begin(camera);
        if (currentModel != null) {
            currentModel.render(modelBatch, camera);
        }
        modelBatch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            switchMode(SimulationMode.BOHR);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            switchMode(SimulationMode.SCHRODINGER);
        }
    }

    public void switchMode(SimulationMode newMode) {
        if (currentMode == newMode) return;

        // Freeing up resources of the old model before switching
        if (currentModel != null) {
            currentModel.dispose();
        }

        currentMode = newMode;

        // The State pattern in action: swapping logic on the fly
        switch (currentMode) {
            case BOHR:
                // We are passing a stub; we will create the classes in the next step.
                currentModel = new BohrModel();
                break;
            case SCHRODINGER:
                currentModel = new SchrodingerModel();
                break;
        }

        if (currentModel != null) {
            currentModel.activate();
            Gdx.app.log("Simulation", "Переключено на: " + currentMode.getTitle());
        }
    }

    @Override
    public void resize(int width, int height) {
        // Correctly update camera proportions when the browser window is resized
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        // Cleaning LibGDX native memory
        modelBatch.dispose();
        if (currentModel != null) {
            currentModel.dispose();
        }
    }
}
