package com.github.magrifle.data.searchapi;

import java.util.HashMap;
import java.util.Map;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

    public static final Map<Character, SearchOperation> SIMPLE_OPERATION_SET;

    static {
        SIMPLE_OPERATION_SET = new HashMap<>();
        SIMPLE_OPERATION_SET.put(':', EQUALITY);
        SIMPLE_OPERATION_SET.put('!', NEGATION);
        SIMPLE_OPERATION_SET.put('>', GREATER_THAN);
        SIMPLE_OPERATION_SET.put('<', LESS_THAN);
        SIMPLE_OPERATION_SET.put('~', LIKE);
    }
}