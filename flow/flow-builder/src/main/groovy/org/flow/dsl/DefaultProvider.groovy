package org.flow.dsl

import org.dorkmaster.flow.Action
import org.dorkmaster.flow.Expression
import org.dorkmaster.flow.Flow
import org.dorkmaster.flow.builder.Provider


class DefaultProvider implements Provider {

    def flows = [simple: Flow.class]
    def expressions = [and: AndExpression.class, not: NotExpression.class, or: OrExpression.class]
    def actions = [composite: CompositeAction.class]

    @Override
    String namespace() {
        return "default"
    }

    @Override
    Flow flowInstance(String name) {
        def clazz = flows."${name}"
        if (clazz) {
            return clazz.newInstance()
        }
        return null
    }

    @Override
    Expression expressionInstance(String name) {
        def clazz = expressions."${name}"
        if (clazz) {
            return clazz.newInstance()
        }
    }

    @Override
    Action actionInstance(String name) {
        def clazz = actions."${name}"
        if (clazz) {
            return clazz.newInstance()
        }
    }
}
