package com.cool.task.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.cool.task.common.enums.TaskMethodEnum;
import com.cool.task.common.pojo.ChannelCache;
import com.cool.task.common.pojo.TCPMsgRequest;
import com.cool.task.common.pojo.Task;
import com.cool.task.subject.TaskSubject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cool.task.common.pojo.TCPMsgRequest.HEART_TYPE;
import static com.cool.task.common.pojo.TCPMsgRequest.MESSAGE_TYPE;


/**
 * TCP Handler
 * @author  Vincent
 */
@Component
public class OnlineServerHandler extends SimpleChannelInboundHandler<String> {

    private static Logger log = LoggerFactory.getLogger(OnlineServerHandler.class);

    private static Map<String, ChannelCache> onlineClients;

    static {

        onlineClients = new ConcurrentHashMap<>();
    }

    private final TaskSubject taskSubject;

    public OnlineServerHandler(TaskSubject taskSubject) {
        this.taskSubject = taskSubject;
    }


    /**
     * init connect,
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }


    /**
     * accept msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) {

        Channel channel = ctx.channel();
        TCPMsgRequest req = JSON.parseObject(request, TCPMsgRequest.class);


        String clientId = req.getId();


        if (!onlineClients.containsKey(clientId)) {

            log.info("tcp id = {} build connect success", clientId);

            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                onlineClients.remove(clientId);
            });

            onlineClients.put(clientId, new ChannelCache(channel, heartTask(ctx, channel, clientId)));
        }

        switch (req.getType()) {

            case HEART_TYPE:

                ChannelCache channelCache = onlineClients.get(clientId);

                channelCache.getScheduledFuture().cancel(Boolean.TRUE);
                channelCache.setScheduledFuture(heartTask(ctx, channel, clientId));
                ctx.channel().writeAndFlush(req);

                break;
            case MESSAGE_TYPE:

                String method = req.getMethod();
                Task task = req.getTask();
                if (method == null || "".equals(method) || task == null){
                    log.warn("the tcp client id = {} send msg was error", clientId);
                    return;
                }

                TaskMethodEnum taskMethodEnum = null;

                switch (method){

                    case "get":
                        taskMethodEnum = TaskMethodEnum.FIND;
                        break;
                    case "post":
                        taskMethodEnum = TaskMethodEnum.ADD;
                        break;
                    case "put":
                        taskMethodEnum = TaskMethodEnum.MODIFY;
                        break;
                    case "delete":
                        taskMethodEnum = TaskMethodEnum.REMOVE;
                        break;
                    default:
                        log.info("tcp id = {} task method was error", clientId);;
                }
                if (taskMethodEnum != null){

                    taskSubject.offer(task, taskMethodEnum);
                }

                break;
            default:
                log.info("tcp id = {} message type was error", clientId);

        }
    }


    /*
        normal close connect
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        ctx.fireChannelInactive();
    }


    /*
        exception close connect
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (null != ctx) {
            log.warn("TCP:{} connect exception", ctx.channel().remoteAddress());
            ctx.close();
        }
        if (null != cause) cause.printStackTrace();

    }

    /**
     *  keep alive
     */
    private ScheduledFuture heartTask(ChannelHandlerContext ctx, Channel channel, String clientId) {

        return ctx.executor().schedule(() -> {

            log.warn("TCP client:{} fault disconnect because of heart was stopped", clientId);
            channel.close();
        }, 5, TimeUnit.MINUTES);
    }


    /**
     * send msg to client
     */
    public static boolean sendMsgToClient(String clientId, String msg) {

        try {
            Channel channel = onlineClients.get(clientId).getChannel();
            channel.writeAndFlush(msg);
        } catch (Exception e){
            log.warn("send msg to client id = {} error, the connect was closed", clientId);
            return false;
        }

        return true;
    }



}
