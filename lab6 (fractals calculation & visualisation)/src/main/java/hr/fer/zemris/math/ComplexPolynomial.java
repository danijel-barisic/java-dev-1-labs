package hr.fer.zemris.math;

import java.util.List;

/**
 * Class that models a complex polynomial, e.g. (7+2i)z^3+2z^2+5z+1
 *
 * @author Danijel Barišić
 */
public class ComplexPolynomial {

    /**
     * Factors of the polynomial.
     */
    private final List<Complex> factors;

    /**
     * @param factors factors of the polynomial
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = List.of(factors);
    }

    /**
     * @return order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
     */
    public short order() {
        return (short) (factors.size() - 1);
    }

    /**
     * @param p polynomial to multiply this with
     * @return new polynomial this*p
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {

        //+1 for 0th member, it was excluded from order, yet it has to be included in size
        Complex[] newFactors = new Complex[this.order() + p.order() + 1];

        for (int i = 0; i < this.order() + p.order() + 1; i++) {
            newFactors[i] = Complex.ZERO;
        }

        //iterativno mnozenje
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < p.factors.size(); j++) {
                newFactors[i + j] = newFactors[i + j]
                        .add(this.factors.get(i).multiply(p.factors.get(j)));
            }
        }

        return new ComplexPolynomial(newFactors);
    }

    // computes first derivative of this polynomial; for example, for
    // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5

    /**
     * @return first derivative of this polynomial, e.g. for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     */
    public ComplexPolynomial derive() {
        if (this.order() == 0) {
            return new ComplexPolynomial(Complex.ZERO);
        } else if (this.order() == 1) {
            return new ComplexPolynomial(this.factors.get(1)); //derivation of the first order member
        }

        Complex[] derived = new Complex[this.factors.size() - 1];

        int factorsLen = this.factors.size();
        for (int i = 0; i < factorsLen - 1; i++) {
            derived[i] = this.factors.get(i + 1).multiply(i + 1);
        }

        return new ComplexPolynomial(derived);
    }


    /**
     * Computes polynomial value at given point z.
     *
     * @param z complex point
     * @return polynomial value at point z
     */
    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;

        for (int i = 0; i < factors.size(); i++) {
            result = result.add(factors.get(i).multiply(z.power(i)));
        }

        return result;
    }

    /**
     * @return factors of the polynomial
     */
    public List<Complex> getFactors() {
        return factors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%s)", factors.get(factors.size() - 1)));
        for (int i = factors.size() - 2; i >= 0; i--) {
            sb.append(String.format("*z^%d + (%s)", i + 1, factors.get(i)));
        }
        return sb.toString();
    }
}
