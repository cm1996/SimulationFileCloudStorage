package file_client;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * 文件传输的客户端类，实现了上传下载更名删除指定文件四个操作
 * 
 * @author liyu
 *
 */
public class FileClient {

	static String IP = "192.168.45.113";
	static int port1 = 11113;

	/**
	 * 完成上传文件的操作，将“1，文件名，文件长度”发送给server， 得到一个字符串“1/0,IP地址1，port1，IP地址2，port2”
	 * 1是成功，0是不成功，一次创建两个线程 分别与port1，port2进行文件传输。
	 * 
	 * @param aFile
	 *            要传输的文件
	 * @throws Exception
	 */

	public void upload(File aFile) throws Exception // 上传文件
	{
		String s = "1," + aFile.getName() + "," + aFile.length();
		Socket sockettoserver = null; // 与fileserver建立连接，得到uuid和两个filestorage的端口号
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			sockettoserver = new Socket(IP, port1);
			System.out.println("socket:" + sockettoserver);
			br = new BufferedReader(new InputStreamReader(sockettoserver.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettoserver.getOutputStream())), true);
			pw.println(s);
			pw.flush();
			String string = br.readLine();
			System.out.println(string);
			String[] get = string.split(","); // 得到了可以存储该文件的两个存储器的端口号
			br.close();
			pw.close();
			sockettoserver.close();
			if (get[0].equals("1")) {
				new Thread() {
					public void run() {
						int port = Integer.parseInt(get[2]);
						int length = 0;
						double sumL = 0;
						byte[] sendBytes = null;
						Socket sockettostorage1 = null;
						PrintWriter pw1 = null;
						DataOutputStream dos = null;
						FileInputStream fis = null; // 将要传输的文件读出
						BufferedReader br1 = null; // 客户端和云端之间数据传输
						boolean bool = false;
						try {
							File file = new File(aFile.getName()); // 要传输的文件路径
							long l = file.length();
							sendBytes = new byte[1024];
							sockettostorage1 = new Socket();
							sockettostorage1.connect(new InetSocketAddress(IP, port));
							pw1 = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(sockettostorage1.getOutputStream())),
									true);
							pw1.println("1," + get[5]); // 通知云端操作和uuid
							System.out.println("socket:" + sockettostorage1);
							System.out.println("qwertyy         " + get[5]);
							pw1.flush();
							br1 = new BufferedReader(new InputStreamReader(sockettostorage1.getInputStream()));
							System.out.println("sssss");
							if (br1.readLine().equals("1")) // 客户端反回了1则开始传输文件
							{
								System.out.println("1111111");
								fis = new FileInputStream(file);
								while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
									System.out.println("xunhuan");
									sumL += length;
									dos = new DataOutputStream(sockettostorage1.getOutputStream());
									System.out.println("已传输：" + ((sumL / l) * 100) + "%");
									dos.write(sendBytes, 0, length);
									dos.flush();
								}
								// 虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
								if (sumL == l) {
									bool = true;
								}
							} else
								System.out.println("地方太小，没有地方存储啦！");
						} catch (Exception e) {
							System.out.println("客户端文件传输1异常");
							bool = false;
							e.printStackTrace();
						} finally {
							if (dos != null)
								try {
									dos.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							if (fis != null)
								try {
									fis.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							if (sockettostorage1 != null)
								try {
									sockettostorage1.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						System.out.println(bool ? "传输1成功" : "传输1失败");
					}

				}.start();
				new Thread() {
					public void run() {
						int port = Integer.parseInt(get[4]);
						int length = 0;
						double sumL = 0;
						byte[] sendBytes = null;
						Socket sockettostorage2 = null;
						DataOutputStream dos = null;
						FileInputStream fis = null;
						PrintWriter pw2 = null;
						BufferedReader br2 = null;
						boolean bool = false;
						try {
							File file = new File(aFile.getName()); // 要传输的文件路径
							long l = file.length();
							sockettostorage2 = new Socket();
							sockettostorage2.connect(new InetSocketAddress(IP, port));
							pw2 = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(sockettostorage2.getOutputStream())),
									true);
							dos = new DataOutputStream(sockettostorage2.getOutputStream());
							pw2.println("1," + get[5]); // 通知云端操作和uuid
							System.out.println(get[5]);
							pw2.flush();
							fis = new FileInputStream(file);
							br2 = new BufferedReader(new InputStreamReader(sockettostorage2.getInputStream()));
							sendBytes = new byte[1024];
							if (br2.readLine().equals("1")) // 如果客户端反回了1则开始传输文件
							{
								while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
									sumL += length;
									System.out.println("已传输：" + ((sumL / l) * 100) + "%");
									dos.write(sendBytes, 0, length);
									dos.flush();
								}
								// 虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
								if (sumL == l) {
									bool = true;
								}
							} else
								System.out.println("地方太小，没有地方存储啦！");
						} catch (Exception e) {
							System.out.println("客户端文件传输2异常");
							bool = false;
							e.printStackTrace();
						} finally {
							if (dos != null)
								try {
									dos.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							if (fis != null)
								try {
									fis.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							if (sockettostorage2 != null)
								try {
									sockettostorage2.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						System.out.println(bool ? "传输2成功" : "传输2失败");
					}

				}.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 客户端向server发出一条指令“2+uuid”，server向它返回一个字符串
	 * IP地址加端口号，然后客户端和这个端口号建立socket连接，接收文件 客户端不需要多线程，storage和server需要多线程
	 * 
	 * @param uuid
	 *            要下载的文件的uuid（唯一标识）
	 * @throws Exception
	 */
	public void download(String uuid) throws Exception // 下载文件
	{
		Socket sockettoserver = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		sockettoserver = new Socket(IP, port1);
		System.out.println("socket:" + sockettoserver);
		br = new BufferedReader(new InputStreamReader(sockettoserver.getInputStream()));
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettoserver.getOutputStream())), true);
		pw.println("2," + uuid);
		pw.flush();
		String string = br.readLine();
		System.out.println(string);
		String[] get = string.split(",");// 假装得到了存储该文件的存储器的端口

		/*
		 * if (get.length == 3) // 如果一个云端死了，则只传一个可用的地址 { int port1 =
		 * Integer.parseInt(get[2]); if (get[0].equals("1")) { Socket
		 * sockettostorage = new Socket(IP, port1);
		 * System.out.println("wojindaoxunhuanlile "); br = new
		 * BufferedReader(new
		 * InputStreamReader(sockettostorage.getInputStream())); pw = new
		 * PrintWriter(new BufferedWriter(new
		 * OutputStreamWriter(sockettostorage.getOutputStream())), true);
		 * pw.println("2," + uuid); pw.flush(); byte[] inputByte = null; int
		 * length = 0; if (br.readLine().equals("1")) { pw.println("1");
		 * pw.flush(); DataInputStream dis = null; FileOutputStream fos = null;
		 * try { dis = new DataInputStream(sockettostorage.getInputStream());
		 * File f = new File(uuid); fos = new FileOutputStream(f); inputByte =
		 * new byte[1024]; System.out.println("开始接收数据..."); while ((length =
		 * dis.read(inputByte, 0, inputByte.length)) > 0) { fos.write(inputByte,
		 * 0, length); fos.flush(); } System.out.println("完成接收"); }
		 * 
		 * 
		 * finally { if (fos != null) fos.close(); if (dis != null) dis.close();
		 * if (sockettostorage != null) sockettostorage.close(); }
		 * 
		 * 
		 * fos.close(); dis.close(); sockettostorage.close();
		 * 
		 * catch (Exception e) { System.out.println("文件下载失败");
		 * e.printStackTrace(); } }
		 * 
		 * } } // length==3
		 */ if (get.length == 5) // 如果两个storage都活着，则两个都传，来实现宕机重传
		{
			int port1 = Integer.parseInt(get[2]);
			int port2 = Integer.parseInt(get[4]);
			if (get[0].equals("1")) { // 找到了文件，收到了地址

				try {
					Socket sockettostorage = new Socket(IP, port1);
					System.out.println("我在第一个云端收数据");
					br = new BufferedReader(new InputStreamReader(sockettostorage.getInputStream()));
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettostorage.getOutputStream())),
							true);
					pw.println("2," + uuid);
					pw.flush();
					byte[] inputByte = null;
					int length = 0;
					if (br.readLine().equals("1")) { // 收到1代表我可以开始接受了
						pw.println("1");
						pw.flush();
						DataInputStream dis = null;
						FileOutputStream fos = null;
						try {
							dis = new DataInputStream(sockettostorage.getInputStream());
							File f = new File(uuid);
							fos = new FileOutputStream(f);
							inputByte = new byte[1024];
							System.out.println("开始接收数据...");
							while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
								fos.write(inputByte, 0, length);
								fos.flush();
							}
							System.out.println("完成接收");
							sockettostorage.close();

						} // try

						catch (SocketException e) {
							System.out.println("文件下载失败");
							e.printStackTrace();

						} // catch
					} // 我接收完了
				}

				catch (Exception e) {
					File oldfile = new File(uuid);
					if (oldfile.exists()) {
						System.out.println("22222222222222222222222222222222222222222222222");
						
						oldfile .delete();
					}
					System.out.println("1111111111111111111111111111111111111111111111");
					secondTransfer(IP, port2, uuid);

				} // catch

			} else
				System.out.println("没有找到这个文件");

		} // length==5

		else
			System.out.println("很不幸，您的文件丢失了，我们对此感到抱歉");

	}

	/**
	 * 第二次传输，用于第一次传输异常之后在catch模块中执行
	 * 
	 * @param ip
	 *            服务器的IP
	 * @param port
	 *            服务器的端口
	 * @param uuid
	 *            要下载文件的uuid（唯一标识）
	 * @throws IOException
	 */
	public void secondTransfer(String ip, int port, String uuid) throws IOException {              //第二次传输
		Socket sockettostorage2 = new Socket(IP, port); // 第二次连上了
		BufferedReader br = null;
		PrintWriter pw = null;
		System.out.println("我要开始尝试第二个云端了 ");
		br = new BufferedReader(new InputStreamReader(sockettostorage2.getInputStream()));
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettostorage2.getOutputStream())), true);
		pw.println("2," + uuid);
		pw.flush();

		if (br.readLine().equals("1")) { // 可以接收文件了
			pw.println("1");
			pw.flush();
			DataInputStream dis2 = null;
			FileOutputStream fos2 = null;
			byte[] inputByte2 = null;
			int length2 = 0;
			try {
				dis2 = new DataInputStream(sockettostorage2.getInputStream());
				File f = new File(uuid );
				fos2 = new FileOutputStream(f);
				inputByte2 = new byte[1024];
				System.out.println("开始接收数据...");
				while ((length2 = dis2.read(inputByte2, 0, inputByte2.length)) > 0) {
					fos2.write(inputByte2, 0, length2);
					fos2.flush();
				}
				System.out.println("完成接收");
			}

			catch (Exception e) {
				System.out.println("文件下载失败");
				e.printStackTrace();
			}
		} // 第二次接收文件
	}

	/**
	 * 删除文件，客户端向server发送一个字符串“3，uuid” 剩下的操作都交给server来完成，最后客户端会收到server的一个返回值
	 * 来判断是否成功删除了文件
	 * 
	 * @param uuid
	 *            要删除文件的uuid（唯一标识）
	 * @return
	 */
	public boolean delete(String uuid) // 删除文件
	{
		Socket sockettoserver = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			sockettoserver = new Socket(IP, port1);
			System.out.println("socket:" + sockettoserver);
			br = new BufferedReader(new InputStreamReader(sockettoserver.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettoserver.getOutputStream())), true);
			pw.println("3," + uuid);
			pw.flush();
			int shanchu = Integer.parseInt(br.readLine());
			System.out.println("       " + Integer.toString(shanchu) + "        我要是1你就成功了");
			sockettoserver.close();// 假装得到了返回值
			br.close();
			pw.close();
			if (shanchu == 1) {
				System.out.println("删除文件成功");
				return true;
			} else {
				System.out.println("删除文件失败");
				return false;
			}
		} catch (Exception e) {
			System.out.println("文件删除异常");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 文件改名函数，客户端将“4，uuid，新名字“直接传给server， 剩下的事情交给server去做，直到server返回一个参数给客户端
	 * 客户端输出是否成功改名
	 * 
	 * @param uuid
	 *            参数1，uuid
	 * @param newname
	 *            参数2，新的名字
	 * @return
	 */
	public boolean rename(String uuid, String newname) // 文件改名
	{
		Socket sockettoserver = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			sockettoserver = new Socket(IP, port1);
			System.out.println("socket:" + sockettoserver);
			br = new BufferedReader(new InputStreamReader(sockettoserver.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettoserver.getOutputStream())), true);
			pw.println("4," + uuid + "," + newname);
			pw.flush();
			int genggai = Integer.parseInt(br.readLine()); // 假装得到了返回值
			System.out.println("       " + Integer.toString(genggai) + "        我要是1你就成功了");
			sockettoserver.close();
			br.close();
			pw.close();
			if (genggai == 1) {
				System.out.println("文件更名成功");
				return true;
			} else {
				System.out.println("文件更名失败");
				return false;
			}
		} catch (Exception e) {
			System.out.println("文件更名异常");
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		FileClient fileClient = new FileClient();
		 //fileClient.upload(new File("a.rmvb"));
		 //fileClient.download("c8c6e923-0ee1-471c-983d-72ac28770b40new.rmvb");
		fileClient.delete("c8c6e923-0ee1-471c-983d-72ac28770b40new.rmvb");
		// fileClient.rename("c8c6e923-0ee1-471c-983d-72ac28770b40a.rmvb","new.rmvb");
	}
}
