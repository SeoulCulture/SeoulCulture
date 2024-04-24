package seoul.culture.demo.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import seoul.culture.demo.service.distance.NaverPathFinder;

@Configuration
@RequiredArgsConstructor
@ComponentScan
public class Config {
    private final SeoulEventAPIReader apiReader;
    private final NaverPathFinder pathFinder;
}
