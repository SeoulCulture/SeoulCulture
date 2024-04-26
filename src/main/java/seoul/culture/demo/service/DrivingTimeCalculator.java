package seoul.culture.demo.service;

public final class DrivingTimeCalculator {

    private static final int SPEED = 50;

    public static double calculateDrivingTime(double distanceKm) {
        return distanceKm / SPEED * 60;
    }

}