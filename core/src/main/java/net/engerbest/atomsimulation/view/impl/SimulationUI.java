package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.engerbest.atomsimulation.controller.SimulationController;

import static com.badlogic.gdx.Gdx.graphics;

/**
 * Encapsulates the 2D Heads-Up Display (HUD) and user interface layout.
 * Belongs strictly to the View layer in the MVC architecture.
 */
public class SimulationUI implements Disposable {
    private final SimulationController controller;

    private final Stage stage;
    private final Texture background;
    private final Skin defaultSkin;

    private final Table settingsMenuContainer;

    private final TextButton pauseButton;

    /**
     * Initializes the UI stage, generates procedural button textures, and builds the layout grid.
     */
    public SimulationUI(SimulationController controller) {
        // 1. Generate a 1x1 pixel canvas in RAM to create a solid background texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();

        // Use ScreenViewport to ensure 1:1 pixel mapping between browser mouse clicks and UI actors
        stage = new Stage(new ScreenViewport());
        this.controller = controller;

        // 2. Upload the pixel data to VRAM and immediately free the temporary RAM canvas
        background = new Texture(pixmap);
        pixmap.dispose();

        // 3. Configure a programmatic skin with the default bitmap font and stretched background
        defaultSkin = new Skin();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(background));
        defaultSkin.add("default", buttonStyle);

        // 4. Create Table and fill it for Settings Window
        Table settingsWindow = new Table();
        settingsWindow.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        settingsWindow.setTouchable(Touchable.enabled);
        // Attach an empty listener to consume mouse events and prevent camera rotation behind the open window
        settingsWindow.addListener(new ClickListener());

        // 5. Create container for Settings Window (for aligning)
        settingsMenuContainer = new Table();
        settingsMenuContainer.setFillParent(true);
        settingsMenuContainer.center();
        settingsMenuContainer.add(settingsWindow).width(400f).height(300f);
        settingsMenuContainer.setVisible(false);

        // 6. Attach the layout container to the 2D scene graph
        stage.addActor(settingsMenuContainer);

        // 7. Create the settings trigger button
        TextButton settingsButton = new TextButton("Settings", defaultSkin);
        // Delegate the click event directly to the controller without processing logic in the View
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onSettingsClicked();
            }
        });

        // 8. Create the pause trigger button
        pauseButton = new TextButton("Pause", defaultSkin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onPauseClicked();
            }
        });

        // 9. Build a responsive root table aligned to the top-right corner of the screen
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        // Optional: Enable visual lines around cells for easy layout positioning and debugging
        // rootTable.debug();

        rootTable.top().right();
        rootTable.add(settingsButton).width(140f).height(45f).pad(15f);

        rootTable.row();
        rootTable.add(pauseButton).width(140f).height(45f).pad(15f);

        // 10. Attach the root layout container to the 2D scene graph
        stage.addActor(rootTable);

        // Force initial viewport synchronization to prevent hitbox offset on browser startup
        resize(graphics.getWidth(), graphics.getHeight());
    }

    /**
     * Updates UI actor states and renders the 2D overlay.
     * @param dt Delta time in seconds since the last frame.
     */
    public void render(float dt) {
        // Synchronize menu visibility with the state managed by the Controller
        settingsMenuContainer.setVisible(controller.isSettingsOpen());

        // Update button label dynamically based on the current simulation pause state
        pauseButton.setText(controller.isPaused() ? "Resume" : "Pause");

        stage.act(dt);
        stage.draw();
    }

    /**
     * Synchronizes the UI viewport dimensions when the application window resizes.
     */
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        stage.getViewport().update(width, height, true);
    }

    /**
     * Releases unmanaged 2D graphics resources from VRAM.
     */
    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (defaultSkin != null) defaultSkin.dispose();
        if (background != null) background.dispose();
    }

    /**
     * Exposes the 2D UI stage so the parent screen can register it in the global InputMultiplexer.
     */
    public Stage getStage() {
        return this.stage;
    }
}
