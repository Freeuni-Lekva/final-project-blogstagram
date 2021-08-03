package org.blogstagram.pairs;

public class StringPair implements GeneralPair{

    private final String key;
    private final String value;

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
