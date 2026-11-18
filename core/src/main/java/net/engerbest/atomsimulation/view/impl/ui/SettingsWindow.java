package net.engerbest.atomsimulation.view.impl.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.engerbest.atomsimulation.controller.SimulationController;

public class SettingsWindow extends Table {
    public SettingsWindow(SimulationController controller, Skin skin) {
        // Configure the modal settings window background and input blocking
        setBackground(skin.newDrawable("white", Color.LIGHT_GRAY));
        setTouchable(Touchable.enabled);
        // Attach an empty listener to consume mouse events and prevent camera rotation behind the open window
        addListener(new ClickListener());

        // Create label for speed slider with current speed
        float initialSpeed = Math.round(controller.getElectronSpeed() * 10f) / 10f;
        final Label speedLabel = new Label("Electron Speed: " + initialSpeed, skin);

        // Create a horizontal slider for electron speed (range: 0.0 to 10.0, step: 0.1)
        final Slider speedSlider = new Slider(0f, 10f, 0.1f, false, skin);
        // Sync initial knob position with the current model speed
        speedSlider.setValue(controller.getElectronSpeed());
        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Notify controller whenever the user drags the slider
                controller.onSpeedChanged(speedSlider.getValue());

                float roundedSpeed = Math.round(speedSlider.getValue() * 10f) / 10f;
                speedLabel.setText("Electron Speed: " + roundedSpeed);
            }
        });

        // Add label and slider to the Table
        add(speedLabel).padBottom(10f).row();
        add(speedSlider).width(250f).padBottom(20f).row();
    }
}
