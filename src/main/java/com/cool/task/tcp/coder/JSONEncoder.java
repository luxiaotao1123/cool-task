
package com.cool.task.tcp.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Vincent
 */
public class JSONEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        if (msg == null || "".equals(msg)) {
            return;
        }
        out.writeBytes(msg.getBytes());
    }


}
