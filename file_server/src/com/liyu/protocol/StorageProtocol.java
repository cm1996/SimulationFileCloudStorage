package com.liyu.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.liyu.data.FileData;
import com.liyu.data.StorageData;
import com.liyu.model.Storage;
import com.liyu.utils.UpdateUtils;

/**
 * @author ��˵�˲����� ���첻��д�� ����д��ע�Ͱ� ���������Ҫ��ʲô ������Ҫ����ͨ�ŵ� ����Ǵ���storage�������
 *         ********************************************************
 *         storage���������������˵�� Ȼ���Ҿ����������ע��һ����Ϣ ��ʽ����
 *         1,storageName,ip,port,size,leftSize, fileNum,isUsable�� ǰ���1��ʾ ��Ϣ����
 *         ��˼������������� ���Ǹ�����һ�� �������� ��崻�֮�󡣡����ٸ��Ҵ�һ���������Ϣ ������һ��������0��isUsable�϶�����0��
 *         ******************************************************** Ȼ������û���˰ɣ�
 *         ò�ƾ�û�б����ϵ�ˣ���Ϊ�ܶ�Ҫ������ ����ClientProtocol ������� ���� �������Ҫд������ ����������Ч�ʱȽϸ߰�
 *         ��һ����д�˵�server ��Ҫд�������� ������һ�����Ǹ�storageò�ƺ��е㹤���� ������������ĩ��������Ĵ��Ū���
 *         ֮����԰����� Ŷ ���� ������������д�ļ��ĿӸ��㡣��������
 */
public class StorageProtocol implements Protocol {
	InetAddress ia = null; // storage��IP
	BufferedReader br = null;
	PrintWriter pw = null;

	@Override
	public void service(Socket socket) {
		try {
			String msg = socket.getInetAddress().getHostAddress() + ": ";
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String str = br.readLine();
			System.out.println("Storage Socket Message:" + str);
			// socket.shutdownInput();
			System.out.println(str);
			String[] requests = str.split(",");
			//
			switch (Integer.parseInt(requests[0])) {
			case 1: // ������������,�Ҹ���ע����Ϣ,���ҵı�
				// 1,name,ip,port,volume
				// 1,storageName,ip,port,size,leftSize, fileNum,isUsable
				Storage storage = StorageData.getStorageData()
						.getStorageByName(requests[1]);
				// ���ǵ�һ������ʱ
				if (storage == null) {
					StorageData.getStorageData().addStorage(
							new Storage(requests[1] // name
									, requests[2] // ip
									, requests[3] // port
									, Long.parseLong(requests[4]) * 1024 // ��С
									, Long.parseLong(requests[4]) * 1024 // ʣ���С
									, 0 // �ļ���
									, true)); // ����
				}
				// ����崻�����������ʱ ��������Ϣ��������дһ��
				else {
					/*
					 * storage.setStorageName(requests[1]);
					 * storage.setIp(requests[2]); storage.setPort(requests[3]);
					 * storage.setSize(Long.parseLong(requests[4]));
					 * storage.setLeftSize(Long.parseLong(requests[5]));
					 * storage.setFileNum(Integer.parseInt(requests[6]));
					 */
					storage.setUsable(true);
				}
				/*
				 * pw.println("1"); //��֪���� pw.flush();
				 */

				break;
			case 2: // ��崻��� �Ҹı�
				System.out.println("shit �ǲ����и���崻��ˣ�����������������");
				StorageData.getStorageData().getStorageByName(requests[1])
						.setUsable(false);
				// �ҾͲ������� �Ͼ����Ѿ���Ҳ��������
				break;
			case 3:
				System.out.println("you tell me");

				UpdateUtils.bindFileToStorage(StorageData.getStorageData()
						.getStorageByName(requests[1]), FileData.getFileData()
						.getFileByUUID(requests[2]));

				FileData.getFileData().writeToFile(
						FileData.getFileData().getFileByUUID(requests[2]));

			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			/*
			 * pw.print("0"); pw.flush();
			 */
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.close();
	}

}
