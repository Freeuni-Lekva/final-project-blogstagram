package org.blogstagram.errors;

public class VariableError implements GeneralError {

    private String variableName;
    private String errorMessage;

    public String getVariableName() {
        return variableName;
    }
    public void setVariableName(String variableName){
        this.variableName = variableName;
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

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}