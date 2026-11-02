package net.engerbest.atomsimulation.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class AtomPartsFactory {
    // Reuse a single builder context to avoid redundant resource allocations during mesh creation
    private final ModelBuilder modelBuilder = new ModelBuilder();

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
}
