
package com.cool.task.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class JSONDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (true) {

            if (in.readableBytes() <= 4) {
                break;
            }
            byte[] msg = new byte[in.readableBytes()];
            in.readBytes(msg);

            out.add(new String(msg, "utf-8"));
        }

    }


}
