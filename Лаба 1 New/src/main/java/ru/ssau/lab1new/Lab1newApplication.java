package ru.ssau.lab1new;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan("ru.ssau.lab1new")
@SpringBootApplication
public class Lab1newApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab1newApplication.class, args);
	}

}
