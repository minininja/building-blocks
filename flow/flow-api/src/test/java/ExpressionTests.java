import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.FlowContext;
import org.dorkmaster.flow.expression.AndExpression;
import org.dorkmaster.flow.expression.NotExpression;
import org.dorkmaster.flow.expression.OrExpression;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionTests {
    public static final Expression falseExp = new Expression() {
        @Override
        public boolean isValid(FlowContext ctx) {
            return false;
        }
    };

    public static final Expression trueExp = new Expression() {
        @Override
        public boolean isValid(FlowContext ctx) {
            return true;
        }
    };

    @Test
    public void testNot() {
        Expression exp = new NotExpression(falseExp);
        assertTrue(exp.isValid(new FlowContext()));
    }

    @Test
    public void testOrTrue() {
        Expression exp = new OrExpression(List.of(falseExp, trueExp));
        assertTrue(exp.isValid(new FlowContext()));
        exp = new OrExpression(List.of(trueExp, falseExp));
        assertTrue(exp.isValid(new FlowContext()));
    }

    @Test
    public void testOrFalse() {
        Expression exp = new OrExpression(List.of(falseExp, falseExp));
        assertFalse(exp.isValid(new FlowContext()));
    }

    @Test
    public void testAndTrue() {
        Expression exp = new AndExpression(List.of(trueExp, trueExp));
        assertTrue(exp.isValid(new FlowContext()));
    }

    @Test
    public void testAndFalse() {
        Expression exp = new AndExpression(List.of(trueExp, falseExp));
        assertFalse(exp.isValid(new FlowContext()));

        exp = new AndExpression(List.of(falseExp,trueExp));
        assertFalse(exp.isValid(new FlowContext()));
    }
}
