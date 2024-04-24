package seoul.culture.demo.service.distance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import seoul.culture.demo.YamlPropertySourceFactory;

@Component @Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "nmap")
@PropertySource(value = "classpath:key.yml", factory = YamlPropertySourceFactory.class)
public class NaverConfig {
    private String id;
    private String secret;
}