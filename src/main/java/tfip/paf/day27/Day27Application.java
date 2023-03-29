package tfip.paf.day27;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tfip.paf.day27.Services.ShowService;

@SpringBootApplication
public class Day27Application implements CommandLineRunner{

	@Autowired
	private ShowService showSvc;

	public static void main(String[] args) {
		SpringApplication.run(Day27Application.class, args);
	}

	@Override
	public void run(String... args) {
		// showSvc.findShowsByType("Animation");
		// showSvc.groupShowsByTimezone();
		// showSvc.summarizeShows("Animation");
	}

}
