package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * Encapsulates the 2D Heads-Up Display (HUD) and user interface layout.
 * Belongs strictly to the View layer in the MVC architecture.
 */
public class SimulationUI implements Disposable {
    private final Stage stage;
    private final Texture buttonBG;
    private final Skin defaultSkin;

    /**
     * Initializes the UI stage, generates procedural button textures, and builds the layout grid.
     */
    public SimulationUI() {
        // 1. Generate a 1x1 pixel canvas in RAM to create a solid background texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();

        stage = new Stage();

        // 2. Upload the pixel data to VRAM and immediately free the temporary RAM canvas
        buttonBG = new Texture(pixmap);
        pixmap.dispose();

        // 3. Configure a programmatic skin with the default bitmap font and stretched background
        defaultSkin = new Skin();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonBG));
        defaultSkin.add("default", buttonStyle);

        // 4. Create the settings trigger button
        TextButton settingsButton = new TextButton("Settings", defaultSkin);

        // 5. Build a responsive root table aligned to the top-right corner of the screen
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        // Optional: Enable visual lines around cells for easy layout positioning and debugging
        // rootTable.debug();
        rootTable.top().right();
        rootTable.add(settingsButton).width(140f).height(45f).pad(15f);

        // 6. Attach the root layout container to the 2D scene graph
        stage.addActor(rootTable);
    }

    /**
     * Updates UI actor states and renders the 2D overlay.
     * @param dt Delta time in seconds since the last frame.
     */
    public void render(float dt) {
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
        if (buttonBG != null) buttonBG.dispose();
    }
}
