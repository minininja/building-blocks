package org.dorkmaster.flow;

import java.io.*;

import org.dorkmaster.flow.action.CompositeAction;
import org.dorkmaster.flow.expression.AndExpression;
import org.dorkmaster.flow.expression.JexlEvalExpression;
import org.dorkmaster.flow.expression.NotExpression;
import org.dorkmaster.flow.expression.OrExpression;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.*;

public class FlowBuilder {
    protected static Map<String, ExpressionBuilder> expressions = new HashMap<>();
    protected static Map<String, ActionBuilder> actions = new HashMap<>();

    static {
        expressions.put("and", new ExpressionBuilder() {
            @Override
            public Expression build(FlowBuilder builder, Map<String, Object> data) {
                Object tmp = builder.parseExpression(data.get("expressions"));
                if (tmp instanceof Expression){
                    return (Expression) tmp;
                } else {
                    return new AndExpression((List<Expression>) tmp);
                }
            }
        });
        expressions.put("or", new ExpressionBuilder() {
            @Override
            public Expression build(FlowBuilder builder, Map<String, Object> data) {
                Object tmp = builder.parseExpression(data.get("expressions"));
                if (tmp instanceof Expression){
                    return (Expression) tmp;
                } else {
                    return new OrExpression((List<Expression>) tmp);
                }
            }
        });
        expressions.put("not", new ExpressionBuilder() {
            @Override
            public Expression build(FlowBuilder builder, Map<String, Object> data) {
                Object tmp = builder.parseExpression(data.get("expressions"));
                if (tmp instanceof Expression) {
                    return new NotExpression((Expression) tmp);
                } else {
                    return new NotExpression(
                            new AndExpression((List<Expression>) tmp)
                    );
                }
            }
        });
        expressions.put("jexl", new ExpressionBuilder() {
            @Override
            public Expression build(FlowBuilder builder, Map<String, Object> data) {
                return new JexlEvalExpression((String) data.get("jexl"));
            }
        });
    }

    public static void registerExpressionBuilder(String name, ExpressionBuilder expression) {
        expressions.put(name, expression);
    }

    public static void registerActionBuilder(String name, ActionBuilder action) {
        actions.put(name, action);
    }

    public static FlowBuilder from(String input) {
        Yaml yaml = new Yaml();

        // should be in the jar file in most cases
        try (InputStream in = FlowBuilder.class.getResourceAsStream(input)) {
            return new FlowBuilder(yaml.load(in));
        } catch (NullPointerException | IOException e) {
            // noop
            System.out.println("exception:" + e);
        }

        // fallback to external files
        try (InputStream in = new FileInputStream(input)) {
            return new FlowBuilder(yaml.load(in));
        } catch (NullPointerException | IOException e) {
            // noop
            System.out.println("exception:" + e);
        }

        return null;
    }

    protected Map<String, Object> yaml;

    private FlowBuilder() {
        // disallowed
    }

    protected FlowBuilder(Map<String, Object> yaml) {
        this.yaml = (Map<String, Object>) yaml.get("flows");
    }

    public Flow parseFlow(Map<String, Object> yaml) {
        Expression expr = null;

        List<Expression> expressions = parseExpression(yaml.get("expressions"));
        if (!expressions.isEmpty() && expressions.size() == 1) {
            expr = expressions.get(0);
        } else {
            // log that its goofed up, should be always one
        }

        return new Flow()
                .setExpression(expr)
                .setAction(parseAction(yaml.get("actions")));
    }

    public List<Expression> parseExpression(Object tmp) {
        List<Expression> result = new LinkedList<>();
        if (tmp instanceof Collection) {
           for (Object child : (Collection) tmp) {
               if (child instanceof Map) {
                   Map<String, Object> expr = (Map<String, Object>) child;
                   ExpressionBuilder builder = expressions.get((String) expr.get("type"));
                   if (null != builder){
                       result.add(builder.build(this, expr));
                   }
               }
           }
        }
        return result;
    }

    public Action parseAction(Object tmp) {
        List<Action> result = new LinkedList<>();
        if (tmp instanceof Collection) {
            for (Object child : (Collection) tmp) {
                if (child instanceof Map) {
                    Map<String, Object> expr = (Map<String, Object>) child;
                    ActionBuilder builder = actions.get((String) expr.get("type"));
                    if (null != builder){
                        result.add(builder.build(this, expr));
                    }
                }
                else if (child instanceof String) {
                    ActionBuilder builder = actions.get((String) child);
                    if (null != builder){
                        result.add(builder.build(this, Collections.emptyMap()));
                    }
                }
            }
        }
        return result.isEmpty() ? null : result.size() == 1 ? result.get(0) : new CompositeAction(result);
    }

    public Flow getFlow(String name) {
        return parseFlow((Map<String,Object>) yaml.get(name));
    }
}
