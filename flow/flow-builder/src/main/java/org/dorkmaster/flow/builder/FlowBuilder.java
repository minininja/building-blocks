package org.dorkmaster.flow.builder;

import org.dorkmaster.flow.Flow;

import java.io.InputStream;

public interface FlowBuilder {
    FlowBuilder register(Provider provider);
    FlowBuilder load(InputStream in);
    Flow build(String name);
}
