package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that models a complex number.
 *
 * @author Danijel Barišić
 */
public class Complex {

    /**
     * Real part of the complex number.
     */
    public final double re;

    /**
     * Imaginary part of the complex number.
     */
    public final double im;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * Initialises complex number to 0+i0.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * @param re real part of the complex number
     * @param im imaginary part of the complex number
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * @return module of complex number
     */
    public double module() {
        return Math.sqrt(this.re * this.re + this.im * this.im);
    }

    /**
     * @param c complex number
     * @return this * c
     */
    public Complex multiply(Complex c) {
        double resRe = 0, resIm = 0;

        resRe = this.re * c.re - this.im * c.im;
        resIm = this.im * c.re + this.re * c.im;

        return new Complex(resRe, resIm); // should be (7+2i) * (3+4i) = 13+34i;
    }

    /**
     * @param d real factor
     * @return this * d
     */
    public Complex multiply(double d) {
        return new Complex(this.re * d, this.im * d);
    }

    /**
     * @param c complex number
     * @return this / c
     */
    public Complex divide(Complex c) { // c is divisor
        double resRe = 0, resIm = 0;

        Complex numerator = this.multiply(c.conjugate());
        Complex denominator = c.multiply(c.conjugate()); // real number

        resRe = numerator.re * (1 / denominator.re);
        resIm = numerator.im * (1 / denominator.re);

        return new Complex(resRe, resIm);
    }

    /**
     * @param c complex number
     * @return this + c
     */
    public Complex add(Complex c) {
        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * @param c complex number
     * @return this - c
     */
    public Complex sub(Complex c) {
        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * @return -this
     */
    public Complex negate() {
        return this.multiply(new Complex(-1, -1));
    }

    /**
     * @param n non-negative integer exponent
     * @return this^n
     */
    public Complex power(int n) {

        double theta = Math.atan2(this.im, this.re);
        double r = this.module();

        double resRe = Math.pow(r, n) * Math.cos(n * theta);
        double resIm = Math.pow(r, n) * Math.sin(n * theta);

        return new Complex(resRe, resIm);
    }

    /**
     * @param n positive integer degree of root
     * @return n-th root of this
     */
    public List<Complex> root(int n) {

        if (n < 0) {
            throw new ArithmeticException("Root degree cannot be 0 or less");
        }

        List<Complex> resList = new ArrayList<>();
        double theta = Math.atan2(this.im, this.re);
        double rToNth = Math.sqrt(this.re * this.re + this.im * this.im);
        double r = Math.pow(rToNth, 1. / n);

        for (int k = 0; k < n; k++) {
            double resRe = r * Math.cos((theta + 2 * k * Math.PI) / n);
            double resIm = r * Math.sin((theta + 2 * k * Math.PI) / n);
            resList.add(new Complex(resRe, resIm));
        }

        return resList;
    }

    /**
     * @return invert sign of the imaginary part of this
     */
    public Complex conjugate() {
        return new Complex(this.re, -this.im);
    }

    @Override
    public String toString() {
        return this.re + (this.im >= 0 ? " + " : " - ") + "i" + Math.abs(this.im);
    }

    /**
     * @param o other complex number
     * @return true if real and imaginary parts of both numbers are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complex z = (Complex) o;

        if (Double.compare(this.re, z.re) != 0) return false;
        return Double.compare(this.im, z.im) == 0;
    }

}