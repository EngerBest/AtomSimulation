package net.engerbest.atomsimulation.view.impl;

import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.Vector3;
import net.engerbest.atomsimulation.model.impl.BohrAtomModel;
import net.engerbest.atomsimulation.view.AtomPartsFactory;
import net.engerbest.atomsimulation.view.AtomRenderer;

public class BohrAtomRenderer implements AtomRenderer {
    private final BohrAtomModel mathModel; // Store specific math model as a private field

    private final Model nucleusMesh;
    private final Model electronMesh;
    private final Model orbitalMesh;

    private final ModelInstance nucleusInstance;
    private final ModelInstance electronInstance;
    private final ModelInstance orbitalInstance;

    public BohrAtomRenderer(AtomPartsFactory factory, BohrAtomModel mathModel) {
        this.nucleusMesh = factory.createNucleusMesh();
        this.electronMesh = factory.createElectronMesh();
        this.orbitalMesh = factory.createOrbitalMesh();

        this.nucleusInstance = new ModelInstance(nucleusMesh);
        this.electronInstance = new ModelInstance(electronMesh);
        this.orbitalInstance = new ModelInstance(orbitalMesh);

        this.mathModel = mathModel; // Bind them tightly inside this specific renderer
    }

    @Override
    public void render(ModelBatch batch) {
        Vector3 nucleusPos = mathModel.getNucleusPosition();
        Vector3 electronPos = mathModel.getElectronPosition(0);
        Vector3 orbitalPos = mathModel.getOrbitalPosition();

        nucleusInstance.transform.setToTranslation(nucleusPos);
        electronInstance.transform.setToTranslation(electronPos);
        orbitalInstance.transform.setToTranslation(orbitalPos);

        batch.render(nucleusInstance);
        batch.render(electronInstance);
        batch.render(orbitalInstance);
    }

    @Override
    public void dispose() {
        if (nucleusMesh != null) nucleusMesh.dispose();
        if (electronMesh != null) electronMesh.dispose();
        if (orbitalMesh != null) orbitalMesh.dispose();
    }
}
