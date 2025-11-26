package org.dorkmaster.examples.pipeline.virtual;

import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineFactory;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.util.Timer;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MandlebrotPipelineExample {
    int xRes = 1024 * 5;
    int yRes = 768 * 5;

    double pMin = -2.0;
    double pMax = 2.0;
    double qMin = -2.0;
    double qMax = 2.0;

    int i = 256;
    double max = 4.0;
    int slices = 10;

    Collection<Stage> generateStages(int xres, int yres, double pmin, double pmax, double qmin, double qmax, int i, double max) {
        Collection<Stage> stages = new LinkedHashSet<>();
        stages.add(new ComputeConstantsStage(xres, yres, pmin, pmax, qmin, qmax));

        int step = xres / slices;
        int xMin = 0;
        int xMax = xMin + step;
        while (xMax < xres) {
            stages.add(new ComputePixelStage(xMin, xMax, yRes, i, max));
            xMin = xMax + 1;
            xMax = xMin + step;
        }
        if (xMin < xRes){
            stages.add(new ComputePixelStage(xMin, xres - 1, yRes, i, max));
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
        assertTrue(ctx.keys().contains("slice:0"));
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
        assertTrue(ctx.keys().contains("slice:0"));
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
        assertTrue(ctx.keys().contains("slice:0"));

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
        int xMin;
        int xMax;
        int yRes;
        int i;
        double max;

        public ComputePixelStage(int xMin, int xMax, int yRes, int i, double max) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yRes = yRes;
            this.i = i;
            this.max = max;
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

            int cnt = 0;
            for (int x = xMin; x <= xMax; x++) {
                for (int y = 0; y < yRes; y++) {
                    double zx = 0;
                    double zy = 0;
                    double tmp;

                    int iterator = 0;
                    while (iterator < i && Math.abs(zx +zy) < max) {
                        tmp = zx * zx + zy * zy + cx[x];
                        zy = 2 * zx * zy + cy[y];
                        zx = tmp;
                        iterator++;
                    }
                    pixels[x][y] = iterator;
                    cnt++;
                }
            }

            return ctx.set("slice:"+xMin, cnt);
        }
    }
}
