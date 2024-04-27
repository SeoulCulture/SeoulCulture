package seoul.culture.demo.pathfinder;

import java.io.IOException;
import java.util.Map;

public interface PathFinder {
    void setPathInfo(double lat1, double lon1, double lat2, double lon2, HowToGo howToGo) throws IOException;

    int getDuration() throws IOException;

    int getDistance();

    Map<String, String> getSrcAddress() throws IOException;
    Map<String, String> getDstAddress() throws IOException;

}