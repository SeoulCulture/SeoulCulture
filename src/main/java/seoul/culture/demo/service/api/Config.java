package seoul.culture.demo.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈  & 싱글톤 보장
@RequiredArgsConstructor
@EnableConfigurationProperties(SeoulEventAPIReader.class)  // 설정정보인 key 를 주입하기 위한 Config 파일
public class Config {
    private final SeoulEventAPIReader seoulEventAPIReader;
}
