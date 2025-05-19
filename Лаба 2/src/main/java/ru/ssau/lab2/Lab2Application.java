package ru.ssau.lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringProperties;
import org.springframework.shell.command.annotation.CommandScan;
@CommandScan("ru.ssau.lab2")
@SpringBootApplication
public class Lab2Application 
{
	public static void main(String[] args) 
	{
		SpringProperties.setProperty("spring.jdbc.getParameterType.ignore", "true");
		SpringApplication.run(Lab2Application.class, args);
	}
}