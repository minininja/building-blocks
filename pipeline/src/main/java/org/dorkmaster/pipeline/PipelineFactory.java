package org.dorkmaster.pipeline;

import org.dorkmaster.pipeline.parallel.NativeTheadedPipeline;
import org.dorkmaster.pipeline.parallel.VirtualThreadedPipeline;
import org.dorkmaster.pipeline.serial.StandardPipeline;

import java.util.Collection;

public class PipelineFactory {
    public static Pipeline createStandard(Collection<Stage> stages) {
        return new StandardPipeline(stages);
    }

    public static Pipeline createNative(Collection<Stage> stages) {
        return new NativeTheadedPipeline(stages);
    }

    public static Pipeline createNative(Collection<Stage> stages, int timeout) {
        return new NativeTheadedPipeline(stages, timeout);
    }

    public static Pipeline createVirtual(Collection<Stage> stages) {
        return new VirtualThreadedPipeline(stages);
    }

    public static Pipeline createVirtual(Collection<Stage> stages, int timeout) {
        return new VirtualThreadedPipeline(stages, timeout);
    }
}
