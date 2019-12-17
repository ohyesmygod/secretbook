package com.example.javase.io.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * SelectionKey.OP_ACCEPT —— 接受连接就绪事件，表示服务器接受了客户端连接
 * SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户端与服务器建立连接成功
 * SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
 * SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        //获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(7777));
        //设置成非阻塞模式
        serverChannel.configureBlocking(false);

        //获取选择器
        Selector selector = Selector.open();

        ///监听该通道上的接受连接就绪事件,ServerSocketChannel的Operation Set只能是OP_ACCEPT，如果在注册的时候添加了OP_CONNECT、OP_WRITE或OP_READ会报异常
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //select()方法会阻塞到至少有一个通道触发了该selector感兴趣的事件
            //num表示就绪事件的数量
            int num = selector.select();
            //iterator包含当前选择器中所有已就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //一个就绪的事件
                SelectionKey selectionKey = iterator.next();

                //接受连接就绪事件
                if(selectionKey.isAcceptable()) {
                    System.out.println(System.nanoTime());
                    System.out.println("isAcceptable=========================");
                    //获取客户端通道
                    SocketChannel clientChannel = serverChannel.accept();
                    //设置成非阻塞
                    clientChannel.configureBlocking(false);
                    //监听该通道上的读就绪事件
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }

                //连接就绪事件
                if(selectionKey.isConnectable()) {
                    System.out.println("isConnectable=========================");
                }

                //读就绪事件
                if(selectionKey.isReadable()) {
                    System.out.println("isReadable===========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel clientChannel = (SocketChannel)selectionKey.channel();
                    System.out.println("接收客户端的请求");
                    //接收客户端的请求
                    //创建buffer
                    ByteBuffer readByteBuffer = ByteBuffer.allocate(1024);
                    //读取数据
                    clientChannel.read(readByteBuffer);
                    //切换读写模式
                    readByteBuffer.flip();
                    //byte[] data = new byte[1024]; //这种方式不行
                    byte[] data = new byte[readByteBuffer.limit()];
                    readByteBuffer.get(data);
                    System.out.println("接收到的数据: " + new String(data));
                    //监听该通道上的写就绪事件
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }

                //写就绪事件
                if(selectionKey.isWritable()) {
                    System.out.println("isWritable=========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel clientChannel = (SocketChannel)selectionKey.channel();
                    System.out.println("返回响应给客户端");
                    //返回响应给客户端
                    ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);
                    writeByteBuffer.put("wo ta ma shou dao ni de qing qiu le".getBytes());
                    //切换读写模式
                    writeByteBuffer.flip();
                    clientChannel.write(writeByteBuffer);
                    //监听该通道上的读就绪事件
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }

                //移除已经处理过的key,防止重复处理
                iterator.remove();
            }
        }





    }


}
