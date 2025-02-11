module ReccurentSet {
    requires commons.math3;
    requires org.slf4j;
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
    requires org.hibernate.orm.core;
    requires flyway.core;
    requires spring.data.jpa;
    requires spring.tx;
    requires spring.jdbc;
    requires spring.boot.starter.data.jpa;
    requires spring.aop;
    requires spring.core;
    requires javafx.swing;


    exports org.zeros.recurrent_set_2;
    exports org.zeros.recurrent_set_2.JavaFxControllers;
    exports org.zeros.recurrent_set_2.Views;
    exports org.zeros.recurrent_set_2.Configuration;
    exports org.zeros.recurrent_set_2.ImageGeneration;
    exports org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations;
    exports org.zeros.recurrent_set_2.Model;
    exports org.zeros.recurrent_set_2.EquationParser;
    exports org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;
    exports org.zeros.recurrent_set_2.EquationParser.EquationTreeSimplifier;
    exports org.zeros.recurrent_set_2.EquationParser.OneFactorCalculation;
    exports org.zeros.recurrent_set_2.EquationParser.TwoFactorsCalculation;
    exports org.zeros.recurrent_set_2.Database.Repositories;
    exports org.zeros.recurrent_set_2.Database.Services;
    exports org.zeros.recurrent_set_2.Util;
    exports org.zeros.recurrent_set_2.ImageSave;


    opens org.zeros.recurrent_set_2;
    opens org.zeros.recurrent_set_2.Model;
    opens org.zeros.recurrent_set_2.ImageSave;
    opens org.zeros.recurrent_set_2.Views;
    opens org.zeros.recurrent_set_2.Database.Repositories;
    opens org.zeros.recurrent_set_2.Database.Services;
    opens org.zeros.recurrent_set_2.Configuration;
    opens org.zeros.recurrent_set_2.ImageGeneration to javafx.graphics, spring.context, spring.core;
    opens org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations to javafx.graphics, spring.context, spring.core;
    opens db.migration;
    opens org.zeros.recurrent_set_2.Util to jakarta.persistence, javafx.graphics, org.hibernate.orm.core, spring.context, spring.core;
}
