package org.dorkmaster.flow;

import org.dorkmaster.flow.expression.AndExpression;
import org.dorkmaster.flow.expression.OrExpression;
import org.dorkmaster.flow.helpers.NoopAction;
import org.dorkmaster.flow.helpers.TrueExpression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FlowBuilderTest {
    protected static FlowBuilder flowBuilder;

    @BeforeAll
    public static void setup() {
        FlowBuilder.registerExpressionBuilder("true", new ExpressionBuilder() {
            @Override
            public Expression build(FlowBuilder builder, Map<String, Object> data) {
                return new TrueExpression();
            }
        });
        FlowBuilder.registerActionBuilder("noop", new ActionBuilder() {
            @Override
            public Action build(FlowBuilder builder, Map<String, Object> data) {
                return new NoopAction();
            }
        });

        flowBuilder = FlowBuilder.from("/test-flow.yml");
        assertNotNull(flowBuilder);
    }

    @Test
    public void noopTest() {
        Flow flow = flowBuilder.getFlow("noop");
        assertNotNull(flow);
        assertTrue(flow.getExpression() instanceof TrueExpression);
        assertTrue(flow.getAction() instanceof NoopAction);

        FlowContext ctx = flow.execute(new FlowContext());
        assertNotNull(ctx);
    }

    @Test
    public void noExpressionTest() {
        Flow flow = flowBuilder.getFlow("noExpression");
        assertNotNull(flow);
        assertNull(flow.getExpression());

        FlowContext ctx = flow.execute(new FlowContext());
        assertNotNull(ctx);
    }

    @Test
    public void andTrueTest() {
        Flow flow = flowBuilder.getFlow("andTrue");
        assertNotNull(flow);
        assertTrue(flow.getExpression() instanceof AndExpression);

        FlowContext ctx = flow.execute(new FlowContext());
        assertNotNull(ctx);
    }

    @Test
    public void orTrueTest() {
        Flow flow = flowBuilder.getFlow("orTrue");
        assertNotNull(flow);
        assertTrue(flow.getExpression() instanceof OrExpression);

        FlowContext ctx = flow.execute(new FlowContext());
        assertNotNull(ctx);
    }

}