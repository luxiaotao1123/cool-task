package com.cool.task.common.pojo;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;


public class ChannelCache {

    private Channel channel;
    private ScheduledFuture scheduledFuture;

    public ChannelCache() {
    }

    public ChannelCache(Channel channel, ScheduledFuture scheduledFuture) {
        this.channel = channel;
        this.scheduledFuture = scheduledFuture;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
