package org.zeros.recurrent_set_2.EquationParser;

import java.util.*;

public class AllowedCharacters {
    public static final Set<String> BRACKETS = Set.of("(", ")","[", "]","{", "}");
    static final Set<String> ALTERNATIVE_STARTING_BRACKETS = Set.of("(","[","{");
    static final Set<String> ALTERNATIVE_CLOSING_BRACKETS = Set.of(")","]","}");
    public static final Set<String> OPERATORS = Set.of("^", "*", "/","+", "-");
    public static final Set<String> NUMBERS=Set.of( "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    public static final Set<String> DIVISORS=Set.of(".");
    public static final Set<String> CONSTANT =Set.of("e","i","PI");
    public static final Set<String> FUNCTIONS=Set.of("sin","cos","tg","ctg");
    public static final ArrayList<String> OPERATORS_IN_ORDER= new ArrayList<>(Arrays.asList("^", "*", "/","+", "-"));
}
