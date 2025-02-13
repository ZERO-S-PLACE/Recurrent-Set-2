package org.zeros.recurrent_set_2.EquationParserTests;

import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorImpl;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreatorImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class ParserPerformanceTests {
    @Autowired
    private ExpressionCalculatorCreatorImpl creator;


@Test
    public void testParserPerformance() {

    long start = System.currentTimeMillis();
    Set<Complex> customCalculatorResults=new HashSet<>();
    ExpressionCalculatorImpl expressionCalculator=creator.getExpressionCalculator(
            "5 1.21*aPIex+xa/21ei+sin(5x)",
            Set.of("a"," x"));

    for (int i = 0; i < 10000; i++) {
        for (int j = 0; j < 2000; j++) {
           customCalculatorResults.add(expressionCalculator.compute(
                    Map.of("a",new Complex(i,j),
                    "x",new Complex(j,i))));
        }
    }
    long end = System.currentTimeMillis();
    System.out.println("Elapsed time -custom parsed: " + (end - start));

    long start2 = System.currentTimeMillis();
    Set<Complex> defaultCalculatorResults=new HashSet<>();


    for (int i = 0; i < 10000; i++) {
        for (int j = 0; j < 2000; j++) {
            Complex x=new Complex(j,i);
            Complex a=new Complex(i,j);
           defaultCalculatorResults.add(
                   Complex.valueOf(51.21).multiply(a).multiply(Math.PI)
                           .multiply(Math.E).multiply(x).add(
                                   x.multiply(a).divide(21).multiply(Math.E)
                                           .multiply(Complex.I)
                           )
                           .add(x.add(5).sin())

           );
        }
    }
    long end2 = System.currentTimeMillis();
    System.out.println("Elapsed time -default parsed: " + (end2 - start2));
    System.out.println("Coefficient: " + (double)(end-start)/(end2 - start2));


}
    @Test
    public void testParserPerformanceMandelbrot() {


        long start2 = System.currentTimeMillis();

        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 1000; j++) {
                Complex z=Complex.ZERO;
                Complex p=new Complex(i,j).multiply(0.001);
                for (int k = 0; k < 100; k++) {
                    z=z.pow(2).add(p);
                }
            }
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Elapsed time -default parsed: " + (end2 - start2));

        ExpressionCalculatorImpl expressionCalculator=creator.getExpressionCalculator(
                "p+z^2",
                Set.of("p"," z"));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 1000; j++) {
                Complex z=Complex.ZERO;
                Complex p=new Complex(i,j).multiply(0.001);
                for (int k = 0; k < 100; k++) {
                    z=expressionCalculator.compute(
                            Map.of("p", p,
                                    "z",z));
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time -custom parsed: " + (end - start));


        System.out.println("Coefficient: " + (double)(end-start)/(end2 - start2));


    }


@Test
public void testParserPerformance3() {

    long start = System.currentTimeMillis();
    Set<Complex> customCalculatorResults=new HashSet<>();
    ExpressionCalculatorImpl expressionCalculator=creator.getExpressionCalculator(
            "5^(3x)-(2i+a)/sin(16ax)",
            Set.of("a"," x"));

    for (int i = 0; i < 10000; i++) {
        for (int j = 0; j < 2000; j++) {
            customCalculatorResults.add(expressionCalculator.compute(
                    Map.of("a",new Complex(i,j),
                            "x",new Complex(j,i))));
        }
    }
    long end = System.currentTimeMillis();
    System.out.println("Elapsed time -custom parsed: " + (end - start));

    long start2 = System.currentTimeMillis();
    Set<Complex> defaultCalculatorResults=new HashSet<>();


    for (int i = 0; i < 10000; i++) {
        for (int j = 0; j < 2000; j++) {
            Complex x=new Complex(j,i);
            Complex a=new Complex(i,j);
            defaultCalculatorResults.add(
                    Complex.valueOf(5).pow(x.multiply(3)).subtract(a.add(new Complex(0,2)).divide(
                            a.multiply(16).multiply(x).sin()
                            )));

        }
    }
    long end2 = System.currentTimeMillis();
    System.out.println("Elapsed time -default parsed: " + (end2 - start2));
    System.out.println("Coefficient: " + (double)(end-start)/(end2 - start2));


}
}
