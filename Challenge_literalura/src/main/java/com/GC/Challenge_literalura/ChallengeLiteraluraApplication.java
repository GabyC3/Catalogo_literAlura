package com.GC.Challenge_literalura;

import com.GC.Challenge_literalura.Principal.Principal;
import com.GC.Challenge_literalura.Repository.IAutoresRepository;
import com.GC.Challenge_literalura.Repository.ILibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private IAutoresRepository autoresRepository;
	@Autowired
	private ILibrosRepository librosRepository;
	public static void main(String[] args) {

		SpringApplication.run(ChallengeLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(autoresRepository, librosRepository);
		principal.Menu();

	}
}
