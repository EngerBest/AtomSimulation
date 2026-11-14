package net.engerbest.atomsimulation.view.impl.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public class SimulationSkinFactory implements Disposable {
    private final Texture background;
    private final Skin defaultSkin;

    public SimulationSkinFactory() {
        // Generate a 1x1 pixel canvas in RAM to create a solid background texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        // Upload pixel data to VRAM and dispose temporary RAM canvas
        background = new Texture(pixmap);
        pixmap.dispose();

        // Configure styles inside defaultSkin
        defaultSkin = new Skin();

        defaultSkin.add("white", background);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont();
        buttonStyle.up = defaultSkin.newDrawable("white", Color.LIGHT_GRAY);
        defaultSkin.add("default", buttonStyle);
        defaultSkin.add("default-font", buttonStyle.font);

        // Configure procedural style for the speed slider using the 1x1 white pixel
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Drawable track = defaultSkin.newDrawable("white", Color.DARK_GRAY);
        track.setMinHeight(6f); // Thickness of the horizontal track line
        Drawable knob = defaultSkin.newDrawable("white", Color.ROYAL);
        knob.setMinWidth(16f); // Width of the draggable handle
        knob.setMinHeight(24f); // Height of the draggable handle
        sliderStyle.background = track;
        sliderStyle.knob = knob;
        // Register the style as "default-horizontal" so new Slider(..., false, skin) finds it automatically
        defaultSkin.add("default-horizontal", sliderStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = buttonStyle.font;
        labelStyle.fontColor = Color.BLACK;
        defaultSkin.add("default", labelStyle);
    }

    public Skin getSkin() {
        return defaultSkin;
    }

    @Override
    public void dispose() {
        if (defaultSkin != null) defaultSkin.dispose();
        if (background != null) background.dispose();
    }
}
