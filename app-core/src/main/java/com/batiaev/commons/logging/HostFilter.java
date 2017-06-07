package com.batiaev.commons.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.net.UnknownHostException;
import java.util.List;

import static ch.qos.logback.core.spi.FilterReply.DENY;
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL;
import static java.net.InetAddress.getLocalHost;
import static java.util.Arrays.asList;

/**
 * HostFilter
 *
 * @author anton
 * @since 07/06/17
 */
public class HostFilter extends Filter<ILoggingEvent> {
    private String hostNames;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) return DENY;

        try {
            String hostName = getLocalHost().getHostName();
            List<String> hosts = asList(hostNames.split(","));
            return hosts.contains(hostName) ? NEUTRAL : DENY;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return NEUTRAL;
    }
}
