package com.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //boss 对应 IOServer.java 中的接受新连接线程,主要负责创建新连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //worker 对应 IOServer.java 中的负责读取数据的线程,主要用于读取数据以及业务逻辑处理
        NioEventLoopGroup worker = new NioEventLoopGroup();

        //要启动一个Netty服务端,必须要指定三类属性,分别是线程模型、IO 模型、连接读写处理逻辑
        serverBootstrap
                //group方法指定线程模型,线程模型包括三种单线程模型,多线程模型,主从线程模型,这里是主从线程模型
                .group(boss, worker)
                //channel方法指定IO模型,NioServerSocketChannel代表NIO,OioServerSocketChannel代表BIO
                .channel(NioServerSocketChannel.class)
                //childHandler方法会创建一个ChannelInitializer,ChannelInitializer主要就是定义后续每条连接的数据读写,业务处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                //handler方法用于指定在服务端启动过程中的一些逻辑,通常情况下呢,我们用不着这个方法
                //.handler()
                //attr方法可以给服务端的channel,也就是NioServerSocketChannel指定一些自定义属性,然后我们可以通过channel.attr()取出这个属性
                //.attr()
                //childAttr方法可以给每一条连接指定自定义属性,然后后续我们可以通过channel.attr()取出该属性
                //.childAttr()
                //childOption方法可以给每条连接设置一些TCP底层相关的属性,ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制,true为开启,ChannelOption.TCP_NODELAY表示是否开启Nagle算法,true表示关闭,false表示开启,通俗地说,如果要求高实时性,有数据发送时就马上发送,就关闭,如果需要减少发送次数减少网络交互,就开启
                //.childOption()
                //option方法服务端channel设置一些属性,最常见的就是so_backlog,ChannelOption.SO_BACKLOG,表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁,服务器处理创建新连接较慢,可以适当调大这个参数
                //.option()
                .bind(8000);
    }
}