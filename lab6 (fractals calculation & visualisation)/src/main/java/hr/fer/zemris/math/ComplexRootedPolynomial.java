package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that models complex polynomial in root form, like z0 * (z - z1) * (z - z2)*...
 *
 * @author Danijel Barišić
 */
public class ComplexRootedPolynomial {

    /**
     * Complex constant that multiplies the rest of the polynomial.
     */
    private final Complex constant;

    /**
     * Roots of the polynomial.
     */
    private final List<Complex> roots;

    /**
     * @param constant constant to multiply polynomial with
     * @param roots    roots of the polynomial
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = constant;
        this.roots = List.of(roots);
    }

    //

    /**
     * Computes polynomial value at given point z.
     *
     * @param z complex point
     * @return polynomial value at point z
     */
    public Complex apply(Complex z) {
        Complex res = constant;
        for (Complex root : roots) {
            res = res.multiply(z.sub(root));
        }
        return res;
    }

    /**
     * Converts this representation to ComplexPolynomial type.
     *
     * @return ComplexPolynomial representation of this
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial p = new ComplexPolynomial(constant);

        //iterative multiply of individual polynomials, z0*(z-z1)*(z-z2)... z0 is the constant
        for (int i = 0; i < roots.size(); i++) {
            p = p.multiply(new ComplexPolynomial(roots.get(i), Complex.ONE));
        }

        List<Complex> newFactors = new ArrayList<>(p.getFactors());
        Collections.reverse(newFactors);

        return new ComplexPolynomial(newFactors.toArray(new Complex[0]));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%s)", constant));
        for (Complex root : roots) {
            sb.append(String.format("*(z - (%s))", root));
        }
        return sb.toString();
    }


    /**
     * Finds index of closest root for given complex number z that is within threshold;
     * if there is no such root, returns -1.
     * First root has index 0, second index 1, etc.
     *
     * @param z         complex number
     * @param threshold cap on distance between z and an acceptably close root
     * @return index of closest root for z within threshold of the root
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        int indexOfClosest = -1;
        double minDist = Double.MAX_VALUE;

        for (int k = 0; k < roots.size(); k++) {

            double dist = z.sub(roots.get(k)).module();
            if (dist < minDist && dist < threshold) {
                minDist = dist;
                indexOfClosest = k;
            }
        }

        return indexOfClosest;
    }
}
