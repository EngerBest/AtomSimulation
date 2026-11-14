package net.engerbest.atomsimulation.view.impl.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.engerbest.atomsimulation.controller.SimulationController;

public class HudControlsPanel extends Table {
    private final TextButton pauseButton;

    private final SimulationController controller;

    public HudControlsPanel(SimulationController controller, Skin skin) {
        this.controller = controller;

        // Create the settings trigger button
        TextButton settingsButton = new TextButton("Settings", skin);
        // Delegate the click event directly to the controller without processing logic in the View
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onSettingsClicked();
            }
        });

        // Create the pause trigger button
        pauseButton = new TextButton("Pause", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onPauseClicked();
            }
        });

        // Configure this HUD panel aligned to the top-right corner of the screen
        setFillParent(true);
        // Optional: Enable visual lines around cells for easy layout positioning and debugging
        // debug();

        top().right();
        add(settingsButton).width(140f).height(45f).pad(15f);
        row();
        add(pauseButton).width(140f).height(45f).pad(15f);
    }

    public void update() {
        // Update button label dynamically based on the current simulation pause state
        pauseButton.setText(controller.isPaused() ? "Resume" : "Pause");
    }
}
