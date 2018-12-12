package hello.netty;

import java.io.File;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;

public class HttpRequestHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        if (msg instanceof DefaultHttpRequest) {
            System.out.println("Got HTTP request =======================");
            DefaultHttpRequest request = (DefaultHttpRequest) msg;

            final ByteBuf time = ctx.alloc().buffer(10); // (2)

            FlushableBuffer fbuf = new FlushableBuffer(time, b -> {
                ctx.write(b);
            });

            if (request.decoderResult().isSuccess()) {
                System.out.println(request.method() + " " + request.uri());

                File file = new File("d:/DOWNLOADS");

                fbuf.write("HTTP/1.1 200 OK\nContent-Type: text/plain\n\n");
                for (File f : file.listFiles()) {
                    fbuf.write(f.getName() + " " + f.getAbsolutePath() + "\n");
                }
                //                ctx.flush();
            } else {
                fbuf.write("HTTP/1.1 404 OK\nContent-Length: 0\n\n");
            }
            ctx.flush();
            ctx.close();
            System.out.println("=======================");
            //
            //            ((ByteBuf) msg).release(); // (3)
            return;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    private class FlushableBuffer {
        ByteBuf buf;
        Consumer<ByteBuf> consumer;

        FlushableBuffer(ByteBuf buf, Consumer<ByteBuf> consumer) {
            this.buf = buf;
            this.consumer = consumer;
        }

        void write(String string) {
            write(string.getBytes());

        }

        void write(byte[] bytes) {
            int i = 0;
            int l = bytes.length;
            while (l > 0) {
                int w = Math.min(buf.writableBytes(), l);
                buf.writeBytes(bytes, i, w);
                l -= w;
                i += w;
                consumer.accept(buf);
                buf.clear();
            }
        }
    }
}