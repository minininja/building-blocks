package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.BasePipelineContext;
import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.standard.Pipeline;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParallelPipeline extends Pipeline {
    @Override
    public PipelineContext execute(PipelineContext context) {
        ParallelPipelineContext ctx;
        if (!(context instanceof ParallelPipelineContext)) {
            ctx = new ParallelPipelineContext().merge(context);
        } else {
            ctx = (ParallelPipelineContext) context;
        }

        Set<Stage> executed = new HashSet<>(super.getStages().size());

        while (executed.size() != super.getStages().size()) {
            Set<Stage> toExecute = new HashSet<>(super.getStages().size() - executed.size());

            // could be more efficient to construct a set of all stages and the remove what was executed,
            // but it seems unlikely the pipelines would ever be that large so perhaps it's not worth
            // the effort.
            for (Stage stg : super.getStages()) {
                if (!executed.contains(stg) && super.preVerify(stg, ctx)) {
                    toExecute.add(stg);
                }
            }

            if (toExecute.size() > 0) {
                // think about adding a custom thread pool later
                toExecute.parallelStream().map(a -> a.execute(ctx)).forEach(a -> ctx.merge(a));

                // cleanup
                executed.addAll(toExecute);
                toExecute.clear();
            } else {
                throw new UnresolvableRequirementsException("Could not execute all stages", ctx, executed.size());
            }
        }

        return ctx;
    }
}
