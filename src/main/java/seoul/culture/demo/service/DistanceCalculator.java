package seoul.culture.demo.service;

import java.lang.Math;

public final class DistanceCalculator {

    private static double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }
    private static double toDegree(double rad){
        return (rad * 180 / Math.PI);
    }
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(toRadians(lat1)) * Math.sin(toRadians(lat2)) +
                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) * Math.cos(toRadians(theta));
        dist = Math.acos(dist);
        dist = toDegree(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist / 1000;
    }
}