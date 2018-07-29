package com.cool.task.tcp;


import com.cool.task.subject.TaskSubject;
import com.cool.task.tcp.coder.JSONDecoder;
import com.cool.task.tcp.coder.JSONEncoder;
import com.cool.task.tcp.handler.OnlineServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Netty Server
 * @author Vincent
 */
@Component
public class OnlineServer {

    private static Logger log = LoggerFactory.getLogger(OnlineServer.class);


    @Value("${tcp.port}")
    private int port;

    private Channel channel;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final TaskSubject taskSubject;

    @Autowired
    public OnlineServer(TaskSubject taskSubject) {
        this.taskSubject = taskSubject;
    }


    /**
     *  tcp server init
     */
    @PostConstruct
    public void serverStart() throws InterruptedException {

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {


                    @Override
                    protected void initChannel(SocketChannel ch) {

                        ch.pipeline()
                                .addLast(new JSONDecoder())
                                .addLast(new JSONEncoder())
                                .addLast(new OnlineServerHandler(taskSubject));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        log.info("TCP server started successfully, port：{}", port);

        channel = bootstrap.bind(port).sync().channel();
    }


    /**
     * tcp server stop
     */
    @PreDestroy
    public void destroy() {


        if (channel != null && channel.isActive()) {
            channel.close();
        }

        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

        log.info("TCP server stopped successfully, port: {}", port);
    }
}
