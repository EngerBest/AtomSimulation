package net.engerbest.atomsimulation;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.engerbest.atomsimulation.models.BohrModel;
import net.engerbest.atomsimulation.models.SchrodingerModel;
import net.engerbest.atomsimulation.ui.SettingsButton;
import net.engerbest.atomsimulation.ui.SettingsWindow;

import static com.badlogic.gdx.Gdx.*;

public class AtomSimulation extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private ModelBatch modelBatch;
    private Stage stage;
    private SettingsButton settingsButton;
    private SettingsWindow settingsWindow;

    private SimulationMode currentMode;
    private AtomModel currentModel;

    @Override
    public void create() {
        // 1. 3D camera initialization
        // The quantum world is tiny, so we set an appropriate render distance (from 0.1 to 100 units).
        camera = new PerspectiveCamera(67, graphics.getWidth(), graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Move the camera back along the Z axis
        camera.lookAt(0, 0, 0); // Look straight at the center (at the atomic nucleus)
        camera.near = 0.1f;
        camera.far = 100f;
        camera.update();

        // 2. Controller for rotation (left mouse button), movement (right mouse button), and zooming (scroll wheel)
        cameraController = new CameraInputController(camera);

        // 3. Initializing UI
        // We MUST initialize the stage object first, otherwise it will be null!
        stage = new Stage(new ScreenViewport());

        settingsWindow = new SettingsWindow(stage);
        settingsButton = new SettingsButton(stage, settingsWindow.getWindow());

        // 4. 3D Model Batch Renderer
        modelBatch = new ModelBatch();

        // 5. Setting up the initial mode (e.g., the Bohr model)
        switchMode(SimulationMode.BOHR);

        // 6. Creating multiplexer for UI controls (cursor)
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, cameraController);
        input.setInputProcessor(inputMultiplexer);

        // Force trigger layout calculation for both 3D and UI components on startup
        resize(graphics.getWidth(), graphics.getHeight());
    }

    @Override
    public void render() {
        // Processing user input for switching modes (Keys 1 and 2)
        handleInput();

        // Updating the camera controller (smoothness of rotation)
        cameraController.update();

        // Updating the mathematical logic of the current model
        if (currentModel != null) {
            currentModel.update(graphics.getDeltaTime());
        }

        // Clear the screen (fill it with a deep space color) and clear the depth buffer for correct 3D rendering.
        gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Starting the 3D rendering process
        modelBatch.begin(camera);
        if (currentModel != null) {
            currentModel.render(modelBatch, camera);
        }
        modelBatch.end();

        stage.act();
        stage.draw();
    }

    private void handleInput() {
        if (input.isKeyJustPressed(Input.Keys.NUM_1)) {
            switchMode(SimulationMode.BOHR);
        }
        if (input.isKeyJustPressed(Input.Keys.NUM_2)) {
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
                currentModel = new BohrModel();
                break;
            case SCHRODINGER:
                currentModel = new SchrodingerModel();
                break;
        }

        if (currentModel != null) {
            currentModel.activate();
            Gdx.app.log("Simulation", "Switched to: " + currentMode.getTitle());
        }
    }

    @Override
    public void resize(int width, int height) {
        // Prevent layout crashes if GWT passes zero dimensions on the very first frame
        if (width <= 0 || height <= 0) return;

        // 1. Update the 3D camera size to match the new screen proportions
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        // 2. Update the viewport of our UI scene (passing 'true' shifts the UI camera to its default position)
        stage.getViewport().update(width, height, true);

        // 3. Update the button's position (using the new width and height)
        settingsButton.getButton().setPosition(
            width - (20 + settingsButton.getButton().getWidth()),
            height - (20 + settingsButton.getButton().getHeight())
        );

        // 4. Keep settings window centered on the screen
        settingsWindow.getWindow().setPosition(
            (width - settingsWindow.getWindow().getWidth()) / 2,
            (height - settingsWindow.getWindow().getHeight()) / 2
        );
    }

    @Override
    public void dispose() {
        // Cleaning LibGDX native memory
        modelBatch.dispose();
        if (currentModel != null) {
            currentModel.dispose();
        }

        // Cleaning UI stage and its native renderers
        stage.dispose();

        // Clean up our custom UI component resources
        if (settingsButton != null) {
            settingsButton.dispose();
        }
        if (settingsWindow != null) {
            settingsWindow.dispose();
        }
    }
}
