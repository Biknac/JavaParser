package com.Parser.Bernatsky_12220sk1_Parser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bernatsky12220sk1ParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(Bernatsky12220sk1ParserApplication.class, args);
		WebDriverManager.chromedriver().setup();
	}

}
