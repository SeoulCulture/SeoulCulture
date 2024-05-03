package seoul.culture.demo.datareader;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public interface JsonReader {

    List getResult() throws IOException;
}
