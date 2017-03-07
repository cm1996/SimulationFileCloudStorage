package file_client;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * �ļ�����Ŀͻ����࣬ʵ�����ϴ����ظ���ɾ��ָ���ļ��ĸ�����
 * 
 * @author liyu
 *
 */
public class FileClient {

	static String IP = "192.168.45.113";
	static int port1 = 11113;

	/**
	 * ����ϴ��ļ��Ĳ���������1���ļ������ļ����ȡ����͸�server�� �õ�һ���ַ�����1/0,IP��ַ1��port1��IP��ַ2��port2��
	 * 1�ǳɹ���0�ǲ��ɹ���һ�δ��������߳� �ֱ���port1��port2�����ļ����䡣
	 * 
	 * @param aFile
	 *            Ҫ������ļ�
	 * @throws Exception
	 */

	public void upload(File aFile) throws Exception // �ϴ��ļ�
	{
		String s = "1," + aFile.getName() + "," + aFile.length();
		Socket sockettoserver = null; // ��fileserver�������ӣ��õ�uuid������filestorage�Ķ˿ں�
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
			String[] get = string.split(","); // �õ��˿��Դ洢���ļ��������洢���Ķ˿ں�
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
						FileInputStream fis = null; // ��Ҫ������ļ�����
						BufferedReader br1 = null; // �ͻ��˺��ƶ�֮�����ݴ���
						boolean bool = false;
						try {
							File file = new File(aFile.getName()); // Ҫ������ļ�·��
							long l = file.length();
							sendBytes = new byte[1024];
							sockettostorage1 = new Socket();
							sockettostorage1.connect(new InetSocketAddress(IP, port));
							pw1 = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(sockettostorage1.getOutputStream())),
									true);
							pw1.println("1," + get[5]); // ֪ͨ�ƶ˲�����uuid
							System.out.println("socket:" + sockettostorage1);
							System.out.println("qwertyy         " + get[5]);
							pw1.flush();
							br1 = new BufferedReader(new InputStreamReader(sockettostorage1.getInputStream()));
							System.out.println("sssss");
							if (br1.readLine().equals("1")) // �ͻ��˷�����1��ʼ�����ļ�
							{
								System.out.println("1111111");
								fis = new FileInputStream(file);
								while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
									System.out.println("xunhuan");
									sumL += length;
									dos = new DataOutputStream(sockettostorage1.getOutputStream());
									System.out.println("�Ѵ��䣺" + ((sumL / l) * 100) + "%");
									dos.write(sendBytes, 0, length);
									dos.flush();
								}
								// ��Ȼ�������Ͳ�ͬ����JAVA���Զ�ת������ͬ�������ͺ������Ƚ�
								if (sumL == l) {
									bool = true;
								}
							} else
								System.out.println("�ط�̫С��û�еط��洢����");
						} catch (Exception e) {
							System.out.println("�ͻ����ļ�����1�쳣");
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
						System.out.println(bool ? "����1�ɹ�" : "����1ʧ��");
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
							File file = new File(aFile.getName()); // Ҫ������ļ�·��
							long l = file.length();
							sockettostorage2 = new Socket();
							sockettostorage2.connect(new InetSocketAddress(IP, port));
							pw2 = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(sockettostorage2.getOutputStream())),
									true);
							dos = new DataOutputStream(sockettostorage2.getOutputStream());
							pw2.println("1," + get[5]); // ֪ͨ�ƶ˲�����uuid
							System.out.println(get[5]);
							pw2.flush();
							fis = new FileInputStream(file);
							br2 = new BufferedReader(new InputStreamReader(sockettostorage2.getInputStream()));
							sendBytes = new byte[1024];
							if (br2.readLine().equals("1")) // ����ͻ��˷�����1��ʼ�����ļ�
							{
								while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
									sumL += length;
									System.out.println("�Ѵ��䣺" + ((sumL / l) * 100) + "%");
									dos.write(sendBytes, 0, length);
									dos.flush();
								}
								// ��Ȼ�������Ͳ�ͬ����JAVA���Զ�ת������ͬ�������ͺ������Ƚ�
								if (sumL == l) {
									bool = true;
								}
							} else
								System.out.println("�ط�̫С��û�еط��洢����");
						} catch (Exception e) {
							System.out.println("�ͻ����ļ�����2�쳣");
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
						System.out.println(bool ? "����2�ɹ�" : "����2ʧ��");
					}

				}.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ͻ�����server����һ��ָ�2+uuid����server��������һ���ַ���
	 * IP��ַ�Ӷ˿ںţ�Ȼ��ͻ��˺�����˿ںŽ���socket���ӣ������ļ� �ͻ��˲���Ҫ���̣߳�storage��server��Ҫ���߳�
	 * 
	 * @param uuid
	 *            Ҫ���ص��ļ���uuid��Ψһ��ʶ��
	 * @throws Exception
	 */
	public void download(String uuid) throws Exception // �����ļ�
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
		String[] get = string.split(",");// ��װ�õ��˴洢���ļ��Ĵ洢���Ķ˿�

		/*
		 * if (get.length == 3) // ���һ���ƶ����ˣ���ֻ��һ�����õĵ�ַ { int port1 =
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
		 * new byte[1024]; System.out.println("��ʼ��������..."); while ((length =
		 * dis.read(inputByte, 0, inputByte.length)) > 0) { fos.write(inputByte,
		 * 0, length); fos.flush(); } System.out.println("��ɽ���"); }
		 * 
		 * 
		 * finally { if (fos != null) fos.close(); if (dis != null) dis.close();
		 * if (sockettostorage != null) sockettostorage.close(); }
		 * 
		 * 
		 * fos.close(); dis.close(); sockettostorage.close();
		 * 
		 * catch (Exception e) { System.out.println("�ļ�����ʧ��");
		 * e.printStackTrace(); } }
		 * 
		 * } } // length==3
		 */ if (get.length == 5) // �������storage�����ţ���������������ʵ��崻��ش�
		{
			int port1 = Integer.parseInt(get[2]);
			int port2 = Integer.parseInt(get[4]);
			if (get[0].equals("1")) { // �ҵ����ļ����յ��˵�ַ

				try {
					Socket sockettostorage = new Socket(IP, port1);
					System.out.println("���ڵ�һ���ƶ�������");
					br = new BufferedReader(new InputStreamReader(sockettostorage.getInputStream()));
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettostorage.getOutputStream())),
							true);
					pw.println("2," + uuid);
					pw.flush();
					byte[] inputByte = null;
					int length = 0;
					if (br.readLine().equals("1")) { // �յ�1�����ҿ��Կ�ʼ������
						pw.println("1");
						pw.flush();
						DataInputStream dis = null;
						FileOutputStream fos = null;
						try {
							dis = new DataInputStream(sockettostorage.getInputStream());
							File f = new File(uuid);
							fos = new FileOutputStream(f);
							inputByte = new byte[1024];
							System.out.println("��ʼ��������...");
							while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
								fos.write(inputByte, 0, length);
								fos.flush();
							}
							System.out.println("��ɽ���");
							sockettostorage.close();

						} // try

						catch (SocketException e) {
							System.out.println("�ļ�����ʧ��");
							e.printStackTrace();

						} // catch
					} // �ҽ�������
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
				System.out.println("û���ҵ�����ļ�");

		} // length==5

		else
			System.out.println("�ܲ��ң������ļ���ʧ�ˣ����ǶԴ˸е���Ǹ");

	}

	/**
	 * �ڶ��δ��䣬���ڵ�һ�δ����쳣֮����catchģ����ִ��
	 * 
	 * @param ip
	 *            ��������IP
	 * @param port
	 *            �������Ķ˿�
	 * @param uuid
	 *            Ҫ�����ļ���uuid��Ψһ��ʶ��
	 * @throws IOException
	 */
	public void secondTransfer(String ip, int port, String uuid) throws IOException {              //�ڶ��δ���
		Socket sockettostorage2 = new Socket(IP, port); // �ڶ���������
		BufferedReader br = null;
		PrintWriter pw = null;
		System.out.println("��Ҫ��ʼ���Եڶ����ƶ��� ");
		br = new BufferedReader(new InputStreamReader(sockettostorage2.getInputStream()));
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockettostorage2.getOutputStream())), true);
		pw.println("2," + uuid);
		pw.flush();

		if (br.readLine().equals("1")) { // ���Խ����ļ���
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
				System.out.println("��ʼ��������...");
				while ((length2 = dis2.read(inputByte2, 0, inputByte2.length)) > 0) {
					fos2.write(inputByte2, 0, length2);
					fos2.flush();
				}
				System.out.println("��ɽ���");
			}

			catch (Exception e) {
				System.out.println("�ļ�����ʧ��");
				e.printStackTrace();
			}
		} // �ڶ��ν����ļ�
	}

	/**
	 * ɾ���ļ����ͻ�����server����һ���ַ�����3��uuid�� ʣ�µĲ���������server����ɣ����ͻ��˻��յ�server��һ������ֵ
	 * ���ж��Ƿ�ɹ�ɾ�����ļ�
	 * 
	 * @param uuid
	 *            Ҫɾ���ļ���uuid��Ψһ��ʶ��
	 * @return
	 */
	public boolean delete(String uuid) // ɾ���ļ�
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
			System.out.println("       " + Integer.toString(shanchu) + "        ��Ҫ��1��ͳɹ���");
			sockettoserver.close();// ��װ�õ��˷���ֵ
			br.close();
			pw.close();
			if (shanchu == 1) {
				System.out.println("ɾ���ļ��ɹ�");
				return true;
			} else {
				System.out.println("ɾ���ļ�ʧ��");
				return false;
			}
		} catch (Exception e) {
			System.out.println("�ļ�ɾ���쳣");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �ļ������������ͻ��˽���4��uuid�������֡�ֱ�Ӵ���server�� ʣ�µ����齻��serverȥ����ֱ��server����һ���������ͻ���
	 * �ͻ�������Ƿ�ɹ�����
	 * 
	 * @param uuid
	 *            ����1��uuid
	 * @param newname
	 *            ����2���µ�����
	 * @return
	 */
	public boolean rename(String uuid, String newname) // �ļ�����
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
			int genggai = Integer.parseInt(br.readLine()); // ��װ�õ��˷���ֵ
			System.out.println("       " + Integer.toString(genggai) + "        ��Ҫ��1��ͳɹ���");
			sockettoserver.close();
			br.close();
			pw.close();
			if (genggai == 1) {
				System.out.println("�ļ������ɹ�");
				return true;
			} else {
				System.out.println("�ļ�����ʧ��");
				return false;
			}
		} catch (Exception e) {
			System.out.println("�ļ������쳣");
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
