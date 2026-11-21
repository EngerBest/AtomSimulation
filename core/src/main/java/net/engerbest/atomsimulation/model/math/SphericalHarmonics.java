package net.engerbest.atomsimulation.model.math;

import static java.lang.Math.*;

public class SphericalHarmonics {
    private static final double K_0_0 = 1.0 / sqrt(4.0 * PI);
    private static final double K_1_0 = sqrt(3.0 / (4.0 * PI));
    private static final double K_1_1 = sqrt(3.0 / (8.0 * PI));

    public static void evaluate(int l, int m, double theta, double phi, Complex out) {
        if (out == null) return;

        if (l == 0 && m == 0) {
            out.set(K_0_0, 0.0);
            return;
        }

        if (l == 1 && m == 0) {
            out.set(K_1_0 * cos(theta), 0.0);
            return;
        }

        if (l == 1 && m == 1) {
            out.setPolar(-K_1_1 * sin(theta), phi);
            return;
        }

        if (l == 1 && m == -1) {
            out.setPolar(K_1_1 * sin(theta), -phi);
            return;
        }

        out.set(0.0, 0.0);
    }
}
