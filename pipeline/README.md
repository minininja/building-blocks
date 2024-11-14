# Pipeline

A pipeline consists of a number of stages to be executed sequentially.  Upon completion 
of all the stages a "renderer" is used to convert the context of the pipeline into the 
expected result of the pipeline.

Pipeline stages can be defined with "required" inputs as well fields that they 
provide, as outputs.  If these are specified then the pipeline will evaluate these
pre and post conditions throwing an exception if they are not met.