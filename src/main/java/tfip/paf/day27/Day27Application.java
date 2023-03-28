package tfip.paf.day27;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import tfip.paf.day27.Repositories.TaskRepository;

@SpringBootApplication
public class Day27Application implements CommandLineRunner{

	@Autowired
	private TaskRepository taskRepo;

	public static void main(String[] args) {
		SpringApplication.run(Day27Application.class, args);
	}

	@Override
	public void run(String... args) {
		JsonObject task = Json.createObjectBuilder()
			.add("task","Watch JW4")
			.add("priority",5)
			.add("dueDate","2023-04-01")
			.build();

		taskRepo.updateActivity();
		taskRepo.deleteActivitiesWithoutTask();
		Document result = taskRepo.insertTask(task);
		System.out.printf(">>> inserted result: ", result);
		
	}

}
