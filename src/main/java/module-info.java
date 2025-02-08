module ReccurentSet {
    requires commons.math3;
    requires org.slf4j;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.jetbrains.annotations;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires micrometer.observation;
    requires spring.beans;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires aparapi;

    exports org.zeros.recurrent_set_2;
    exports org.zeros.recurrent_set_2.Controllers;
    exports org.zeros.recurrent_set_2.Views;
    exports org.zeros.recurrent_set_2.Configuration;
    exports org.zeros.recurrent_set_2.ImageGeneration;
    exports org.zeros.recurrent_set_2.Model;
    exports org.zeros.recurrent_set_2.EquationParser;
    exports org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;
    exports org.zeros.recurrent_set_2.EquationParser.EquationTreeSimplifier;
    exports org.zeros.recurrent_set_2.EquationParser.OneFactorCalculation;
    exports org.zeros.recurrent_set_2.EquationParser.TwoFactorsCalculation;

    opens org.zeros.recurrent_set_2 to spring.core, spring.context, javafx.graphics;
    opens org.zeros.recurrent_set_2.ImageGeneration to javafx.graphics, spring.context, spring.core;
}