package org.blogstagram.pairs;

public class StringKeyPair<T> implements GeneralPair{

    private String key;
    private T value;

    private StringKeyPair(String key,T value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getValue() {
        return value;
    }
}
