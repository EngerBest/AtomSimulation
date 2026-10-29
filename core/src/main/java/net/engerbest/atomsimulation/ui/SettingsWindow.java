package net.engerbest.atomsimulation.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SettingsWindow {
    private final Window window;
    private final BitmapFont font;
    private final Texture texture;

    public SettingsWindow(Stage stage) {
        // 1. Create a font and a background pixel texture
        font = new BitmapFont();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();

        // 2. Configure the style
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = new TextureRegionDrawable(texture); // temporary gray background

        // 3. Initialize the window with a title and style
        window = new Window("Settings", windowStyle);
        window.setSize(600, 800);

        // 4. Hide the window by default
        window.setVisible(false);

        // 5. Add the window to the stage
        stage.addActor(window);
    }

    // Getter to allow AtomSimulation to position or access the window
    public Window getWindow() {
        return window;
    }

    // Public dispose method to clean up resources from memory
    public void dispose() {
        font.dispose();
        texture.dispose();
    }
}
