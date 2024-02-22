package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import hr.fer.zemris.math.Util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Sequential Newton-Raphson fractal generator.
 *
 * @author Danijel Barišić
 */
public class Newton {

    /**
     * Prompts the user for complex roots.
     *
     * @param args
     */
    public static void main(String[] args) {
        Complex[] roots = Util.promptForRoots();
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(Complex.ONE, roots); //Complex.ONE for constant

        FractalViewer.show(new SequentialNewtonProducer(crp));
    }

    /**
     * Calculates the Newton fractal coordinates and draws the image of the fractal.
     *
     * @author Danijel Barišić
     */
    public static class SequentialNewtonProducer implements IFractalProducer {

        /**
         * Complex rooted polynomial that represents the fractal formula.
         */
        private ComplexRootedPolynomial crp;

        /**
         * @param crp complex rooted polynomial that represents the fractal formula.
         */
        public SequentialNewtonProducer(ComplexRootedPolynomial crp) {
            this.crp = crp;
        }

        /**
         * Calculates the Newton fractal coordinates and maps appropriate colors to pixels,
         * according to closest complex root of a complex number that's a result of iterations of the algorithm.
         *
         * @param reMin     minimal value on real axis
         * @param reMax     maximal value on real axis
         * @param imMin     minimal value on imaginary axis
         * @param imMax     maximal value on imaginary axis
         * @param width     width of the screen onto which fractal is displayed
         * @param height    height of the screen onto which fractal is displayed
         * @param requestNo request identifier which will be associated with generated data
         * @param observer  accepts data for fractal generation and displays the fractal
         * @param cancel    flag that signals to cancel production of fractal when true
         */
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Započinjem izračun...");

            int m = 16 * 16 * 16;
            int offset = 0;
            short[] data = new short[width * height];

            ComplexPolynomial polynomial = crp.toComplexPolynom();
            ComplexPolynomial derived = polynomial.derive();

            double convergenceThreshold = 0.001;

            for (int y = 0; y < height; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {

                    double cRe = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cIm = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cRe, cIm);

                    double module = 0;
                    int iters = 0;

                    //algorithm for fractal
                    do {
                        Complex numerator = polynomial.apply(zn);
                        Complex denominator = derived.apply(zn);
                        Complex znOld = zn;
                        Complex fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = zn.sub(znOld).module();

                        iters++;
                    } while (module > convergenceThreshold && iters < m);

                    int index = crp.indexOfClosestRootFor(zn, 0.002);
                    data[offset++] = (short) (index + 1);
                }
            }

            System.out.println("Računanje gotovo. Idem obavijestiti promatrača tj. GUI!");

            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }
    }
}
