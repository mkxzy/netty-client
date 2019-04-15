package com.gyxr;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * NettyClient
 *
 */
public class NettyClient
{
    public static void main( String[] args ) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addFirst(new StringEncoder());
                        ch.pipeline().addFirst(new OutboundA());
                        ch.pipeline().addFirst(new OutboundB());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

        while (true) {
            channel.writeAndFlush(new Date() + ": hello world!");
            Thread.sleep(2000);
        }
    }

    private static class OutboundA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutboundA write");
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString(Charset.forName("UTF-8")));
            super.write(ctx, msg, promise);
        }
    }

    private static class OutboundB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutboundB write");
            super.write(ctx, msg, promise);
        }
    }
}
