import org.dorkmaster.flow.FlowContext;
import org.dorkmaster.flow.expression.JexlEvalExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JexlTest {
    @Test
    public void basic() {
        assertFalse(new JexlEvalExpression("1 != 1").isValid(new FlowContext()));
        assertTrue(new JexlEvalExpression("1 == 1").isValid(new FlowContext()));
    }

    @Test
    public void testWithContext() {
        FlowContext context = new FlowContext().set("one", 1);
        assertTrue(new JexlEvalExpression("one == 1").isValid(context));

        context.set("two", 2);
        assertFalse(new JexlEvalExpression("one == two").isValid(context));
    }

    @Test
    public void testNonBoolean() {
        FlowContext context = new FlowContext().set("one", 1);
        assertTrue(new JexlEvalExpression("one.toString()").isValid(context));
        assertFalse(new JexlEvalExpression("two.toString()").isValid(context));
    }
}
