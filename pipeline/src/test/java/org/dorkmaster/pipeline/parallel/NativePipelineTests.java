package org.dorkmaster.pipeline.parallel;

import org.dorkmaster.pipeline.Pipeline;
import org.dorkmaster.pipeline.PipelineFactory;
import org.dorkmaster.pipeline.Stage;

import java.util.Collection;

public class NativePipelineTests extends AbstractThreadTests {
    @Override
    protected Pipeline create(Collection<Stage> stages){
        return PipelineFactory.createNative(stages);
    }

    @Override
    protected Pipeline create(Collection<Stage> stages, int timeout) {
        return PipelineFactory.createNative(stages, timeout);
    }
}
