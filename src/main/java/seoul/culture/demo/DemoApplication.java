package seoul.culture.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import seoul.culture.demo.service.api.SeoulEventAPIReader;

@SpringBootApplication
@EnableConfigurationProperties({SeoulEventAPIReader.class})
//@ConfigurationPropertiesScan
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
