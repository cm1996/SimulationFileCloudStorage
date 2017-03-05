package ffile_storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.stream.events.EndDocument;

public class Storage {

	static String name;
	String ip;
	String portString;
	String volume;
	int port;
	static String dir;	
	
	static int PORT = 22224;
	static String IP = "192.168.45.113";
	
	static PrintWriter pw;  
    BufferedReader br; 
	
	//Map<String,String> information = new HashMap<String,String>();
	public Storage(String name,int port) {
		// 先确定打开存储器的名字 唯一的
		this.name = name;
		this.port = port;
		File prop = new File(name+".properties");
		Properties properties = new Properties();
		
		if(prop.exists()){
			//读
			try {
			properties.load(new FileInputStream(prop));
			} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
/*			information.put("server",properties.getProperty("server"));
			information.put("ip", properties.getProperty("ip"));
			information.put("port",properties.getProperty("port"));
			information.put("folder",properties.getProperty("folder"));
			information.put("volume",properties.getProperty("volume"));*/
			ip = properties.getProperty("ip");
			portString = properties.getProperty("port");
			volume = properties.getProperty("volume");
			dir = properties.getProperty("root_folder");
		} else {
			try {
				//没有就主动创建  
				prop.createNewFile();
				File folder = new File(name+"_folder");
				dir = name+"_folder";
				folder.mkdir();
/*				information.put("server",name);
				information.put("ip", "127.0.0.1");
				information.put("port","11111");
				information.put("folder",name+"_folder");
				information.put("volume","1KB");*/
				ip = IP;
				portString = ""+port;
				volume = "100000";
				properties.setProperty("server",name);
				properties.setProperty("ip",ip);
				properties.setProperty("port",portString);
				properties.setProperty("root_folder",dir);
				properties.setProperty("volume",volume);
				OutputStream os = new FileOutputStream(prop);
				properties.store(os, "保存?");
				//System.out.println("你叫我输出的  "+ip+" "+volume);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Socket askServerSocket = new Socket(IP,PORT);
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(askServerSocket.getOutputStream())),true);  
            br = new BufferedReader(new InputStreamReader(askServerSocket.getInputStream()));  
            pw.println("1,"+name+","+ip+","+port+","+volume);
            System.out.println("我发的是 "+"1,"+name+","+ip+","+port+","+volume);
            pw.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("我觉得有点热啊");
		pw.println("2,"+name);
		pw.flush();
		//记得还要写prop！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		super.finalize();
	}*/
	/*private class EndThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("you are going to die you fb");
			pw.println("2,"+name);
			pw.flush();
			super.run();
		}
	}*/
	static Thread endThread = new Thread(){
		public void run() {
			System.out.println("you are going to die you fb");
			pw.println("2,"+name);
			pw.flush();
		}
	};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("请输入 存储器名字和端口号");
		Scanner scanner = new Scanner(System.in);
		String name = scanner.next();
		Runtime.getRuntime().addShutdownHook(endThread);
		int port = scanner.nextInt();
		Storage storage = new Storage(name, port);
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true){
				Socket socket = serverSocket.accept();
				new Thread(){
					public void run() {
						Protocol.service(socket);
					}
				}.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
