package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

    private TextButton settingsButton;
    private Skin defaultSkin;
    private Texture buttonBG;

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

        // The quantum world is tiny, so we set an appropriate render distance (from 0.1 to 100 units).
        camera = new PerspectiveCamera(67, graphics.getWidth(), graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Move the camera back by 10 units along the Z-axis
        camera.lookAt(0f, 0f, 0f); // Make camera look strictly toward the center of the scene
        camera.near = 1f;
        camera.far = 300f;

        // Settings button creation
        // 1. Create a minimal programmatic skin for the text button
        defaultSkin = new Skin();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(); // Uses the default built-in LibGDX white font

        // Create background texture for button
        // 1. Allocate a 1x1 pixel canvas in RAM and fill it with dark gray color
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();

        // 2. Upload pixel data to VRAM as a Texture and free temporary RAM canvas
        buttonBG = new Texture(pixmap);
        pixmap.dispose();

        // 3. Assign the stretched texture as the default background for the button's normal state
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonBG));

        defaultSkin.add("default", buttonStyle);

        // 2. Instantiate the button with our text and generated skin
        settingsButton = new TextButton("Settings", defaultSkin);

        // 3. Align the entire table container contents to the top-right corner of the screen
        rootTable.top().right();

        // 4. Insert the button into the grid, add 15 pixels of protective outer margins (padding)
        rootTable.add(settingsButton).width(140f).height(45f).pad(15f);
    }

    @Override
    public void render(float dt) {
        // Safely pass the hardware delta clock into our instance variable
        simulationState.advanceTime(dt);
        bohrModel.update(dt);

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
        if (defaultSkin != null) defaultSkin.dispose();
        if (buttonBG != null) buttonBG.dispose();
    }
}
