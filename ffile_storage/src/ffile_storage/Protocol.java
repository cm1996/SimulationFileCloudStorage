package ffile_storage;

import java.io.*;
import java.net.*;

public class Protocol {

	/**
	 * 处理请求 1上传 2下载 3删除 4改名
	 * 
	 * @param socket
	 */
	static PrintWriter pw;
	static BufferedReader br;
	static DataInputStream dis;
	static FileOutputStream fos;
	static DataOutputStream dos;
	static FileInputStream fis;

	public static void service(Socket socket) {
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String msg;
			System.out.println("!!!!!!!!socket is " + socket);
			msg = br.readLine();
			System.out.println("SS22222222222");
			System.out.println(msg);
			String[] requests = msg.split(",");
			File file = new File(Storage.dir + "\\" + requests[1]);

			long len = 0;
			byte[] buffer = new byte[4096];
			long r = 0;
			int rr = 0;

			switch (Integer.parseInt(requests[0])) {
			case 1:
				// 新建一个文件
				file.createNewFile();
				System.out.println("11111111111111111111111111111111111");
				pw.println("1");
				pw.flush();
				// 传输 成功后给server
				byte[] inputByte = null;
				int length = 0;
				dis = null;
				fos = null;
				try {
					try {
						dis = new DataInputStream(socket.getInputStream());

						/*
						 * 文件存储位置
						 */
						fos = new FileOutputStream(file);
						inputByte = new byte[1024];
						System.out.println("开始接收数据...");
						while ((length = dis.read(inputByte, 0,
								inputByte.length)) > 0) {
							fos.write(inputByte, 0, length);
							fos.flush();
						}
						System.out.println("完成接收：");
					} finally {
						if (fos != null)
							fos.close();
						if (dis != null)
							dis.close();
						if (socket != null)
							socket.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// here im going to tell server the bastard
				PrintWriter pwToServer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(
								new Socket("127.0.0.1", 22224)
										.getOutputStream())), true);
				pwToServer.println("3," + Storage.name + "," + file.getName());
				System.out.println("fuck u anyway················3,"
						+ Storage.name + file.getName());
				pwToServer.flush();
				pwToServer.close();
				break;
			case 2:
				// 下载
				System.out.println("hello? It's me.");
				if (file.exists()) {
					pw.println("1");// 如果文件存在 我发个1给你
					pw.flush();
					if (br.readLine().equals("1")) {
						int length2 = 0;
						double sumL = 0;
						byte[] sendBytes = null;
						dos = null;
						fis = null;
						boolean bool = false;
						try {
							long l = file.length();
							dos = new DataOutputStream(socket.getOutputStream());
							fis = new FileInputStream(file);
							sendBytes = new byte[1024];
							while ((length2 = fis.read(sendBytes, 0,
									sendBytes.length)) > 0) {
								System.out.println("写东西的循环");
								sumL += length2;
								System.out.println("已传输：" + ((sumL / l) * 100)
										+ "%");
								dos.write(sendBytes, 0, length2);
								dos.flush();
							}
							// 虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
							if (sumL == l) {
								bool = true;
							}
						} catch (Exception e) {
							System.out.println("客户端文件传输异常");
							bool = false;
							e.printStackTrace();
						} finally {
							if (dos != null)
								dos.close();
							if (fis != null)
								fis.close();
							if (socket != null)
								socket.close();
						}
						System.out.println(bool ? "成功" : "失败");
					}
					System.out.println("存在！！！");
					// 传输

				} else {
					pw.println("0");
					pw.flush();
				}
				break;
			case 3:// 删除
				if (file.exists()) {
					// 删除 告诉server
					file.delete();
					pw.println("1");
				} else {
					pw.println("0");
				}
				pw.flush();
				break;
			case 4:// 改名
				if (file.exists()) {
					// 改名 告诉服务器
					file.renameTo(new File(Storage.dir + "\\" + requests[2]));
					pw.println("1");
				} else {
					pw.println("0");
				}
				pw.flush();
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
