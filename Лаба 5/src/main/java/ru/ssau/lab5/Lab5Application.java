package ru.ssau.lab5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan("ru.ssau.lab5")
@SpringBootApplication
public class Lab5Application 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(Lab5Application.class, args);
	}
}
