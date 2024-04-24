package seoul.culture.demo.service.distance;

import java.io.IOException;

public interface PathFinder {
    void loadPathInfo(double lat1, double lon1, double lat2, double lon2, HowToGo howToGo) throws IOException;

    int getDuration() throws IOException;

    int getDistance();
}