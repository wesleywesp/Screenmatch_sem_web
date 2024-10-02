package pt.com.wesleywesp.Screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.com.wesleywesp.Screenmatch.principal.Principal;


@SpringBootApplication

public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		Principal principal= new Principal();
		principal.exibirMenu();

	}
}
