package org.blogstagram.pairs;

public class StringKeyPair<T> implements GeneralPair{

    private final String key;
    private final T value;

    public StringKeyPair(String key,T value){
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
