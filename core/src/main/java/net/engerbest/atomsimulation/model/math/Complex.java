package net.engerbest.atomsimulation.model.math;

import static java.lang.Math.*;

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

    // Convenient way to overwrite current complex number by new in polar coordinatesПроверь
    public void setPolar(double radius, double angle) {
        re = radius * cos(angle);
        im = radius * sin(angle);
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
        return sqrt(magnitudeSquared());
    }

    // Convenient way to create new complex numbers from polar coordinates
    public static Complex fromPolar(double radius, double angle) {
        Complex result = new Complex();
        result.setPolar(radius, angle);
        return result;
    }

    // Returns current angle of complex number
    public double argument() {
        return atan2(im, re);
    }

    // Getters
    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}
