package seoul.culture.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.culture.demo.dto.CultureInfoDto;
import seoul.culture.demo.entity.Category;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.entity.mark.CulturePlace;
import seoul.culture.demo.repository.CultureEventRepository;
import seoul.culture.demo.repository.CulturePlaceRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CultureService {
    private final CulturePlaceRepository culturePlaceRepository;
    private final CultureEventRepository cultureEventRepository;

    private static final String CULTURE_INFO_PATH = "src/main/resources/static/data.json";

    @Transactional
    public void registerCulturePlace(){
        // 가장 먼저 json 데이터를 읽어온다. - 읽을 때, DTO로 읽어오자.
        List<CultureInfoDto> cultureInfo = readCultureInfo(CULTURE_INFO_PATH);

        // 읽어온 데이터를 culture 엔티티로 바꿔서 등록한다.
        cultureInfo.stream()
                .filter(x -> !culturePlaceRepository.existsByTitleAndLocation(x.getTitle(), x.getLocation()))
                .filter(x -> !x.getTitle().endsWith("대학교"))
                .map(x -> {
                    if (x.getTitle().endsWith("박물관"))
                        x.setCategory(Category.박물관);
                    else if (x.getTitle().endsWith("극장"))
                        x.setCategory(Category.공연장);
                    else if (x.getTitle().endsWith("갤러리"))
                        x.setCategory(Category.전시);
                    else if (x.getTitle().contains("축제"))
                        x.setCategory(Category.축제);
                    return new CulturePlace(x);
                })
                .forEach(culturePlaceRepository::save);
    }

    private List<CultureInfoDto> readCultureInfo(String jsonPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 파일을 읽어와 JsonNode로 변환
            return Arrays.asList(objectMapper.readValue(new File(jsonPath), CultureInfoDto[].class));
        }catch(IOException e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("json 경로를 읽어올 수 없습니다.");
        }
    }

    @Transactional
    public void registerCultureEvent(List<CultureEvent> cultures) {
        // 데이터 전체 삭제 : 하루마다 문화행사 데이터는 갈아엎는다.
        cultureEventRepository.deleteAll();

        // TODO: location이 동일하고, title이 다른 경우는.. 지도상에 겹치는데 그거 클릭 가능하게 처리하려면? (붙어있는 경우도 마찬가지긴 함)
        cultures.stream().filter(culture -> !cultureEventRepository.existsByLocation(
                culture.getLocation()
        )).forEach(cultureEventRepository::save);
    }
}
