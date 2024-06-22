package seoul.culture.demo.confusion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

// Entity에서 @Convert를 달아놓은 List타입 컬럼에, 자동으로 List와 String의 변환을 도움 (저 두개의 오버라이딩이)
// 리스트 타입은 결국, String타입의 JSON형식으로 저장이 된다.
// 편한 점은, db에서 다시 그 String을 꺼내오면 리스트 형식으로 꺼낼수있다.
// Converter는 편하구나
@Converter
public class AreaListConverter implements AttributeConverter<List<String>, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
