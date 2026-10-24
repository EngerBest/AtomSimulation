package net.engerbest.atomsimulation;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.engerbest.atomsimulation.models.BohrModel;
import net.engerbest.atomsimulation.models.SchrodingerModel;

import static com.badlogic.gdx.Gdx.*;

public class AtomSimulation extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private ModelBatch modelBatch;
    private Stage stage;
    private BitmapFont uiFont;
    private Texture buttonTexture; // To clear memory later in dispose()
    private TextButton settingsButton;
    private Window settingsWindow;

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
        initButton();
        initWindow();

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

    private void initButton() {
        stage = new Stage(new ScreenViewport());

        // 1. Create a font and a background pixel texture
        uiFont = new BitmapFont();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        buttonTexture = new Texture(pixmap);
        pixmap.dispose(); // We delete this temporary object immediately

        // 2. Configure the style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = uiFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(buttonTexture);

        // 3. Create a button and ADD it to the scene
        settingsButton = new TextButton("Settings", buttonStyle);

        // Temporary step: set a size for the button so that it isn't 1x1 pixels in size
        settingsButton.setSize(120, 40);
        settingsButton.setPosition(0, 0);

        stage.addActor(settingsButton); // This is the critically important line!
    }

    private void initWindow() {
        // 1. Create a style for the window (using the same font and texture)
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = uiFont;
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = new TextureRegionDrawable(buttonTexture); // temporary gray background

        // 2. Initialize the window with a title and style
        settingsWindow = new Window("Settings", windowStyle);
        settingsWindow.setSize(600, 800);
        settingsWindow.setPosition(0, 0);

        // 4. Hide the window by default
        settingsWindow.setVisible(false);

        // 5. Add the window to the stage
        stage.addActor(settingsWindow);

        // 6. Create listener for registering clicks on button
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Toggle window visibility: if it's true, make it false
                settingsWindow.setVisible(!settingsWindow.isVisible());
            }
        });
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
        // 1. Update the 3D camera size to match the new screen proportions
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        // 2. Update the viewport of our UI scene (passing 'true' shifts the UI camera to its default position)
        stage.getViewport().update(width, height, true);

        // 3. Update the button's position (using the new width and height)
        settingsButton.setPosition(
            width - (20 + settingsButton.getWidth()),
            height - (20 + settingsButton.getHeight())
        );

        // 4. Keep settings window centered on the screen
        settingsWindow.setPosition(
            (width - settingsWindow.getWidth()) / 2,
            (height - settingsWindow.getHeight()) / 2
        );
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
