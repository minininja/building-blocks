package org.dorkmaster.pipeline;

import java.util.Collection;

public interface Stage {
    Collection<String> required();
    Collection<String> provided();

    PipelineContext execute(PipelineContext ctx);
}
