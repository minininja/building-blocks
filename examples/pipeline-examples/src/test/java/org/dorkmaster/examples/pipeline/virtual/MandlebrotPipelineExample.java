package org.dorkmaster.examples.pipeline.virtual;

import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineFactory;
import org.dorkmaster.pipeline.Stage;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MandlebrotPipelineExample {
    int xRes = 640;
    int yRes = 480;

    double pMin = -2.0;
    double pMax = 2.0;
    double qMin = -2.0;
    double qMax = 2.0;

    int i;
    double max = 4.0;

    Collection<Stage> generateStages(int xres, int yres, double pmin, double pmax, double qmin, double qmax, int i, double max) {
        Collection<Stage> stages = new LinkedHashSet<>();
        stages.add(new ComputeConstantsStage(xres, yres, pmin, pmax, qmin, qmax));
        for (int x = 0; x < xres; x++){
            for (int y = 0; y < yres; y++){
                stages.add(new ComputePixelStage(x, y, i, max));
            }
        }
        return stages;
    }

    @Test
    public void mandlebrotRunStandard(){
        int[][] pixels = new int[xRes][yRes];
        Pipeline pipeline = PipelineFactory.createStandard(generateStages(xRes, yRes, pMin, pMax, qMin, qMax, i, max));
        PipelineContext ctx = PipelineContext.newInstance().set("pixels", pixels);
        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();
        System.out.println("mandlebrot runtime on standard: " + runtime);
        assertTrue(ctx.keys().size() > 0);
    }

    @Test
    public void mandlebrotRunNative(){
        int[][] pixels = new int[xRes][yRes];
        Pipeline pipeline = PipelineFactory.createStandard(generateStages(xRes, yRes, pMin, pMax, qMin, qMax, i, max));
        PipelineContext ctx = PipelineContext.newInstance().set("pixels", pixels);
        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();
        System.out.println("mandlebrot runtime on native: " + runtime);
        assertTrue(ctx.keys().size() > 0);
    }

    @Test
    public void mandlebrotRunVirtual(){
        int[][] pixels = new int[xRes][yRes];
        Pipeline pipeline = PipelineFactory.createStandard(generateStages(xRes, yRes, pMin, pMax, qMin, qMax, i, max));
        PipelineContext ctx = PipelineContext.newInstance().set("pixels", pixels);
        Timer t = new Timer();
        ctx = pipeline.execute(ctx);
        long runtime = t.delta();
        System.out.println("mandlebrot runtime on virtual: " + runtime);
        assertTrue(ctx.keys().size() > 0);
    }

    class Timer {
        long start = System.currentTimeMillis();

        long delta() {
            return System.currentTimeMillis() - start;
        }
    }

    class ComputeConstantsStage implements Stage {
        int xRes;
        int yRes;
        double cxMin;
        double cxMax;
        double cyMin;
        double cyMax;

        public ComputeConstantsStage(int xRes, int yRes, double cxMin, double cxMax, double cyMin, double cyMax) {
            this.xRes = xRes;
            this.yRes = yRes;
            this.cxMin = cxMin;
            this.cxMax = cxMax;
            this.cyMin = cyMin;
            this.cyMax = cyMax;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ComputeConstantsStage that = (ComputeConstantsStage) o;
            return xRes == that.xRes && yRes == that.yRes && Double.compare(cxMin, that.cxMin) == 0 && Double.compare(cxMax, that.cxMax) == 0 && Double.compare(cyMin, that.cyMin) == 0 && Double.compare(cyMax, that.cyMax) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(xRes, yRes, cxMin, cxMax, cyMin, cyMax);
        }

        @Override
        public Collection<String> required() {
            return Collections.emptySet();
        }

        @Override
        public Collection<String> provides() {
            return List.of("cx", "cy");
        }

        @Override
        public PipelineContext execute(PipelineContext ctx) {
            double xStep = (cxMax - cxMin) / xRes;
            double[] cx = new double[xRes];
            for (int i = 0; i < cx.length; i++) {
                cx[i] = cxMin + i * xStep;
            }

            double yStep = (cyMax - cyMin) / xRes;
            double[] cy = new double[yRes];
            for (int i = 0; i < cy.length; i++) {
                cy[i] = cyMin + i * yStep;
            }

            return ctx.set("cx", cx).set("cy", cy);
        }
    }

    class ComputePixelStage implements Stage {
        int x;
        int y;
        int i;
        double max;

        public ComputePixelStage(int x, int y, int i, double max) {
            this.x = x;
            this.y = y;
            this.i = i;
            this.max = max;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ComputePixelStage that = (ComputePixelStage) o;
            return x == that.x && y == that.y && i == that.i && Double.compare(max, that.max) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, i, max);
        }

        @Override
        public Collection<String> required() {
            return List.of("cx", "cy", "pixels");
        }

        @Override
        public Collection<String> provides() {
            return Collections.emptySet();
        }

        @Override
        public PipelineContext execute(PipelineContext ctx) {
            int[][] pixels =  (int[][]) ctx.get("pixels");
            double[] cx = (double[]) ctx.get("cx");
            double[] cy = (double[]) ctx.get("cy");

            double zx = 0;
            double zy = 0;
            double tmp;

            int i = 0;
            while (i < this.i && Math.abs(zx +zy) < max) {
                tmp = zx * zx + zy * zy + cx[x];
                zy = 2 * zx * zy + cy[y];
                zx = tmp;
                i++;
            }
            pixels[x][y] = i;

            return ctx;
        }
    }
}
