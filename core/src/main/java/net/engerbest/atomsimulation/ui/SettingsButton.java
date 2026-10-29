package net.engerbest.atomsimulation.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SettingsButton {
    // fields are now private and contained inside this class context
    private final TextButton button;
    private final BitmapFont font;
    private final Texture texture;

    // Constructor receives the Stage from AtomSimulation
    public SettingsButton(Stage stage, Window window) {
        // 1. Create a font and a background pixel texture
        font = new BitmapFont();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();

        // 2. Configure the style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new TextureRegionDrawable(texture);

        // 3. Create the button instance
        button = new TextButton("Settings", buttonStyle);
        button.setSize(120, 40);

        // 4. Add it to the passed stage
        stage.addActor(button);

        // 5. Create listener for registering clicks on button
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Toggle window visibility: if it's true, make it false
                window.setVisible(!window.isVisible());
            }
        });
    }

    // Getter to allow AtomSimulation to position or access the button
    public TextButton getButton() {
        return button;
    }

    // Public dispose method to clean up resources from memory
    public void dispose() {
        font.dispose();
        texture.dispose();
    }
}
