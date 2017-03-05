package com.liyu.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AcceptPendingException;

import com.liyu.protocol.ClientProtocol;
import com.liyu.protocol.Protocol;
import com.liyu.protocol.StorageProtocol;

public class MainServer {

	static DoClientThread doClientThread = new DoClientThread();
	static DoStorageThread doStorageThread = new DoStorageThread();
	static ClientProtocol clientProtocol= new ClientProtocol();
	static StorageProtocol storageProtocol = new StorageProtocol();
	
	static int PORT1 = 11113; //给客户端用的
	static int PORT2 = 22224; //给云用的
	
	public static void main(String[] args) {
		doClientThread.start(); //处理客户
		doStorageThread.start(); // 处理云
	}

	private static class DoClientThread extends Thread{
		@Override
		public void run() {
			// TODO deal with requests from clients
			super.run();
			acceptRequest(true); //true 代表客户端
		}
	}
	private static class DoStorageThread extends Thread{
		@Override
		public void run() {
			// TODO deal with requests from Storage
			super.run();
			acceptRequest(false);  //false代表云
		}
	}
	@SuppressWarnings("resource")
	private static void acceptRequest(boolean isClient) {
		try {
			ServerSocket server = null;
			if(isClient){
				server = new ServerSocket(PORT1); //客户端给的请求
			} else {
				server = new ServerSocket(PORT2);	//storage的请求			
			}
			while(true)
			{
				System.out.println("进入循环···");
				Socket socket = server.accept(); //监听客户端请求
				if(isClient)
					System.out.println("lian shang le");
				else
					System.out.println("看啊 storage也连上了！");
				InetAddress ia = socket.getInetAddress(); //得到对方 ip地址
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				//开个新线程 让它去处理这个请求吧 我就继续等请求
				new Thread(){
					public void run() {
						if(isClient)
							clientProtocol.service(socket);
						else 
							storageProtocol.service(socket);
					};
				}.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("响应失败");
			e.printStackTrace();
		}
	}
	/*private static void acceptClient(Protocol p) {
		try {
			ServerSocket server = new ServerSocket(14321);
			while(true)
			{
				Socket socket = server.accept(); //监听客户端请求
						System.out.println("lian shang le");

				InetAddress ia = socket.getInetAddress(); //得到客户端ip地址
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				new Thread(){
					public void run() {
						p.service(socket);	
					};
				}.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("响应客户失败");
			e.printStackTrace();
		}
	}
	private static void acceptStorage(Protocol p) {
		try {
			ServerSocket server = new ServerSocket(14322);
			while(true)
			{
				Socket socket = server.accept(); //监听storage请求
				System.out.println("黎宇看啊 storage也连上了！");
				InetAddress ia = socket.getInetAddress(); //得到storage ip地址
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				new Thread(){
					public void run() {
						p.service(socket);	
					};
				}.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("响应客户失败");
			e.printStackTrace();
		}
	}*/
}
