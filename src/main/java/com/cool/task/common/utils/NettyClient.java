package com.cool.task.common.utils;


import com.alibaba.fastjson.JSON;
import com.cool.task.common.pojo.TCPMsgRequest;
import com.cool.task.common.pojo.Task;
import com.cool.task.common.utils.CronUtils;
import com.cool.task.tcp.coder.JSONDecoder;
import com.cool.task.tcp.coder.JSONEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * client   /   it will be used to test
 * @author Vincent
 */
@Deprecated
public class NettyClient {

    public static void main(String[] args) {

        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new JSONDecoder());
                    pipeline.addLast(new JSONEncoder());
                    pipeline.addLast(new ClientHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1",4040)).sync();
            Channel channel = future.sync().channel();

            TCPMsgRequest heartRequest = new TCPMsgRequest();
            heartRequest.setType(1);
            heartRequest.setId("1");
            heartRequest.setMethod("post");
            String heartStr = JSON.toJSONString(heartRequest);

            TCPMsgRequest msgRequest = new TCPMsgRequest();
            msgRequest.setType(2);
            Task task = new Task();
            task.setName("11995033111231211");
            task.setCron(CronUtils.cron(20, TimeUnit.SECONDS));
            task.setParams("=====================123123======================");
            msgRequest.setMethod("post");
            msgRequest.setTask(task);
            msgRequest.setId("11995033111231211");
            String msgStr = JSON.toJSONString(msgRequest);

            new Thread(() -> {

                while (true){
                    try {
                        channel.writeAndFlush(heartStr);
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).start();

            Thread.sleep(1000);
            channel.writeAndFlush(msgStr);


                Thread.sleep(3000090);



            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            worker.shutdownGracefully();
        }



    }

    static class ClientHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("server response ： "+msg);
        }


        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            System.out.println("channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelInactive");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.channel().close();
            cause.printStackTrace();
        }

    }


}
