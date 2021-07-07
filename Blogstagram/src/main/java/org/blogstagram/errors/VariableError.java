package org.blogstagram.errors;

public class VariableError {

    private String variableName;
    private String errorMessage;

    public String getVariableName() {
        return variableName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public VariableError(String variableName, String errorMessage) {
        this.variableName = variableName;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString(){
        String str = variableName + " -> " + errorMessage;
        return str;
    }
}
