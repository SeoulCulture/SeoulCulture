package seoul.culture.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import seoul.culture.demo.domain.Culture;
import seoul.culture.demo.repository.CultureRepository;
import seoul.culture.demo.domain.Mood;
import seoul.culture.demo.domain.MoodType;
import seoul.culture.demo.repository.MoodRepository;
import seoul.culture.demo.service.api.APIReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final MoodRepository moodRepository;
    private final APIReader apiReader;
    private final CultureRepository cultureRepository;

    @Autowired
    public DataLoader(MoodRepository moodRepository, APIReader apiReader,
                      CultureRepository cultureRepository) {
        this.moodRepository = moodRepository;
        this.apiReader = apiReader;  // Config에서 먼저 key 세팅되는 건가? 왜지? 그게 우선인가?
        this.cultureRepository = cultureRepository;
    }

    @Override
    public void run(String... args) throws IOException {
        Arrays.stream(MoodType.values())
                .filter(moodType -> !moodRepository.existsByMood(moodType))
                .map(Mood::new)
                .forEach(moodRepository::save);


        // 추후 스케쥴링 대상임 - 매일 오후 9시에 정보 받아온다고 함
        log.debug("----------> 문화행사 API 불러오기 -> DB 세팅 중...");
        List<JsonNode> jsons = apiReader.getResult();
        List<Culture> cultures = jsons.stream()
                .map(json -> {
                    return Culture.of(json);
                })
                .collect(Collectors.toList());
//        cultures.stream().filter(culture -> !cultureRepository.existsByTitleAndLocation(
        // TODO: location이 동일하고, title이 다른 경우는.. 지도상에 겹치는데 그거 클릭 가능하게 처리하려면? (붙어있는 경우도 마찬가지긴 함)
        cultures.stream().filter(culture -> !cultureRepository.existsByLocation(
                culture.getLocation()
        )).forEach(cultureRepository::save);
    }
}
