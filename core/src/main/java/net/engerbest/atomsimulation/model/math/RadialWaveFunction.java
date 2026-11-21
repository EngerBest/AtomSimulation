package net.engerbest.atomsimulation.model.math;

import static java.lang.Math.*;

public class RadialWaveFunction {
    public static double evaluate(int n, int l, double r, double a0) {
        if (a0 <= 0.0 || r < 0.0) return 0.0;

        double norm = 1.0 / (a0 * sqrt(a0)); // 1 / a0^(3/2)
        double rho = r / a0;

        // 1s orbital: (2 * norm) * e^(-rho)
        if (n == 1 && l == 0) return 2.0 * norm * exp(-rho);

        // 2s orbital: (1 / 2√2) * norm * (2 - rho) * e^(-rho / 2)
        if (n == 2 && l == 0)
            return (1.0 / (2.0 * sqrt(2.0))) * norm * (2.0 - rho) * exp(-rho / 2.0);

        // 2p orbital: (1 / 2√6) * norm * rho * e^(-rho / 2)
        if (n == 2 && l == 1)
            return (1.0 / (2.0 * sqrt(6.0))) * norm * rho * exp(-rho / 2.0);

        return 0.0;
    }
}
