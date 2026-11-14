package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.engerbest.atomsimulation.controller.SimulationController;
import net.engerbest.atomsimulation.view.impl.ui.HudControlsPanel;
import net.engerbest.atomsimulation.view.impl.ui.SettingsWindow;
import net.engerbest.atomsimulation.view.impl.ui.SimulationSkinFactory;

import static com.badlogic.gdx.Gdx.graphics;

/**
 * Encapsulates the 2D Heads-Up Display (HUD) and user interface layout.
 * Belongs strictly to the View layer in the MVC architecture.
 */
public class SimulationUI implements Disposable {
    private final SimulationController controller;
    private final SimulationSkinFactory skinFactory;
    private final HudControlsPanel hudPanel;

    private final Table settingsMenuContainer;

    private final Stage stage;

    /**
     * Initializes the UI stage, creates the programmatic skin, and builds the HUD layout.
     */
    public SimulationUI(SimulationController controller) {
        this.controller = controller;

        // Use ScreenViewport to ensure 1:1 pixel mapping between browser mouse clicks and UI actors
        stage = new Stage(new ScreenViewport());
        skinFactory = new SimulationSkinFactory();
        hudPanel = new HudControlsPanel(controller, skinFactory.getSkin());

        // Create container for Settings Window (for aligning)
        settingsMenuContainer = new Table();
        settingsMenuContainer.setFillParent(true);
        settingsMenuContainer.center();
        settingsMenuContainer.add(new SettingsWindow(controller, skinFactory.getSkin())).width(400f).height(300f);
        settingsMenuContainer.setVisible(false);

        // Attach the root layout container to the 2D scene graph
        stage.addActor(settingsMenuContainer);
        stage.addActor(hudPanel);

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

        hudPanel.update();

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
        if (skinFactory != null) skinFactory.dispose();
    }

    /**
     * Exposes the 2D UI stage so the parent screen can register it in the global InputMultiplexer.
     */
    public Stage getStage() {
        return this.stage;
    }
}
