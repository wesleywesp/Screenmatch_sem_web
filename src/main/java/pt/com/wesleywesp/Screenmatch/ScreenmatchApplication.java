package pt.com.wesleywesp.Screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.com.wesleywesp.Screenmatch.principal.Principal;
import pt.com.wesleywesp.Screenmatch.repository.SerieRepository;


@SpringBootApplication

public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		Principal principal= new Principal(repository);
		principal.exibeMenu();

	}
}
