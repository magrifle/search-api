package com.mightwork.searchapi.specification;

public enum SearchParameterSeparator {

    COMMA(','), PLUS('+');

    private final char value;

    SearchParameterSeparator(char c) {
        this.value = c;
    }

    public char getValue() {
        return this.value;
    }
}