package org.zeros.recurrent_set_2.EquationParserTests;

import org.apache.commons.math3.complex.Complex;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.ConstantNode;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.EquationTreeNode;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorImpl;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreatorImpl;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class EquationParserTest {

    private double maxDivergence= 10E-7;
    @Autowired
    private ExpressionCalculatorCreatorImpl creator;
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testEquationParser_SimpleVariable() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "51aPI",
                Set.of("a"));
        assertThat(expressionCalculator).isNotNull();
        assertThat(expressionCalculator.compute(Map.of("a", Complex.valueOf(1)))).isEqualTo(Complex.valueOf(51*Math.PI));
    }

    @Test
    public void testEquationParser_SimpleVariable2() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "51.21aPIex",
                Set.of("a","x"));
        assertThat(expressionCalculator).isNotNull();
        assertThat(expressionCalculator.compute(Map.of("a", Complex.valueOf(1),
                "x", Complex.valueOf(1)
        ))).isEqualTo(Complex.valueOf(51.21*Math.PI*Math.E));
    }

    @Test
    public void testEquationParser_SimpleOperations() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "51.21*aPIex+xa",
                Set.of("a","x"));
        assertThat(expressionCalculator).isNotNull();
        assertThat(expressionCalculator.compute
                (Map.of("a", Complex.valueOf(1),
                        "x", Complex.valueOf(1)))
                .abs()
                -Complex.valueOf(51.21*Math.PI*Math.E+1)
                .abs()
        ).isCloseTo(0.0, Offset.offset(maxDivergence));
    }

    @Test
    public void testEquationParser_SimpleOperations2() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "5 1. 21*a PI e x + x   a/21e   i +sin  (5x)",
                Set.of("a"," x"));
        assertThat(expressionCalculator).isNotNull();
        assertThat(expressionCalculator.compute
                        (Map.of("a", Complex.valueOf(1),
                                "x", Complex.valueOf(1)))
                .abs()
                -Complex.valueOf(51.21*Math.PI*Math.E)
                .add(Complex.valueOf(1).divide(new Complex(0, 21*Math.E)))
                .add(Complex.valueOf(5).sin())
                .abs()
        ).isCloseTo(0.0, Offset.offset(maxDivergence));
    }

    @Test
    public void testEquationParser_SimpleOperations3() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "5 1. 21*a PI e x^3.1 + x   a/21e   i +sin  (5x)",
                Set.of("a"," x"));
        assertThat(expressionCalculator).isNotNull();
        assertThat(expressionCalculator.compute
                        (Map.of("a", Complex.valueOf(1),
                                "x", Complex.valueOf(1)))
                .abs()
                -Complex.valueOf(51.21*Math.PI*Math.E)
                .add(Complex.valueOf(1).divide(new Complex(0, 21*Math.E)))
                .add(Complex.valueOf(5).sin())
                .abs()
        ).isCloseTo(0.0, Offset.offset(maxDivergence));
    }



    @Test
    public void testEquationParser_SimpleOperations5() {
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "(a5x^5)-(5x^(3x))",
                Set.of("a"," x"));
        assertThat(expressionCalculator).isNotNull();
        Complex testValue = Complex.valueOf(2, 21);
        Complex valueFromCalculator=expressionCalculator.compute
                (Map.of("a", testValue,
                        "x", testValue));
        Complex valueExpected= testValue.multiply(Complex.valueOf(5))
                .multiply(testValue.pow(5))
                .subtract(Complex.valueOf(5)
                        .multiply(testValue.pow(testValue.multiply(3))));

        System.out.println("value:"+valueFromCalculator.toString()+"expected: " +valueExpected.toString());

        assertThat(valueFromCalculator.getReal())
                .isCloseTo(valueExpected.getReal(), Offset.offset(maxDivergence));
        assertThat(valueFromCalculator.getImaginary())
                .isCloseTo(valueExpected.getImaginary(), Offset.offset(maxDivergence));
    }



    @Test
    public void testEquationParser_TreeSimplificationTest1() throws NoSuchFieldException, IllegalAccessException {
        Field field = ExpressionCalculatorImpl.class.getDeclaredField("equationTree");
        field.setAccessible(true);
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "5/32-44*(4^3)+(26+i)+2+1+2+1-2-1-2-1",
                Set.of());
        assertThat(expressionCalculator).isNotNull();
        Complex valueFromCalculator=expressionCalculator.compute
                (Map.of());
        Complex valueExpected = Complex.valueOf(5).divide(32.0)
                .subtract(Complex.valueOf(44).multiply(Math.pow(4, 3)))
                .add(Complex.valueOf(26,1));


        System.out.println("value:"+valueFromCalculator.toString()+"expected: " +valueExpected.toString());
        EquationTreeNode equationTree=(EquationTreeNode) field.get(expressionCalculator);
        assertThat(valueFromCalculator.getReal())
                .isCloseTo(valueExpected.getReal(), Offset.offset(maxDivergence));
        assertThat(valueFromCalculator.getImaginary())
                .isCloseTo(valueExpected.getImaginary(), Offset.offset(maxDivergence));
        assertThat(equationTree instanceof ConstantNode).isTrue();
    }
    /*
    @Test
    public void testEquationParser_TreeSimplificationTest2() throws NoSuchFieldException, IllegalAccessException {
        Field field = ExpressionCalculator.class.getDeclaredField("equationTree");
        field.setAccessible(true);
        ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
                "511x/32-44*(4^3)+(26+i)",
                Set.of());
        assertThat(expressionCalculator).isNotNull();
        Complex valueFromCalculator=expressionCalculator.compute
                (Map.of());
        Complex valueExpected = Complex.valueOf(5).divide(32.0)
                .subtract(Complex.valueOf(44).multiply(Math.pow(4, 3)))
                .add(Complex.valueOf(26,1));


        System.out.println("value:"+valueFromCalculator.toString()+"expected: " +valueExpected.toString());
        EquationTreeNode equationTree=(EquationTreeNode) field.get(expressionCalculator);
        assertThat(valueFromCalculator.getReal())
                .isCloseTo(valueExpected.getReal(), Offset.offset(maxDivergence));
        assertThat(valueFromCalculator.getImaginary())
                .isCloseTo(valueExpected.getImaginary(), Offset.offset(maxDivergence));
        assertThat(equationTree instanceof ConstantNode).isTrue();
    }*/
}
