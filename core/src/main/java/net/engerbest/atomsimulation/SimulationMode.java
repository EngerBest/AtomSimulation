package net.engerbest.atomsimulation;

public enum SimulationMode {
    BOHR("Bohr’s historical model"),
    SCHRODINGER("Schrödinger's quantum model");

    private final String title;

    SimulationMode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
