package seoul.culture.demo.subscribe;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SubscribeLogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains("SUBSCRIBE"))
            return FilterReply.ACCEPT;
        else
            return FilterReply.DENY;
    }
}