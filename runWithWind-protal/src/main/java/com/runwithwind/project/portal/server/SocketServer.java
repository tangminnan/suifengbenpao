package com.runwithwind.project.portal.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.alibaba.druid.support.jconsole.util.TableDataProcessor.parseData;

public class SocketServer {


    private static final String TAG = "TCPSocketServer";
    public static final String RECEIVE_DATA = "receive_data";
    private static final int POOL_SIZE = 5;
    private static final int BUFFER_LENGTH = 1024;
    private byte[] receiveByte = new byte[BUFFER_LENGTH];
    public static final int PORT = 8087; // 默认端口8888
    private boolean isThreadRunning = false;
    private long lastReceiveTime = 0;
    private static final long TIME_OUT = 120 * 1000;
    private static final long HEARTBEAT_MESSAGE_DURATION = 10 * 1000;

    private ExecutorService mThreadPool;
    private Thread serverThread;

    private ServerSocket serverSocket;
    Socket socket;


    public  SocketServer() {

        int cpuNumbers = Runtime.getRuntime().availableProcessors();
        // 根据CPU数目初始化线程池
        mThreadPool = Executors.newFixedThreadPool(cpuNumbers * POOL_SIZE);
        // 记录创建对象时的时间
        lastReceiveTime = System.currentTimeMillis();

    }

    /**
     * 启动服务
     */
    public  void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.e(TAG, "startServer: 启动服务");
                    InetSocketAddress socketAddress = new InetSocketAddress(PORT);
                    // 表明这个 Socket 在设置的端口上监听数据。
                    serverSocket = new ServerSocket();
                    serverSocket.bind(socketAddress);
                    socket = serverSocket.accept();

                    //Log.e(TAG, "startServer: 服务已启动");
                    startReader(socket);
                } catch (IOException e) {
                    //Log.e(TAG, "startServer: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    /**
     * 接收数据
     *
     * @param socket
     */
    public void startReader(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream reader;
                byte buffer[] = new byte[10 * 1024];
                int length = 0;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    // 从InputStream当中读取客户端所发送的数据
                    while ((length = reader.read(buffer)) != -1) {
                        byte[] data = Arrays.copyOf(buffer, length);

                        System.out.println("收到消息" + data+new Date());
                        //Log.i(TAG, "data_length:" + data.length + " " + HexUtil.formatHexString(data, true));  //打印接收到的信息

                        parseData(data);//解析数据

                        startWriter("shabi");
                    }


                } catch (IOException e) {

                }

            }
        }).start();

    }

    /*public void heartbeat(final byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeBytes(HexUtil.formatHexString(bytes, false));
                    //Log.e(TAG, "heartbeat:" + bytes.length + " " + HexUtil.formatHexString(bytes, true));
                } catch (IOException e) {
                    Log.e(TAG, "heartbeat: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    public void startWriter(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeUTF(socket.getLocalAddress() + ":" + message); // 写一个UTF-8的信息
                    System.out.println("发送消息" + message+new Date());
                    //Log.e(TAG, "startWriter: " + message);

                } catch (IOException e) {
                    //Log.e(TAG, "startWriter: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
