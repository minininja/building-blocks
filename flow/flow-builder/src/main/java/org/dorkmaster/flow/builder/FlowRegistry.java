package org.dorkmaster.flow.builder;

import org.dorkmaster.flow.Flow;

import java.io.InputStream;

public interface FlowRegistry {
    FlowRegistry register(Provider provider);
    FlowRegistry load(InputStream in);
    Flow build(String name);
}
