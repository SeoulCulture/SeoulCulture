package seoul.culture.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import seoul.culture.demo.entity.*;
import seoul.culture.demo.dto.CultureSearchForm;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.entity.mark.CulturePlace;
import seoul.culture.demo.entity.mark.Mark;
import seoul.culture.demo.repository.CulturePlaceRepository;
import seoul.culture.demo.repository.CultureEventRepository;
import seoul.culture.demo.pathfinder.HowToGo;
import seoul.culture.demo.pathfinder.PathFinder;
import seoul.culture.demo.dto.MarkDto;
import seoul.culture.demo.dto.Markable;
import seoul.culture.demo.repository.SpotRepository;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {
    private final CultureEventRepository cultureEventRepository;
    private final CulturePlaceRepository culturePlaceRepository;
    private final SpotRepository spotRepository;
    private List<CultureEvent> cultures;
    private List<CulturePlace> places;
    private final PathFinder pathFinder;

    public SearchService(CultureEventRepository cultureEventRepository, CulturePlaceRepository culturePlaceRepository, SpotRepository spotRepository, @Qualifier("naverPathFinder") PathFinder pathFinder) {
        this.cultureEventRepository = cultureEventRepository;
        this.culturePlaceRepository = culturePlaceRepository;
        this.spotRepository = spotRepository;
        this.pathFinder = pathFinder;
    }

    public Map<String, Object> search(CultureSearchForm form) throws IOException {
        // 유효성 검증
        if ((form.getLongitude() == null || form.getLatitude() == null)
                && (form.getPlace() == null || form.getPlace().length() == 0)) {
            throw new IllegalArgumentException("위경도좌표없음");
        }
        loadCultures();

        // 장소지정검색을 했을 경우, 지정한 장소로 기준좌표 설정
        if (form.getPlace() != null && form.getPlace().length() != 0) {
            Spot spot = spotRepository.findBySpotName(form.getPlace().trim());
            if (spot != null) {
                form.setLongitude(String.valueOf(spot.getLongitude()));
                form.setLatitude(String.valueOf(spot.getLatitude()));
            }
        }

        // [필터1] 가격 필터링
        Map<String, List> filteredInfo = filterByPrice(form);

        // [필터2] 거리 필터링 - 위경도 & 시간
        int time = form.getTime();
        double currentLat = Double.parseDouble(form.getLatitude());
        double currentLon = Double.parseDouble(form.getLongitude());
        HowToGo howToGo = HowToGo.valueOf(form.getHowToGo());
        Map<String, List> filtered = filterBySimpleDistance(time, currentLat, currentLon, howToGo, filteredInfo);// [1] 간단 필터링
//        filteredCultures = filterByDetailDistance(time, currentLat, currentLon, howToGo, filteredCultures);  // [2] 구체 필터링

        // 마지막 단계: 리턴할 데이터 준비
        List<Markable> cultureDtos = getMarkables(filtered.get("cultures"));
        List<Markable> placeDtos = getMarkables(filtered.get("places"));
        Map<String, List> results = removePlaceDtos(cultureDtos, placeDtos);

        Map<String, Object> map = new HashMap<>();
        map.put("cultures", results.get("cultures"));
        map.put("places", results.get("places"));
        map.put("lat", form.getLatitude());
        map.put("lon", form.getLongitude());

        return map;
    }

    private Map<String, List> removePlaceDtos(List<Markable> cultureDtos, List<Markable> placeDtos) {
        Set<String> cultureCoords = new HashSet<>();

        for (Markable markable : cultureDtos) {
            Map<String, Double> location = markable.getLocation();
            double roundedLat = Math.round(location.get("latitude") * 10000.0) / 10000.0;
            double roundedLon = Math.round(location.get("longitude") * 10000.0) / 10000.0;
            cultureCoords.add(roundedLat + "," + roundedLon);
        }

        // placeDtos에서 겹치지 않는 좌표만을 추출
        List<Markable> filteredPlaceDtos = new ArrayList<>();
        for (Markable markable : placeDtos) {
            Map<String, Double> location = markable.getLocation();
            double roundedLat = Math.round(location.get("latitude") * 10000.0) / 10000.0;
            double roundedLon = Math.round(location.get("longitude") * 10000.0) / 10000.0;
            if (!cultureCoords.contains(roundedLat + "," + roundedLon)) {
                filteredPlaceDtos.add(markable);
            }
        }
        Map<String, List> results = new HashMap<>();
        results.put("cultures", cultureDtos);
        results.put("places", filteredPlaceDtos);
        return results;
    }

    private static List<Markable> getMarkables(List<Mark> marks) {
        List<Markable> dtos = new ArrayList<>();
        for (Mark culture : marks) {
            dtos.add(MarkDto.builder()
                    .category(culture.getCategory().toString())
                    .latitude(culture.getLocation().getLatitude())
                    .longitude(culture.getLocation().getLongitude())
                    .title(culture.getTitle())
                    .price(culture.getPrice())
                    .contents(culture.getContents())
                    .imgUrl(culture.getImgUrl())
                    .detailUrl(culture.getDetailUrl())
                    .build());
        }
        return dtos;
    }

    private Map<String, List> filterByPrice(CultureSearchForm form) {
        if (form.getPrice() == null) {
            Map<String, List> results = new HashMap<>();
            results.put("cultures", cultures);
            results.put("places", places);
            return results;
        }

        List filteredCultures = getPriceFilteredMarks(cultures, form);
        List filteredPlaces = getPriceFilteredMarks(places, form);

        Map<String, List> results = new HashMap<>();
        results.put("cultures", filteredCultures);
        results.put("places", filteredPlaces);
        return results;
    }

    private List getPriceFilteredMarks(List cultures, CultureSearchForm form) {
        List filteredCultures = new ArrayList<>();
        for (Object obj : cultures) {
            Mark culture = (Mark) obj;
            if (culture.getPrice() <= form.getPrice()) {
                filteredCultures.add(obj);
            }
        }
        return filteredCultures;
    }

//    private List<Culture> filterByDetailDistance(int time, double currentLat, double currentLon, HowToGo howToGo, List<Culture> rawTargets) throws IOException {
//        List<Culture> targets = new ArrayList<>();
//        for (Culture culture : rawTargets) {
//            Location location = culture.getLocation();
//            pathFinder.setPathInfo(currentLat, currentLon, location.getLatitude(), location.getLongitude(), HowToGo.DRIVING);
//
//            int durationMinute = 0;
//            if (howToGo == HowToGo.DRIVING) {
//                durationMinute = pathFinder.getDuration();
//            }
//            if (howToGo == HowToGo.WALKING) {
//                int distance = pathFinder.getDistance();
//                durationMinute = (int) (distance / 4.8 * 60);  // 시속 4.8km/h로 걷는다고 가정
//            }
//            if (durationMinute < time) {
//                targets.add(culture);
//            }
//        }
//        return targets;
//    }

    private Map<String, List> filterBySimpleDistance(int time, double currentLat, double currentLon, HowToGo howToGo, Map<String, List> map) {
        // [1] culture
        List<Mark> cultures = filterSimpleTargets(time, currentLat, currentLon, howToGo, map, "cultures");
        // [2] place
        List<Mark> places = filterSimpleTargets(time, currentLat, currentLon, howToGo, map, "places");

        Map<String, List> results = new HashMap<>();
        results.put("cultures", cultures);
        results.put("places", places);

        return results;
    }
    private List<Mark> filterSimpleTargets(int time, double currentLat, double currentLon, HowToGo howToGo, Map<String, List> map, String MAP_KEY) {
        List<Mark> targets = new ArrayList<>();
        for (Mark culture : (List<Mark>) map.get(MAP_KEY)) {
            Location location = culture.getLocation();
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            double distanceKm = DistanceCalculator.calculateDistance(currentLat, currentLon, lat, lon);
            double predictionTime = 0.0;
            if (howToGo == HowToGo.DRIVING)
                predictionTime = DrivingTimeCalculator.calculateTime(distanceKm);
            if (howToGo == HowToGo.WALKING)
                predictionTime = WalkingTimeCalculator.calculateTime(distanceKm);
            if (predictionTime < time)
                targets.add(culture);
        }
        return targets;
    }
    
    private void loadCultures() {
        if (cultures == null || cultures.size() == 0) {
            this.cultures = cultureEventRepository.findAll();
        }
        if (places == null || places.size() == 0) {
            this.places = culturePlaceRepository.findAll();
        }
    }
}