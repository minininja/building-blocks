package org.dorkmaster.flow.expression;

import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.dorkmaster.flow.Expression;
import org.dorkmaster.flow.FlowContext;

import java.util.Map;


public class JexlEvalExpression implements Expression {
    private final JexlEngine jexl;
    private String expresssion;

    public JexlEvalExpression(String expresssion) {
        this.expresssion = expresssion;

        JexlFeatures features = new JexlFeatures()
                .loops(false)
                .sideEffectGlobal(false)
                .sideEffect(false);

        // from an example, figure out what we want here for real
        JexlPermissions permissions = new JexlPermissions.ClassPermissions(java.net.URI.class);

        jexl = new JexlBuilder().features(features).permissions(permissions).create();
    }

    @Override
    public boolean isValid(FlowContext ctx) {
        // create the expression
        JexlExpression expr = jexl.createExpression(expresssion);

        // load our context in to the jexl one
        MapContext jexlCtx = new MapContext();
        for (Map.Entry<String, Object> entry : ctx.entries()) {
            jexlCtx.set(entry.getKey(), entry.getValue());
        }

        Object obj = expr.evaluate(jexlCtx);
        if (obj instanceof Boolean){
            return (Boolean) obj;
        }
        else {
            return obj != null;
        }
    }
}
