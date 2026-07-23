package ec.edu.ups.icc.fundamentos01;

import ec.edu.ups.icc.fundamentos01.security.config.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class Fundamentos01Application {

	public static void main(String[] args) {
		SpringApplication.run(Fundamentos01Application.class, args);
	}

}
