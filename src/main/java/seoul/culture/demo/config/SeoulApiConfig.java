package seoul.culture.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import seoul.culture.demo.YamlPropertySourceFactory;

@Component
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "seoul")
@PropertySource(value = "classpath:key.yml", factory = YamlPropertySourceFactory.class)
public class SeoulApiConfig {
    private String key;
}
