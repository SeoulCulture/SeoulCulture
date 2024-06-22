package seoul.culture.demo.subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import seoul.culture.demo.entity.mark.CultureEvent;

import java.util.*;

@Component
public class KeywordDataStorage {
    private Logger log = LoggerFactory.getLogger(KeywordDataStorage.class);
    private Map<String, Set<CultureEvent>> map;

    public KeywordDataStorage() {
        map = new HashMap<>();
    }

    public void add(String keyword, CultureEvent cultureEvent) {
        if (!map.containsKey(keyword)) {
            map.put(keyword, new HashSet<>());
        }
        map.get(keyword).add(cultureEvent);
        log.info("[SUBSCRIBE] 키워드 {} 추가됨: {}", keyword, cultureEvent.getTitle());
    }

    public Set<CultureEvent> get(String keyword) {
        return map.getOrDefault(keyword, new HashSet<>());
    }
}