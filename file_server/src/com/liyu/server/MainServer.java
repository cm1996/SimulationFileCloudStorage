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
	
	static int PORT1 = 11113; //���ͻ����õ�
	static int PORT2 = 22224; //�����õ�
	
	public static void main(String[] args) {
		doClientThread.start(); //����ͻ�
		doStorageThread.start(); // ������
	}

	private static class DoClientThread extends Thread{
		@Override
		public void run() {
			// TODO deal with requests from clients
			super.run();
			acceptRequest(true); //true ����ͻ���
		}
	}
	private static class DoStorageThread extends Thread{
		@Override
		public void run() {
			// TODO deal with requests from Storage
			super.run();
			acceptRequest(false);  //false������
		}
	}
	@SuppressWarnings("resource")
	private static void acceptRequest(boolean isClient) {
		try {
			ServerSocket server = null;
			if(isClient){
				server = new ServerSocket(PORT1); //�ͻ��˸�������
			} else {
				server = new ServerSocket(PORT2);	//storage������			
			}
			while(true)
			{
				System.out.println("����ѭ��������");
				Socket socket = server.accept(); //�����ͻ�������
				if(isClient)
					System.out.println("lian shang le");
				else
					System.out.println("���� storageҲ�����ˣ�");
				InetAddress ia = socket.getInetAddress(); //�õ��Է� ip��ַ
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				//�������߳� ����ȥ������������ �Ҿͼ���������
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
			System.out.println("��Ӧʧ��");
			e.printStackTrace();
		}
	}
	/*private static void acceptClient(Protocol p) {
		try {
			ServerSocket server = new ServerSocket(14321);
			while(true)
			{
				Socket socket = server.accept(); //�����ͻ�������
						System.out.println("lian shang le");

				InetAddress ia = socket.getInetAddress(); //�õ��ͻ���ip��ַ
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				new Thread(){
					public void run() {
						p.service(socket);	
					};
				}.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("��Ӧ�ͻ�ʧ��");
			e.printStackTrace();
		}
	}
	private static void acceptStorage(Protocol p) {
		try {
			ServerSocket server = new ServerSocket(14322);
			while(true)
			{
				Socket socket = server.accept(); //����storage����
				System.out.println("����� storageҲ�����ˣ�");
				InetAddress ia = socket.getInetAddress(); //�õ�storage ip��ַ
				System.out.println(ia.getHostName() + "(" + ia.getHostAddress() + ") connected."); 
				new Thread(){
					public void run() {
						p.service(socket);	
					};
				}.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("��Ӧ�ͻ�ʧ��");
			e.printStackTrace();
		}
	}*/
}
