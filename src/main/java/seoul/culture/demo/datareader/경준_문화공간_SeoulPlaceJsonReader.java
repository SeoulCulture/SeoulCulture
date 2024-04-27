package seoul.culture.demo.datareader;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;


/**
 * TODO: Json파일 읽어서, 각각의 List<JsonNode> 를 리턴
 * 참고: 리턴받은 List<JsonNode>는, db에 저장하기 위해 쓰일 것이다.
 *       db에 저장하는 과정은 dataLoader에서 진행할 것이다.
 */
//
public class 경준_문화공간_SeoulPlaceJsonReader implements JsonReader{
    @Override
    public List<JsonNode> getResult() throws IOException {
        return null;
    }
}
