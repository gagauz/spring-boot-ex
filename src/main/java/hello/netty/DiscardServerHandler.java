package hello.netty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;

            ByteArrayOutputStream out = new ByteArrayOutputStream(buf.capacity());
            try {
                buf.readBytes(out, buf.readableBytes());
                System.out.println(new String(out.toByteArray()));

                byte[] resp = "HTTP/1.1 200 OK\nContent-Type: text/plain\nContent-Length: 0\n\n".getBytes();

                final ByteBuf time = ctx.alloc().buffer(resp.length); // (2)
                time.writeBytes(resp);
                ctx.writeAndFlush(time);
                ctx.close();
                //
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //            ((ByteBuf) msg).release(); // (3)
            return;
        }
        throw new RuntimeException("Invalid msg object type");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}