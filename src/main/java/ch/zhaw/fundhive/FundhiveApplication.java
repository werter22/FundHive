package ch.zhaw.fundhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FundhiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundhiveApplication.class, args);
	}

}
