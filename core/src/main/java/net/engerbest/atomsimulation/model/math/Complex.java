package net.engerbest.atomsimulation.model.math;

public class Complex {
    // Fields (technically current complex number)
    private double re;
    private double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // Empty constructor for convenient use
    public Complex() {
        this(0.0, 0.0);
    }

    // Mutable methods
    // Overwrites current complex number to new values
    public void set(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // Overwrites current complex number to new values from Complex object
    public void set(Complex other) {
        re = other.getRe();
        im = other.getIm();
    }

    // Adds Complex object to current complex number
    public void addLocal(Complex other) {
        re += other.getRe();
        im += other.getIm();
    }

    // Multiplies current complex number by given Complex object
    public void multiplyLocal(Complex other) {
        double newRe = (re * other.getRe()) - (im * other.getIm());
        im = (re * other.getIm()) + (im * other.getRe());
        re = newRe;
    }

    // Immutable methods
    // Returns new Complex object that equals current added to given
    public Complex add(Complex other) {
        return new Complex(re + other.getRe(), im + other.getIm());
    }

    // Returns new Complex object that equals current multiplied by given
    public Complex multiply(Complex other) {
        Complex result = new Complex(re, im);
        result.multiplyLocal(other);
        return result;
    }

    // Returns new Complex object with inverted imaginary part
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    // Returns probability density of current complex number
    public double magnitudeSquared() {
        return re * re + im * im;
    }

    // Returns magnitude of current complex number
    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    // Getter and Setter
    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}
