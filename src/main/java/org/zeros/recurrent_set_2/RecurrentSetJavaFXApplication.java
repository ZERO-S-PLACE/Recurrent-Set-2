package org.zeros.recurrent_set_2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.zeros.recurrent_set_2.Views.ViewFactory;


@Slf4j
public class RecurrentSetJavaFXApplication extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() {
		context = new SpringApplicationBuilder(RecurrentSetSpringBootApplication.class).run();
	}




	@Override
	public void stop() {
		context.close();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		log.atInfo().log("Starting Recurrent Set 2 Application -Java Fx");
		Platform.runLater(()->{
		context.getBean(ViewFactory.class).showNewWindow();
	});
	}
}




