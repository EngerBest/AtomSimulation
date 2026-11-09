package net.engerbest.atomsimulation.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import static com.badlogic.gdx.math.MathUtils.*;

public class AtomPartsFactory {
    // Reuse a single builder context to avoid redundant resource allocations during mesh creation
    private final ModelBuilder modelBuilder = new ModelBuilder();

    private final float orbitRadius;

    public AtomPartsFactory(float orbitRadius) {
        this.orbitRadius = orbitRadius;
    }

    /**
     * Creates an atomic nucleus mesh based on standard parameters.
     * @return Mesh created in the method.
     */
    public Model createNucleusMesh() {
        return modelBuilder.createSphere(
            1.5f, 1.5f, 1.5f, // Dimensions along the X, Y, and Z axes (sphere diameter)
            24, 24, // Polygon mesh (the more there are, the rounder the sphere)
            new Material(ColorAttribute.createDiffuse(Color.RED)), // Material and color (Red)
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal // Instructions for the graphics card
        );
    }

    /**
     * Creates an electron mesh of the nucleus based on standard parameters.
     * @return Mesh created in the method.
     */
    public Model createElectronMesh() {
        return modelBuilder.createSphere(
            0.4f, 0.4f, 0.4f, // Dimensions along the X, Y, and Z axes (sphere diameter)
            16, 16, // Polygon mesh (the more there are, the rounder the sphere)
            new Material(ColorAttribute.createDiffuse(Color.CYAN)), // Material and color (Red)
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal // Instructions for the graphics card
        );
    }

    public Model createOrbitalMesh() {
        ModelBuilder localBuilder = new ModelBuilder();

        localBuilder.begin();

        // Create a builder part for lines
        MeshPartBuilder lineBuilder = localBuilder.part(
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
            float angle = (i / (float) segments) * PI2;

            // Trigonometry: find the next point on the circle
            float nextX = cos(angle) * orbitRadius;
            float nextY = sin(angle) * orbitRadius;

            // Draw a straight line from the previous point to the next one
            lineBuilder.line(lastX, lastY, 0f, nextX, nextY, 0f);

            // Save current point as the starting point for the next segment
            lastX = nextX;
            lastY = nextY;
        }

        return localBuilder.end();
    }
}
