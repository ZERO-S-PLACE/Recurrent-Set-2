package org.zeros.recurrent_set_2.EquationParser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.*;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeSimplifier.EquationTreeSimplifier;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ExpressionCalculatorCreator {


    /*
    ASSUMPTIONS:
    1.VALUES FOR FUNCTIONS HAVE TO BE IN BRACKETS
    2.VARIABLE CANNOT BE CALLED "e", "i"
    3.VALID DECIMAL SEPARATOR IS "."

    PROCESS IS DESIGN TO GET CALCULATIONS TREE
    WHICH ALLOWS TO EXECUTE EQUATION EFFICIENTLY FOR MANY VARIABLE VALUES
     */



    private final EquationTreeSimplifier equationTreeSimplifier;
    private  Set<String> variableNames;
    private Map<String, EquationTreeNode> subExpressionReplacements;
    private int assignedReplacementCounter = 0;

    public ExpressionCalculator getExpressionCalculator(@NonNull String expression,@NonNull Set<String> variableNames) {

        expression=validateExpression(expression);
        reinitializeTempVariables(variableNames);
        return new ExpressionCalculator(equationTreeSimplifier.simplify(parseExpression(expression)));
    }

    private void reinitializeTempVariables(@NotNull Set<String> variableNames) {
        this.variableNames = variableNames.stream().map(
                variableName -> variableName.replaceAll("\\s",""))
                .collect(Collectors.toSet());
        this.subExpressionReplacements = new HashMap<>();
        this.assignedReplacementCounter = 0;
    }

    private String validateExpression(@NonNull String expression) {
        if(expression.isBlank()){
            throw new IllegalArgumentException("Expression is blank");
        }
        expression=expression.replaceAll("\\s","");
        expression =unifyBracketsSymbols(expression);
        return expression;
    }
    private String unifyBracketsSymbols(String expression) {
        for(String bracket:AllowedCharacters.ALTERNATIVE_STARTING_BRACKETS) {
            expression = expression.replaceAll(Pattern.quote(bracket), "(");
        }
        for(String bracket:AllowedCharacters.ALTERNATIVE_CLOSING_BRACKETS) {
            expression = expression.replaceAll(Pattern.quote(bracket), ")");
        }
        return expression;
    }

    private EquationTreeNode parseExpression(String expression) {

        while (expression.contains("(")){
            expression= simplifyBrackets(expression);
        }
        if(containsAny(expression,AllowedCharacters.OPERATORS)){
            return simplifyOperation(expression);
        }
        return findValue(expression);
    }


    private String simplifyBrackets(String expression) {

        int firstBracketIndex = expression.indexOf('(');
        int closingBracketIndex= getClosingBracketIndex(expression,firstBracketIndex);
        String functionName= getFunctionBeforeBrackets(expression,firstBracketIndex);
        int subexpressionWithFunctionStartingIndex =firstBracketIndex-functionName.length();
        String innerExpression= expression.substring(firstBracketIndex+1,closingBracketIndex);
        String replacementName= getNextReplacementName();
        String simplifiedExpression=expression.substring(0,subexpressionWithFunctionStartingIndex)
                +replacementName
                +((closingBracketIndex + 1 < expression.length())
                ? expression.substring(closingBracketIndex + 1)
                : "");
        if(functionName.isBlank()) {
            subExpressionReplacements.put(replacementName,parseExpression(innerExpression));
        }else {
            subExpressionReplacements.put(replacementName,
                    new OneFactorNode(parseExpression(innerExpression),
                            CharactersValues.FUNCTIONS_CALCULATORSget(functionName)
                            ));
        }

        return simplifiedExpression;
    }

    private int getClosingBracketIndex(String expression, int firstBracketIndex) {
       int startingBracketsCount=1;
       int closingBracketCount=0;
       int index=firstBracketIndex+1;
       while(index<expression.length()) {
           if(expression.charAt(index)=='(') {
               startingBracketsCount++;
           }else if(expression.charAt(index)==')') {
               closingBracketCount++;
           }
           if(closingBracketCount==startingBracketsCount) {
               return index;
           }
           index++;
       }
       throw new IllegalArgumentException("Invalid expression");
    }

    private String getFunctionBeforeBrackets(String expression, int firstBracketIndex) {
        for (String functionName:AllowedCharacters.FUNCTIONS){
            if(firstBracketIndex-functionName.length()>=0) {
                if(expression.startsWith(functionName, firstBracketIndex-functionName.length())) {
                    return functionName;
                }
            }
        }
        return "";
    }


    private EquationTreeNode simplifyOperation(String expression) {
        while (expression.contains("^")) {
            expression= simplifyPowers(expression);
        }
        return findValueOfExpressionWithoutPowers(expression);
    }

    private EquationTreeNode findValueOfExpressionWithoutPowers(String expression) {
        if(expression.substring(1).contains("-")||expression.contains("+")){
            int lastPlusMinusSignIndex= getLastPlusMinusSignIndex(expression);
            String sign= String.valueOf(expression.charAt(lastPlusMinusSignIndex));

                return new TwoFactorsNode(
                        findValueOfExpressionWithoutPowers(expression.substring(0,lastPlusMinusSignIndex)),
                        findValueOfMultiplicationDivisionExpression(expression.substring(lastPlusMinusSignIndex+1)),
                        CharactersValues.OPERATORS_CALCULATORSget(sign)
                );
        }
        return findValueOfMultiplicationDivisionExpression(expression);
    }

    private int getLastPlusMinusSignIndex(String expression) {
        expression = expression.substring(1,expression.length()-1);
        return Math.max(expression.lastIndexOf("+")+1,expression.lastIndexOf("-")+1);
    }

    private EquationTreeNode findValueOfMultiplicationDivisionExpression(String expression) {
        if(expression.contains("*")||expression.contains("/")){
            int firstMultiplyDivideSignIndex=getFirstMultiplyDivideSignIndex(expression);
            String sign= String.valueOf(expression.charAt(firstMultiplyDivideSignIndex));

            return new TwoFactorsNode(
                    findValue(expression.substring(0,firstMultiplyDivideSignIndex)),
                    findValueOfMultiplicationDivisionExpression(expression.substring(firstMultiplyDivideSignIndex+1)),
                    CharactersValues.OPERATORS_CALCULATORSget(sign)
            );
        }
        return findValue(expression);

    }
    private int getFirstMultiplyDivideSignIndex(String expression) {
        return Stream.of(expression.indexOf("*"), expression.indexOf("/"))
                .filter(index -> index != -1)
                .min(Integer::compare)
                .orElse(-1);
    }

    private String simplifyPowers(String expression) {
        int powerSymbolIndex = expression.indexOf('^');
        String baseOfPower= getLastValue(expression.substring(0,powerSymbolIndex));
        String exponent= getFirstValue(expression.substring(powerSymbolIndex+1));
        String replacementName= getNextReplacementName();
        String simplifiedExpression=expression.substring(0,powerSymbolIndex-baseOfPower.length())
                +replacementName
                + expression.substring(powerSymbolIndex + exponent.length()+1);

            subExpressionReplacements.put(replacementName,
                    new TwoFactorsNode(findValue(baseOfPower),
                            findValue(exponent),
                            CharactersValues.OPERATORS_CALCULATORSget("^")
                    ));

        return simplifiedExpression;


    }

    private @NotNull String getNextReplacementName() {
        assignedReplacementCounter++;
        return "|_" + assignedReplacementCounter + "_|";
    }

    private String getFirstValue(String expression) {
        String regex = "^([+-]?[0-9]*\\.?[0-9]+([eE][+-]?[0-9]+)?)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            return matcher.group(1);
        }
        for(String name:getAllPossibleVariableNames()){
            if(expression.startsWith(name)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Invalid expression");
    }

    private String getLastValue(String expression) {

        String regex = "([+-]?[0-9]*\\.?[0-9]+([eE][+-]?[0-9]+)?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
           return matcher.group(1);
        }

        for(String name:getAllPossibleVariableNames()){
            if(expression.endsWith(name)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Invalid expression");
    }

    private @NotNull Set<String> getAllPossibleVariableNames() {
        return Stream.of(AllowedCharacters.CONSTANT,
                        subExpressionReplacements.keySet(),
                        variableNames)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private EquationTreeNode findValue(String expression) {
        if(expression.equals("-")){
            return new ConstantNode(Complex.valueOf(-1));
        }
        if(containsAny(expression,subExpressionReplacements.keySet())){
            return findValueContainingReplacement(expression);
        }
        if(containsAny(expression,AllowedCharacters.CONSTANT)){
            return findValueContainingConstant(expression);
        }
        if(containsAny(expression,variableNames)){
            return findValueContainingVariable(expression);
        }
        return new ConstantNode(Complex.valueOf(Double.parseDouble(expression)));
    }

    private EquationTreeNode findValueContainingReplacement(String expression) {
        if(equalsAny(expression,subExpressionReplacements.keySet()))
        {
            return subExpressionReplacements.get(expression);
        }
        String replacement = findFirstMatchingElement(expression,subExpressionReplacements.keySet());
        int startIndex= expression.indexOf(replacement);
        String partBefore= expression.substring(0,startIndex);
        String partAfter= expression.substring(startIndex+replacement.length());
        if(partBefore.isBlank()){
            return new TwoFactorsNode(
                    subExpressionReplacements.get(replacement),
                    findValue(partAfter),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        if(partAfter.isBlank()){
            return new TwoFactorsNode(
                    findValue(partBefore),
                    subExpressionReplacements.get(replacement),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        return new TwoFactorsNode(
                new TwoFactorsNode(
                        findValue(partBefore),
                        subExpressionReplacements.get(replacement),
                        CharactersValues.OPERATORS_CALCULATORSget("*"))
                , findValue(partAfter),
                CharactersValues.OPERATORS_CALCULATORSget("*"));
    }

    private EquationTreeNode findValueContainingConstant(String expression) {
        if(equalsAny(expression,AllowedCharacters.CONSTANT))
        {
            return new ConstantNode(CharactersValues.CONSTANT_VALUES.get(expression));
        }
        String constant = findFirstMatchingElement(expression,AllowedCharacters.CONSTANT);
        int startIndex= expression.indexOf(constant);
        String partBefore= expression.substring(0,startIndex);
        String partAfter= expression.substring(startIndex+constant.length());
        if(partBefore.isBlank()){
            return new TwoFactorsNode(
                    new ConstantNode(CharactersValues.CONSTANT_VALUES.get(constant)),
                    findValue(partAfter),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        if(partAfter.isBlank()){
            return new TwoFactorsNode(
                    findValue(partBefore),
                    new ConstantNode(CharactersValues.CONSTANT_VALUES.get(constant)),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        return new TwoFactorsNode(
                new TwoFactorsNode(
                        findValue(partBefore),
                        new ConstantNode(CharactersValues.CONSTANT_VALUES.get(constant)),
                        CharactersValues.OPERATORS_CALCULATORSget("*"))
                , findValue(partAfter),
                CharactersValues.OPERATORS_CALCULATORSget("*"));
    }

    private EquationTreeNode findValueContainingVariable(String expression) {
        if(equalsAny(expression,variableNames))
        {
            return createVariableNode(expression);
        }
        String variable = findFirstMatchingElement(expression,variableNames);
        int startIndex= expression.indexOf(variable);
        String partBefore= expression.substring(0,startIndex);
        String partAfter= expression.substring(startIndex+variable.length());
        if(partBefore.isBlank()){
            return new TwoFactorsNode(
                    createVariableNode(variable),
                    findValue(partAfter),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        if(partAfter.isBlank()){
            return new TwoFactorsNode(
                    findValue(partBefore),
                    createVariableNode(variable),
                    CharactersValues.OPERATORS_CALCULATORSget("*"));
        }
        return new TwoFactorsNode(
                new TwoFactorsNode(
                        findValue(partBefore),
                        createVariableNode(variable),
                        CharactersValues.OPERATORS_CALCULATORSget("*"))
                , findValue(partAfter),
                CharactersValues.OPERATORS_CALCULATORSget("*"));
    }

    private VariableNode createVariableNode(String variableName) {

        return new VariableNode(variableName);
    }


    private static String findFirstMatchingElement(String expression, Set<String> strings) {
        return strings.stream().filter(expression::contains)
                .findFirst().orElseThrow();
    }

    private static boolean equalsAny(String expression, Set<String> strings) {
        return strings.stream().anyMatch(expression::equals);
    }

    private static boolean containsAny(String expression, Set<String> strings) {
        return strings.stream().anyMatch(expression::contains);
    }

}
