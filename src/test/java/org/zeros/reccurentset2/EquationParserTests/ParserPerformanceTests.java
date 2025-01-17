package org.zeros.reccurentset2.EquationParserTests;

import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeros.reccurentset2.EquationParser.ExpressionCalculator;
import org.zeros.reccurentset2.EquationParser.ExpressionCalculatorCreator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class ParserPerformanceTests {
    @Autowired
    private ExpressionCalculatorCreator creator;

@Test
    public void testParserPerformance() {

    long start = System.currentTimeMillis();
    Set<Complex> customCalculatorResults=new HashSet<>();
    ExpressionCalculator expressionCalculator=creator.getExpressionCalculator(
            "5 1.21*aPIex+xa/21ei+sin(5x)",
            Set.of("a"," x"));

    for (int i = 0; i < 10000; i++) {
        for (int j = 0; j < 1000; j++) {
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
        for (int j = 0; j < 1000; j++) {
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
}
