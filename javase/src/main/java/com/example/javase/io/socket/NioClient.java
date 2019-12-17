package com.example.javase.io.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {

    public static void main(String[] args) throws IOException {
        //获取通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置成非阻塞模式
        socketChannel.configureBlocking(false);
        //获取选择器
        Selector selector = Selector.open();
        //监听该通道上的连接就绪事件,SocketChannel的Operation Set只能是OP_CONNECT、OP_WRITE和OP_READ，如果在注册的时候添加了OP_ACCEPT同样会报异常
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7777));


        while (true) {
            //select()方法会阻塞到至少有一个通道触发了该selector感兴趣的事件
            //num就绪事件的数量
            int num = selector.select();
            //iterator包含当前选择器中所有已就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //一个就绪的事件
                SelectionKey selectionKey = iterator.next();

                //接受连接就绪事件
                if(selectionKey.isAcceptable()) {
                    System.out.println("isAcceptable=========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //监听该通道上的读就绪事件
                    channel.register(selector, SelectionKey.OP_READ);
                }

                //连接就绪事件
                if(selectionKey.isConnectable()) {
                    System.out.println(System.nanoTime());
                    System.out.println("isConnectable=========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //非阻塞模式下确认建立连接,还不是很清楚
                    channel.finishConnect();
                    //监听该通道上的写就绪事件
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }

                //读就绪事件
                if(selectionKey.isReadable()) {
                    System.out.println("isReadable===========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    System.out.println("接收服务端消息");
                    //接收客户端的请求
                    //创建buffer
                    ByteBuffer readByteBuffer = ByteBuffer.allocate(1024);
                    //读取数据
                    channel.read(readByteBuffer);
                    //切换读写模式
                    readByteBuffer.flip();
                    //byte[] data = new byte[1024]; //这种方式不行
                    byte[] data = new byte[readByteBuffer.limit()];
                    readByteBuffer.get(data);
                    System.out.println("接收到的数据: " + new String(data));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }

                //写就绪事件
                if(selectionKey.isWritable()) {
                    System.out.println("isWritable=========================");
                    //获取当前就绪事件对应的通道
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //返回响应给客户端
                    ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);
                    writeByteBuffer.put("hello server".getBytes());
                    //切换读写模式
                    writeByteBuffer.flip();
                    channel.write(writeByteBuffer);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }

                //移除已经处理过的key,防止重复处理
                iterator.remove();


            }



        }

    }
}
