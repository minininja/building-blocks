# Flow

The intent is to implement what we'd consider to be workflows but without any reliance upon people or time passage.

This leaves us with 3 basic building blocks.  

* A flow, in its simplest terms, is an expression resulting in a boolean with an associated action to be executed.
* An expression, as mentioned above, an expression is the conditional part of an if statement.
* An action to be performed should the expression result in "true".

This by itself is relatively boring and is just a simple if structure.  But this is further extended by defining a flow
as an action which in turn allows nesting of flows within actions.

Even with flows being defined as actions to allow nesting it's still relatively boring.  Where this becomes more 
interesting, potentially, is through the use of a DSL/builder to assemble the flow.  This assembly can be passed
to less technical folk to validate that the process is correct/valid.

## Classes
![](flow-api/src/main/uml/classes.png)