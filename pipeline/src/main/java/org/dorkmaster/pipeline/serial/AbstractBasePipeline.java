package org.dorkmaster.pipeline.serial;

import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;

import java.util.Collection;
import java.util.LinkedList;

public abstract class AbstractBasePipeline implements Pipeline {

    private Collection<Stage> stages = new LinkedList<>();

    public AbstractBasePipeline() {
    }

    public AbstractBasePipeline(Collection<Stage> stages) {
        this.stages = stages;
    }

    public AbstractBasePipeline addStage(Stage stage) {
        stages.add(stage);
        return this;
    }

    public PipelineContext execute(PipelineContext ctx) {
        // bucket the stages for execution
        Collection<Collection<Stage>> bucketed =
                new DependencyResolver().orderStages(ctx, stages).getResolvedStages();

        return doExecute(ctx, bucketed);
    }

    protected abstract PipelineContext doExecute(PipelineContext ctx, Collection<Collection<Stage>> bucketedStages);

    protected boolean postVerify(Stage stage, PipelineContext ctx) {
        return verify(stage.provides(), ctx);
    }

    protected boolean verify(Collection<String> keys, PipelineContext ctx) {
        for (String key : keys) {
            if (ctx.get(key) == null) {
                return false;
            }
        }
        return true;
    }

}
