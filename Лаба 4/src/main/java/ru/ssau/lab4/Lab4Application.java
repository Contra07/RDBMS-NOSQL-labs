package ru.ssau.lab4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan("ru.ssau.lab4")
@SpringBootApplication
public class Lab4Application 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(Lab4Application.class, args);
	}
}