package seoul.culture.demo.service;

public class WalkingTimeCalculator {
    private static final int SPEED = 4;

    public static double calculateTime(double distanceKm) {
        return distanceKm / SPEED * 60;
    }

}
