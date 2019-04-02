package com.test.sixpro.ui.attion.universal;

public abstract class FunctionHasParamHasResult<T,P> extends Function {

    public FunctionHasParamHasResult(String functionName) {
        super(functionName);
    }

    public abstract T function(P p);
}
