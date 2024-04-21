package seoul.culture.demo.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import seoul.culture.demo.service.distance.CarPathFinder;

@Configuration // 빈  & 싱글톤 보장
@RequiredArgsConstructor
@EnableConfigurationProperties({SeoulEventAPIReader.class, CarPathFinder.class})  // 설정정보인 key 를 주입하기 위한 Config 파일
// ㄴ> 왜 꼭 이렇게 매번 구체적으로 지정해줘야할까? 다른 방법은 없을까? 인터페이스 명시는 안되던데..
// ㄴ> 키 필드가 있어야 하기 때문?
public class Config {
    private final SeoulEventAPIReader apiReader;
    private final CarPathFinder pathFinder;
}
