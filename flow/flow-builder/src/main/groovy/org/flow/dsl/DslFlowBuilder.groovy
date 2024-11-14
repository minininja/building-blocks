package org.flow.dsl

import groovy.yaml.YamlSlurper
import org.dorkmaster.flow.Action
import org.dorkmaster.flow.Expression
import org.dorkmaster.flow.Flow
import org.dorkmaster.flow.builder.FlowBuilder
import org.dorkmaster.flow.builder.Provider

class DslFlowBuilder implements FlowBuilder {
    Collection providers = [new DefaultProvider()]

    FlowBuilder register(Provider provider) {
        providers.add(provider)
    }

    Map<String, Flow> flows = new HashMap<>()

    FlowBuilder load(InputStream in) {
        def config = new YamlSlurper().parse(in)

        config.providers.each { providerClazz ->
            providers << (Provider) Class.forName(providerClazz).newInstance()
        }

        config.flows.each { root ->
            def name = root.keySet().iterator().next()

            // at the top level there must be a flow and an action, expressions are optional
            def flow = findFlow(root."${name}".flow)
            def expression = root."${name}".expression ? findExpression(root."${name}".expression.toString()) : Provider.nullExpression()
            def action = findAction(root."${name}".action)

            flow.expression = expression
            flow.action = action

            flows.put(name, flow)
        }
        this
    }

    Flow build(String name) {
        return flows."${name}"
    }

    Flow findFlow(String name) {
        Flow result = null
        providers.each { p ->
            Flow tmp = (Flow) p.flowInstance(name)
            if (tmp) {
                result = tmp
            }
        }
        result
    }

    Expression findExpression(String name) {
        Expression result = null
        providers.each { p ->
            Expression tmp = (Expression) p.expressionInstance(name)
            if (tmp) {
                result = tmp
            }
        }
        result
    }

    Action findAction(String name) {
        Action result = null
        providers.each { p ->
            Action tmp = (Action) p.actionInstance(name)
            if (tmp) {
                result = tmp
            }
        }
        result
    }
}