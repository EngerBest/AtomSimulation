package net.engerbest.atomsimulation.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import net.engerbest.atomsimulation.AtomModel;

public class BohrModel implements AtomModel {
    // 1. Blueprints (Model) — loaded into memory once
    private Model nucleusModel; // Core blueprint
    private Model electronModel; // Blueprint of an electron
    private Model orbitModel; // Blueprint of the orbit path

    // 2. Instances in the scene (ModelInstance) — store coordinates in space
    private ModelInstance nucleusInstance; // Core on scene
    private ModelInstance electronInstance; // Electron on scene
    private ModelInstance orbitInstance; // Orbit on scene

    // 3. Variables for movement trigonometry
    private float animationTime = 0f; // Time variable (our angle alpha)
    private final float orbitRadius = 4f; // Orbital radius
    private final float rotationSpeed = 2f; // Rotation speed

    @Override
    public void activate() {
        ModelBuilder modelBuilder = new ModelBuilder();

        nucleusModel = modelBuilder.createSphere(
            1.5f, 1.5f, 1.5f, // Dimensions along the X, Y, and Z axes (sphere diameter)
            24, 24, // Polygon mesh (the more there are, the rounder the sphere)
            new Material(ColorAttribute.createDiffuse(Color.RED)), // Material and color (Red)
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal // Instructions for the graphics card
        );
        electronModel = modelBuilder.createSphere(
            0.4f, 0.4f, 0.4f, // Dimensions along the X, Y, and Z axes (sphere diameter)
            16, 16, // Polygon mesh (the more there are, the rounder the sphere)
            new Material(ColorAttribute.createDiffuse(Color.CYAN)), // Material and color (Red)
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal // Instructions for the graphics card
        );

        // Create an instance based on a blueprint
        nucleusInstance = new ModelInstance(nucleusModel);
        electronInstance = new ModelInstance(electronModel);
        // Place it exactly at the center of the scene (coordinates 0, 0, 0)
        nucleusInstance.transform.setToTranslation(0, 0, 0);

        modelBuilder.begin();

        // Create a builder part for lines
        MeshPartBuilder lineBuilder = modelBuilder.part(
            "orbit_line",
            GL20.GL_LINES,
            VertexAttributes.Usage.Position,
            new Material()
        );

        lineBuilder.setColor(Color.WHITE);

        int segments = 64; // The more segments, the smoother the circle
        float lastX = orbitRadius;
        float lastY = 0f;

        for (int i = 1; i <= segments; i++) {
            // Calculate angle for the next point in radians (from 0 to 2*PI)
            float angle = (i / (float) segments) * MathUtils.PI2;

            // Trigonometry: find the next point on the circle
            float nextX = MathUtils.cos(angle) * orbitRadius;
            float nextY = MathUtils.sin(angle) * orbitRadius;

            // Draw a straight line from the previous point to the next one
            lineBuilder.line(lastX, lastY, 0f, nextX, nextY, 0f);

            // Save current point as the starting point for the next segment
            lastX = nextX;
            lastY = nextY;
        }

        orbitModel = modelBuilder.end();
        orbitInstance = new ModelInstance(orbitModel);
    }

    @Override
    public void update(float delta) {
        // Accumulate time scaled by rotation speed to increase the angle smoothly
        animationTime += delta * rotationSpeed;

        // Calculate current positions using the animation time directly as the angle
        float nextX = MathUtils.cos(animationTime) * orbitRadius;
        float nextY = MathUtils.sin(animationTime) * orbitRadius;

        // Move the 3D electron instance to the new calculated coordinates (Z is flat 0)
        electronInstance.transform.setToTranslation(nextX, nextY, 0f);
    }

    @Override
    public void render(ModelBatch modelBatch, PerspectiveCamera camera) {
        modelBatch.render(nucleusInstance);
        modelBatch.render(orbitInstance);
        modelBatch.render(electronInstance);
    }

    @Override
    public void dispose() {
        // Release native memory for all generated 3D models to prevent memory leaks
        if (nucleusModel != null) nucleusModel.dispose();
        if (electronModel != null) electronModel.dispose();
        if (orbitModel != null) orbitModel.dispose();
    }
}
