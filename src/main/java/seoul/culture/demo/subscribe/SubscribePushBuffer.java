package seoul.culture.demo.subscribe;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class SubscribePushBuffer {
    private Deque<SubscribePushDto> buffer;

    public SubscribePushBuffer() {
        buffer = new ArrayDeque<>();
    }

    public void add(SubscribePushDto subscribe) {
        buffer.addLast(subscribe);
    }

    public SubscribePushDto poll() {
        return buffer.pollFirst();
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    public int size() {
        return buffer.size();
    }
}