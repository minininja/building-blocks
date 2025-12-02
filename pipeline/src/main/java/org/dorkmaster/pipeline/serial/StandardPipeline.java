package org.dorkmaster.pipeline.serial;

import org.dorkmaster.pipeline.*;
import org.dorkmaster.pipeline.exception.PipelineExecutionException;

import java.util.Collection;

public class StandardPipeline extends AbstractBasePipeline {
    public StandardPipeline(Collection<Stage> stages) {
        super(stages);
    }

    protected PipelineContext doExecute(PipelineContext ctx, Collection<Collection<Stage>> bucketedStages) {
        for (Collection<Stage> step : bucketedStages) {
            for (Stage stage : step) {
                ctx = stage.execute(ctx);
                if (!postVerify(stage, ctx)) {
                    throw new PipelineExecutionException("Post execution product missing", ctx);
                }
            }
        }
        return ctx;
    }
}
