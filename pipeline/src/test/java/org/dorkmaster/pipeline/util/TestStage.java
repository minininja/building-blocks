package org.dorkmaster.pipeline.util;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;

import java.util.Collection;
import java.util.Collections;

public class TestStage implements Stage {
    public static TestStage nada() {
        return new TestStage(Collections.emptySet(), Collections.emptySet());
    }

    public static TestStage req(Collection<String> req) {
        return new TestStage(req, Collections.emptySet());
    }

    public static TestStage pro(Collection<String> pro) {
        return new TestStage(Collections.emptySet(), pro);
    }

    protected Collection<String> req = Collections.emptySet();
    protected Collection<String> pro = Collections.emptySet();
    protected Collection<String> adds = null;

    public TestStage(Collection<String> req, Collection<String> pro) {
        this.req = req;
        this.pro = pro;
        this.adds = pro;
    }

    public TestStage(Collection<String> req, Collection<String> pro, Collection<String> adds) {
        this(req, pro);
        this.adds = adds;
    }

    @Override
    public Collection<String> required() {
        return req;
    }

    @Override
    public Collection<String> provides() {
        return pro;
    }

    @Override
    public PipelineContext execute(PipelineContext ctx) {
        if (adds != null){
            for (String add : adds){
                ctx.set(add, add);
            }
        }
        return ctx;
    }
}
