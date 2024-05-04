package seoul.culture.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.dto.CultureSearchForm;
import seoul.culture.demo.entity.Location;
import seoul.culture.demo.entity.Spot;
import seoul.culture.demo.repository.CultureRepository;
import seoul.culture.demo.pathfinder.HowToGo;
import seoul.culture.demo.pathfinder.PathFinder;
import seoul.culture.demo.dto.MarkDto;
import seoul.culture.demo.dto.Markable;
import seoul.culture.demo.repository.SpotRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {   // TODO: 문제점: 속도.. 현재 꽤나 지루하게 오래걸림 : 1시간 거리 검색은 결과 보기 불가능할 정도 & 30분 거리도 기다리기 지루함
    private final CultureRepository cultureRepository;
    private final SpotRepository spotRepository;
    private List<Culture> cultures;
    private List<Culture> places;
    private final PathFinder pathFinder;

    public SearchService(CultureRepository cultureRepository, SpotRepository spotRepository, @Qualifier("naverPathFinder") PathFinder pathFinder) {
        this.cultureRepository = cultureRepository;
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
            form.setLongitude(String.valueOf(spot.getLongitude()));
            form.setLatitude(String.valueOf(spot.getLatitude()));
        }

        // [필터1] 가격 필터링
        List<Culture> filteredCultures = filterByPrice(form);


        // [필터2] 거리 필터링 - 위경도 & 시간
        int time = form.getTime();
        double currentLat = Double.parseDouble(form.getLatitude());
        double currentLon = Double.parseDouble(form.getLongitude());
        HowToGo howToGo = HowToGo.valueOf(form.getHowToGo());
        filteredCultures = filterBySimpleDistance(time, currentLat, currentLon, howToGo, filteredCultures);  // [1] 간단 필터링
//        filteredCultures = filterByDetailDistance(time, currentLat, currentLon, howToGo, filteredCultures);  // [2] 구체 필터링

        // 마지막 단계: 리턴할 데이터 준비
        List<Markable> dtos = new ArrayList<>();
        for (Culture culture : filteredCultures) {
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
        Map<String, Object> map = new HashMap<>();
        map.put("marks", dtos);
        map.put("lat", form.getLatitude());
        map.put("lon", form.getLongitude());

        return map;
    }

    private List<Culture> filterByPrice(CultureSearchForm form) {
        if (form.getPrice() == null) {
            return cultures;
        }
        List<Culture> filteredCultures = new ArrayList<>();
        for (Culture culture : cultures) {
            if (culture.getPrice() <= form.getPrice()) {
                filteredCultures.add(culture);
            }
        }
        return filteredCultures;
    }

    private List<Culture> filterByDetailDistance(int time, double currentLat, double currentLon, HowToGo howToGo, List<Culture> rawTargets) throws IOException {
        List<Culture> targets = new ArrayList<>();
        for (Culture culture : rawTargets) {
            Location location = culture.getLocation();
            pathFinder.setPathInfo(currentLat, currentLon, location.getLatitude(), location.getLongitude(), HowToGo.DRIVING);

            int durationMinute = 0;
            if (howToGo == HowToGo.DRIVING) {
                durationMinute = pathFinder.getDuration();
            }
            if (howToGo == HowToGo.WALKING) {
                int distance = pathFinder.getDistance();
                durationMinute = (int) (distance / 4.8 * 60);  // 시속 4.8km/h로 걷는다고 가정
            }
            if (durationMinute < time) {
                targets.add(culture);
            }
        }
        return targets;
    }

    private List<Culture> filterBySimpleDistance(int time, double currentLat, double currentLon, HowToGo howToGo, List<Culture> cultures) {
        List<Culture> rawTargets = new ArrayList<>();
        for (Culture culture : cultures) {
            Location location = culture.getLocation();
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            double distanceKm = DistanceCalculator.calculateDistance(currentLat, currentLon, lat, lon);
            double predictionTime = 0.0;
            if (howToGo == HowToGo.DRIVING)
                predictionTime = DrivingTimeCalculator.calculateTime(distanceKm);
            if (howToGo == HowToGo.WALKING) {
                predictionTime = WalkingTimeCalculator.calculateTime(distanceKm);
            }
            if (predictionTime < time) {
                rawTargets.add(culture);
            }
        }
        return rawTargets;
    }

    private void loadCultures() {
        if (cultures == null || cultures.size() == 0) {
            this.cultures = cultureRepository.findAll();
        }
    }
}