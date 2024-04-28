package seoul.culture.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.dto.CultureSearchForm;
import seoul.culture.demo.entity.Location;
import seoul.culture.demo.repository.CultureRepository;
import seoul.culture.demo.pathfinder.HowToGo;
import seoul.culture.demo.pathfinder.PathFinder;
import seoul.culture.demo.dto.MarkDto;
import seoul.culture.demo.dto.Markable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {   // TODO: 문제점: 속도.. 현재 꽤나 지루하게 오래걸림 : 1시간 거리 검색은 결과 보기 불가능할 정도 & 30분 거리도 기다리기 지루함
    private final CultureRepository cultureRepository;
    private List<Culture> cultures;
    private final PathFinder pathFinder;

    public SearchService(CultureRepository cultureRepository, @Qualifier("naverPathFinder") PathFinder pathFinder) {
        this.cultureRepository = cultureRepository;
        this.pathFinder = pathFinder;
    }

    public List<Markable> search(CultureSearchForm cultureSearchForm) throws IOException {
        if (cultureSearchForm.getLongitude() == null || cultureSearchForm.getLatitude() == null) {
            return null;
        }
        if (cultures == null || cultures.size() == 0) {
            this.cultures = cultureRepository.findAll();
        }

        // step1: 위경도 수치 & 시간 인풋 기반 필터링
        int time = cultureSearchForm.getTime();
        double currentLat = Double.parseDouble(cultureSearchForm.getLatitude());
        double currentLon = Double.parseDouble(cultureSearchForm.getLongitude());

        /**
         * 여기서부터는 howToGo에 따라서 변동됨
         */
        HowToGo howToGo = HowToGo.valueOf(cultureSearchForm.getHowToGo());  // 도보
        // 검색속도 향상을 위해서는, 여기서 정확하게 많이 추려내는 것이 관건이다.
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

        // step2: pathFinder 기반 구체적 필터링
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

        // MarkDto로 변경
        List<Markable> dtos = new ArrayList<>();
        for (Culture culture : targets) {
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
}