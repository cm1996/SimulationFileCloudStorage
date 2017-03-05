package ffile_storage;

import java.io.*;
import java.net.*;

public class Protocol {

	/**
	 * �������� 1�ϴ� 2���� 3ɾ�� 4����
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
				// �½�һ���ļ�
				file.createNewFile();
				System.out.println("11111111111111111111111111111111111");
				pw.println("1");
				pw.flush();
				// ���� �ɹ����server
				byte[] inputByte = null;
				int length = 0;
				dis = null;
				fos = null;
				try {
					try {
						dis = new DataInputStream(socket.getInputStream());

						/*
						 * �ļ��洢λ��
						 */
						fos = new FileOutputStream(file);
						inputByte = new byte[1024];
						System.out.println("��ʼ��������...");
						while ((length = dis.read(inputByte, 0,
								inputByte.length)) > 0) {
							fos.write(inputByte, 0, length);
							fos.flush();
						}
						System.out.println("��ɽ��գ�");
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
				System.out.println("fuck u anyway��������������������������������3,"
						+ Storage.name + file.getName());
				pwToServer.flush();
				pwToServer.close();
				break;
			case 2:
				// ����
				System.out.println("hello? It's me.");
				if (file.exists()) {
					pw.println("1");// ����ļ����� �ҷ���1����
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
								System.out.println("д������ѭ��");
								sumL += length2;
								System.out.println("�Ѵ��䣺" + ((sumL / l) * 100)
										+ "%");
								dos.write(sendBytes, 0, length2);
								dos.flush();
							}
							// ��Ȼ�������Ͳ�ͬ����JAVA���Զ�ת������ͬ�������ͺ������Ƚ�
							if (sumL == l) {
								bool = true;
							}
						} catch (Exception e) {
							System.out.println("�ͻ����ļ������쳣");
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
						System.out.println(bool ? "�ɹ�" : "ʧ��");
					}
					System.out.println("���ڣ�����");
					// ����

				} else {
					pw.println("0");
					pw.flush();
				}
				break;
			case 3:// ɾ��
				if (file.exists()) {
					// ɾ�� ����server
					file.delete();
					pw.println("1");
				} else {
					pw.println("0");
				}
				pw.flush();
				break;
			case 4:// ����
				if (file.exists()) {
					// ���� ���߷�����
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
