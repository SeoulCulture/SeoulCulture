package seoul.culture.demo.subscribe;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SubscribeList {
    private Logger log = LoggerFactory.getLogger(SubscribeList.class);
    private List<SubscribeDto> subscribes = new ArrayList<>();

    void add(SubscribeDto dto) {
        subscribes.add(dto);
        log.info("[SUBSCRIBE] 요청 추가됨: {}", dto.message());
    }

    boolean remove(SubscribeDto dto) {
        return subscribes.remove(dto);
    }

    List<SubscribeDto> get() {
        return subscribes;
    }
}