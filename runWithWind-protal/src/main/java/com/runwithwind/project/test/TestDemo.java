package com.runwithwind.project.test;

import com.runwithwind.project.portal.server.SocketServer;

import java.io.*;

/**
 * @author 某家:
 * @version 创建时间：2015年8月17日 下午3:04:14
 * 类说明
 */

public class TestDemo {
    //搭建服务器端
    public static void main(String[] args) throws IOException{
        SocketServer socketServer = new SocketServer();
        socketServer.startServer();
    }
}