import org.dorkmaster.flow.Action;
import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.Flow;
import org.dorkmaster.flow.FlowContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FlowTests {

    @Test
    public void testHappyPositiveFlow() {
        Flow flow = new Flow()
            .setExpression(new Expression() {
                @Override
                public boolean isValid(FlowContext ctx) {
                    return true;
                }
            })
            .setAction(new Action() {

                @Override
                public FlowContext execute(FlowContext ctx) {
                    return ctx.set("happy", "happy");
                }
            });

        FlowContext ctx = flow.execute(new FlowContext());
        assertEquals(ctx.get("happy"), "happy");
    }

    @Test
    public void testHappyNegativeFlow() {
        Flow flow = new Flow()
                .setExpression(new Expression() {
                    @Override
                    public boolean isValid(FlowContext ctx) {
                        return false;
                    }
                })
                .setAction(new Action() {

                    @Override
                    public FlowContext execute(FlowContext ctx) {
                        throw new NullPointerException("Shouldn't get here");
                    }
                });

        FlowContext ctx = flow.execute(new FlowContext());
        assertNull(ctx.get("happy"));
    }
}
