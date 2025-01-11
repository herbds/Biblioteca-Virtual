package com.BiblioChallengue.Biblio;

import com.BiblioChallengue.Biblio.Principal.Principal;
import com.BiblioChallengue.Biblio.repository.DuplicadoService;
import com.BiblioChallengue.Biblio.repository.repositorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiblioApplication implements CommandLineRunner {

	@Autowired
	private repositorioService repositorio;
	@Autowired
	private DuplicadoService repForAutores;
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio, repForAutores);
		principal.ejecutarMenu();
	}

	public static void main(String[] args) {
		SpringApplication.run(BiblioApplication.class, args);
	}
}
