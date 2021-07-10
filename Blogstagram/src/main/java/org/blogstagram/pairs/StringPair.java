package org.blogstagram.pairs;

public class StringPair implements GeneralPair{

    private String key;
    private String value;

    public StringPair(String key,String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
