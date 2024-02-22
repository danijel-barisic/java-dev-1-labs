package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.mandelbrot.Mandelbrot;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import hr.fer.zemris.math.Util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Newton-Raphson fractal generator that uses concurrency.
 *
 * @author Danijel Barišić
 */
public class NewtonParallel {

    /**
     * Complex rooted polynomial that represents the fractal formula.
     */
    private static ComplexRootedPolynomial crp;

    /**
     * Complex polynomial that represents the fractal formula.
     */
    private static ComplexPolynomial polynomial;

    /**
     * Complex polynomial that represents the derivation of fractal formula
     * i.e. derivation of fractal function.
     */
    private static ComplexPolynomial derived;


    /**
     * @param args optional arguments --workers=N (short: -w N) for setting custom number of worker threads,
     *             --tracks=N (short: -t N) for setting custom number of tracks/jobs
     */
    public static void main(String[] args) {
        int numOfWorkers = Runtime.getRuntime().availableProcessors();
        int numOfRows = 4 * numOfWorkers;

        boolean hasCustomWorkersNum = false;
        boolean hasCustomRowsNum = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("\\Q--workers=\\E\\d+")) {
                if (hasCustomWorkersNum) {
                    throw new IllegalStateException("Cannot have same parameter twice.");
                }
                hasCustomWorkersNum = true;
                numOfWorkers = Integer.parseInt(args[i].substring("--workers=".length()));
            } else if (args[i].matches("\\Q--tracks=\\E\\d+")) {
                if (hasCustomRowsNum) {
                    throw new IllegalStateException("Cannot have same parameter twice.");
                }

                hasCustomRowsNum = true;
                numOfRows = Integer.parseInt(args[i].substring("--tracks=".length()));
            } else if (args[i].matches("\\-w")) {

                if (i + 1 < args.length && args[i + 1].matches("\\d+")) {
                    if (hasCustomWorkersNum) {
                        throw new IllegalStateException("Cannot have same parameter twice.");
                    }
                    hasCustomWorkersNum = true;
                    numOfWorkers = Integer.parseInt(args[i + 1]);
                    i++;
                } else {
                    throw new IllegalArgumentException("Invalid parameters.");
                }

            } else if (args[i].matches("\\-t")) {

                if (i + 1 < args.length && args[i + 1].matches("\\d+")) {

                    if (hasCustomRowsNum) {
                        throw new IllegalStateException("Cannot have same parameter twice.");
                    }

                    hasCustomRowsNum = true;
                    numOfRows = Integer.parseInt(args[i + 1]);
                    i++;
                } else {
                    throw new IllegalArgumentException("Invalid parameters.");
                }
            } else {
                throw new IllegalArgumentException("Invalid parameters.");
            }

        }

        Complex[] roots = Util.promptForRoots();
        crp = new ComplexRootedPolynomial(Complex.ONE, roots);
        polynomial = crp.toComplexPolynom();
        derived = polynomial.derive();

        FractalViewer.show(new NewtonParallelProducer(numOfWorkers, numOfRows));
    }

    /**
     * Job in which calculation of a portion of the fractal is done.
     *
     * @author Danijel Barišić
     */
    public static class CalculationJob implements Runnable {
        private double reMin;
        private double reMax;
        private double imMin;
        private double imMax;
        private int width;
        private int height;
        private int yMin;
        private int yMax;
        private int m;
        private short[] data;
        private AtomicBoolean cancel;
        public static CalculationJob NO_JOB = new CalculationJob();

        private CalculationJob() {
        }

        /**
         * @param reMin  minimal value on real axis
         * @param reMax  maximal value on real axis
         * @param imMin  minimal value on imaginary axis
         * @param imMax  maximal value on imaginary axis
         * @param width  width of the screen onto which fractal is displayed
         * @param height height of the screen onto which fractal is displayed
         * @param yMin   beginning of the portion to calculate on y axis, inclusive
         * @param yMax   ending of the portion to calculate on y axis, inclusive
         * @param m      number of iterations to execute in trying to find divergence
         * @param data   field to store calculation result to
         * @param cancel flag that signals to cancel production of fractal when true
         */
        public CalculationJob(double reMin, double reMax, double imMin,
                              double imMax, int width, int height, int yMin, int yMax,
                              int m, short[] data, AtomicBoolean cancel) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
        }

        /**
         * Executes the job of calculating the fractal, iterating toward divergence for each dot in display.
         */
        @Override
        public void run() {

            int offset = this.yMin * width;
            double convergenceThreshold = 0.001;

            for (int y = this.yMin; y <= this.yMax; y++) {
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
        }
    }

    /**
     * Calculates the Newton fractal coordinates and draws the image of the fractal,
     * uses parallelization for splitting calculation jobs across y axis.
     *
     * @author Danijel Barišić
     */
    public static class NewtonParallelProducer implements IFractalProducer {

        private final int numOfWorkers;
        private final int numOfRows;

        /**
         * @param numOfWorkers number of worker threads
         * @param numOfRows    number of rows/jobs
         */
        public NewtonParallelProducer(int numOfWorkers, int numOfRows) {
            this.numOfWorkers = numOfWorkers;
            this.numOfRows = numOfRows;
        }

        /**
         * Calculates the Newton fractal coordinates and maps appropriate colors to pixels,
         * according to closest complex root of a complex number that's a result of iterations of the algorithm.
         * Uses parallelization for splitting calculation jobs across y axis.
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
            short[] data = new short[width * height];
            int yAmountPerRow = height / numOfRows;

            final BlockingQueue<CalculationJob> queue = new LinkedBlockingQueue<>();

            Thread[] workers = new Thread[numOfWorkers];
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(() -> {
                    while (true) {
                        CalculationJob p = null;
                        try {
                            p = queue.take();
                            if (p == CalculationJob.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        p.run();
                    }
                });
            }
            for (Thread worker : workers) {
                worker.start();
            }

            for (int i = 0; i < numOfRows; i++) {
                int yMin = i * yAmountPerRow;
                int yMax = (i + 1) * yAmountPerRow - 1;
                if (i == numOfRows - 1) {
                    yMax = height - 1;
                }
                CalculationJob job = new CalculationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);
                while (true) {
                    try {
                        queue.put(job);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }
            for (int i = 0; i < workers.length; i++) {
                while (true) {
                    try {
                        queue.put(CalculationJob.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }
    }
}