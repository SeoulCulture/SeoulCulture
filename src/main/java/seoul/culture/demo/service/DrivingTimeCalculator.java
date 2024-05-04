package seoul.culture.demo.service;

public final class DrivingTimeCalculator {

    private static final int SPEED = 40;

    public static double calculateTime(double distanceKm) {
        return distanceKm / SPEED * 60;
    }

}