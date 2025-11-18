package org.dorkmaster.pipeline.standard;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.PipelineException;
import org.dorkmaster.pipeline.Stage;

import java.util.Collection;
import java.util.LinkedList;

public class Pipeline {
    private Collection<Stage> stages = new LinkedList<>();

    public Pipeline addStage(Stage stage) {
        stages.add(stage);
        return this;
    }

    protected Collection<Stage> getStages() {
        return stages;
    }

    protected boolean preVerify(Stage stage, PipelineContext ctx) {
        return verify(stage.required(), ctx);
    }

    private boolean postVerify(Stage stage, PipelineContext ctx) {
        return verify(stage.provides(), ctx);
    }

    private boolean verify(Collection<String> keys, PipelineContext ctx) {
        for (String key : keys) {
            if (ctx.get(key) == null) {
                return false;
            }
        }
        return true;
    }

    public PipelineContext execute(PipelineContext ctx) {
        for (Stage stage : stages) {
            if (!preVerify(stage, ctx)) {
                throw new PipelineException("Missing pre-condition", ctx);
            }
            ctx = stage.execute(ctx);
            if (!postVerify(stage, ctx)) {
                throw new PipelineException("Missing post-condition", ctx);
            }
        }
        return ctx;
    }
}
