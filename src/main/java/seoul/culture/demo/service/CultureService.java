package seoul.culture.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.culture.demo.dto.CultureInfoDto;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.repository.CultureRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CultureService {
    private final CultureRepository cultureRepository;
    private static final String CULTURE_INFO_PATH = "src/main/resources/static/data.json";

    @Transactional
    public void cultureRegister(){
        // 가장 먼저 json 데이터를 읽어온다. - 읽을 때, DTO로 읽어오자.
        List<CultureInfoDto> cultureInfo = readCultureInfo(CULTURE_INFO_PATH);

        // 읽어온 데이터를 culture 엔티티로 바꿔서 등록한다.
        cultureInfo.stream()
                .map(Culture::new)
                .forEach(cultureRepository::save);
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

}
