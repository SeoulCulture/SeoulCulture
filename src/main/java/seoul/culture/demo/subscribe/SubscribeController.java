package seoul.culture.demo.subscribe;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import seoul.culture.demo.entity.mark.CultureEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SubscribeController {
    private Logger log = LoggerFactory.getLogger(SubscribeController.class);

    private final SubscribeList subscribeList;
    private final KeywordDataStorage keyStorage;
    private final SubscribePushBuffer buffer;
    // 한강축제알림
    // XX 주제로 버스킹 알림
    // XX 프로그램 알림
    // 지역 필터링하여 알림

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody SubscribeDto dto){
        // TODO: dto의 원본 메세지와 키워드 메세지 모두 필드로 두기.
        //  지금은 원본메세지가 키워드 자체라고 가정.
        subscribeList.add(dto);
    }

//    오전 7시마다 스캔 및 버퍼에 저장
//    @Scheduled(cron = "0 0 7 * * *") // 원래
    @Scheduled(cron = "0 * * * * *") // 테스트용: 1분에 한번
    private void pushMessageToBuffer(){
        // 버퍼를 돌면서 메세지 수집
        List<SubscribeDto> removeTargets = new ArrayList<>();
        List<SubscribePushDto> pushTargets = new ArrayList<>();
        for (SubscribeDto dto : subscribeList.get()) {
            String userRequest = dto.message().replace(" ", "");  // 행사명을 입력받도록 했음.
            // TODO: 날것의 메세지 말고, 행사명에 들어갈 만한 단어로 치환하기!
            Set<CultureEvent> events = keyStorage.get(userRequest);

            // 이 행사는 오픈되었을까?
            // 문제점: 연중행사는 이미 startDate가 엄청 이르다! 일단 다 알려주자.
            for(CultureEvent event : events){
                LocalDate start = event.getStartDate();
                LocalDate end = event.getEndDate();
                LocalDate now = LocalDate.now();
                // TODO: 그런데 어차피 CultureEvent에 담겨있는 것은 아래 조건을 만족함.
                //  그래서 오픈 D-3, Dday 이런식으로 알림 해야할 것 같다.
                if((start.isEqual(now) || start.isBefore(now))
                    && (end.isEqual(now) || end.isAfter(now))) {
                    removeTargets.add(dto);
                    pushTargets.add(new SubscribePushDto(dto.phoneNumber(), event.getTitle(), dto.message()));
                }
            }
        }
        pushTargets.forEach(target -> buffer.add(target));
        removeTargets.forEach(target -> subscribeList.remove(target));
    }

    // 1분에 한번씩 수행
    @Scheduled(cron = "0 * * * * *")
    public void scanBufferAndSend() {
        while (!buffer.isEmpty()) {
            SubscribePushDto pushTarget = buffer.poll();
            log.info("[SUBSCRIBE] {}", pushTarget.toString());
        }
    }
}