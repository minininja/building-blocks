import org.dorkmaster.flow.Action;
import org.dorkmaster.flow.FlowContext;
import org.dorkmaster.flow.action.CompositeAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTests {
    public static final Action one = new Action() {
        @Override
        public FlowContext execute(FlowContext ctx) {
            return ctx.set("one", "one");
        }
    };

    public static final Action two = new Action() {
        @Override
        public FlowContext execute(FlowContext ctx) {
            return ctx.set("two", "two");
        }
    };

    @Test
    public void testComposite() {
        Action action = new CompositeAction(List.of(one, two));
        FlowContext ctx = action.execute(new FlowContext());

        assertEquals(ctx.get("one"), "one");
        assertEquals(ctx.get("two"), "two");
    }
}
