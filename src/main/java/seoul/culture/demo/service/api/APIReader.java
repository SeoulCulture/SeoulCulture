package seoul.culture.demo.service.api;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public interface APIReader {

    List<JsonNode> getResult() throws IOException;
}
