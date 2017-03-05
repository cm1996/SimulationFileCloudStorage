package com.liyu.protocol;
/**
	 * �ϴ��ļ�������
	 * 1���õ��ͻ�����Ϣ
	 * 2��ͨ����Ϣ����ļ�����ѡ�����̨�洢���
	 * 3���������ͻ��˽ڵ���Ϣ��ip port��
	 * 4���޸ı��ֵ
	 * ************************************************
	 * �����ļ�������
	 * 1���õ��ͻ���Ϣ
	 * 2����� ��ѡ��һ̨������
	 * 3���ѷ���������Ϣ IP port���ظ��ͻ���
	 * ************************************************
	 * ɾ��
	 * 1���õ��ͻ���Ϣ
	 * 2��֪ͨ��ɾ��
	 * 3���ı�
	 * 4�������ͻ���
	 * ************************************************
	 * ����
	 * 1���õ��ͻ���Ϣ
	 * 2��֪ͨ�Ƹ���
	 * 3���ı�
	 * 4�������ͻ���
	 * ************************************************
	 * ���иı�����ɺ�̨Storage֪ͨ��֮������ִ�У���Ҫ��������
	 *
	 */
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.lang.model.element.Element;
import javax.swing.text.AbstractDocument.BranchElement;

import com.liyu.data.FileData;
import com.liyu.data.StorageData;
import com.liyu.model.MyFile;
import com.liyu.model.Storage;
import com.liyu.utils.UpdateUtils;

public class ClientProtocol implements Protocol {
	InetAddress ia = null;  //�ͻ���IP
	//OutputStream os = null;   
    //InputStream is = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	
	@Override
	public void service(Socket socket) {
		ia = socket.getInetAddress();
		//�õ��ͻ��˴����ҵ��ַ���
		try {
			//is = socket.getInputStream();
			String msg = socket.getInetAddress().getHostAddress() + ": ";  
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);  
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            String str= br.readLine();
            System.out.println("Client Socket Message:"+str);    
			socket.shutdownInput(); 
			String[] requests = str.split(",");
			MyFile thisFile;
			switch (Integer.parseInt(requests[0])) {
			case 1://�ϴ��ļ�
				/**
				 * ��������ͨ����� �õ���̨�������� ip �� port  Ȼ���Ҵ�����
				 * 
				 */
				thisFile = new MyFile(requests[1], requests[2]);//�����ļ�
				Storage[] storages = StorageData.getStorageData().getUsableStorage(thisFile); //�õ����ô洢��
				thisFile.setStorages(storages);
				UpdateUtils.addFile(thisFile); //�����ļ��������
				if(storages[0]!=null&&storages[1]!=null){
					//pw.println("1,127.0.0.1,10001,127.0.0.1,10002,uuid"+requests[1]);
				pw.println("1"
						+","+storages[0].getIp()
						+","+storages[0].getPort()
						+","+storages[1].getIp()
						+","+storages[1].getPort()
						+","+thisFile.getUuid());
				
				} else {
					pw.println("0"); //���û�п��ô洢�����Ǿ�û�취��
				}
				
				pw.flush();
				break;
				
			case 2://�����ļ�
				thisFile = FileData.getFileData().getFileByUUID(requests[1]); //�ҵ��ļ���
				System.out.println("shit this step does not matter");
				if(thisFile.getStorages()!=null){
					System.out.println("bind fucking error");
					String msgToClient = "1";
					System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+thisFile.getStorages().length);
					for(Storage s:thisFile.getStorages()){
						System.out.println("I am in this fkin circle");
						if(s!=null&&s.isUsable()){
							System.out.println("Not here");
							//pw.println("127.0.0.1,20001");
							//pw.println("1,"+s.getIp()+","+s.getPort()); //�ҵ��洢�� �͸��㷵�� Ȼ������Լ�ȥ�����ɡ�����
							msgToClient +=","+s.getIp()+","+s.getPort();
							//System.out.println("E$^O(&QYTI         "+"1,"+s.getIp()+","+s.getPort());
							//pw.flush();
							
						}
					}
					System.out.println("~~~~~~~~~~~~~~~~~~"+msgToClient);
					pw.println(msgToClient);
					pw.flush();
				}
					
				else{
					pw.println("0");//�޿ɷ��
					pw.flush();
				}
				break;
			case 3://ɾ���ļ�
				//�Ƚ���������� Ȼ����ȥ����storage Ū����֮�� storage������ Ȼ���Ҹı� Ȼ���ҷ�������
				String uuid = requests[1];
				MyFile file = FileData.getFileData().getFileByUUID(uuid);
				Storage[] ss = file.getStorages();
				PrintWriter pwToStorage=null;
				BufferedReader brToStorage=null;
				if(ss!=null){
					for(int i = 0;i<2;i++){
						Storage s=ss[i];
						Socket toStorage = new Socket(s.getIp(),Integer.parseInt(s.getPort()));
						pwToStorage = new PrintWriter(new BufferedWriter(new OutputStreamWriter(toStorage.getOutputStream())),true);  
			            brToStorage = new BufferedReader(new InputStreamReader(toStorage.getInputStream())); 
			            pwToStorage.println(str); //ֱ�Ӱ�����ҵ���Ϣ�ַ����͸�storage  Ȼ���storage�Լ����������Ϣ
			            pwToStorage.flush();
			            if(brToStorage.readLine().equals("1")){
			            	//�ı�� ������̫�淶 ��Ч�ʸߵ�
			            	if(i==1){ //��ζ�ŵڶ�̨�洢��Ҳɾ���ɹ���
			            		UpdateUtils.deleteFile(file);
			            		pw.println("1");
			            	}
			            	continue;
			            } else {
			            	pw.println("0");
			            	break;
			            }
					}
				} else {
					pw.println("0");
				}
				pw.flush();
				break;
			case 4://����  ���︴�����������  ��������Щ�ֱ� ����������� ������ 8��55 �Һö���
				String uuid2 = requests[1];
				MyFile file2 = FileData.getFileData().getFileByUUID(uuid2);
				System.out.println(file2.getUuid());
				Storage[] ss2 = file2.getStorages();
				System.out.println(ss2.length);
				PrintWriter pwToStorage2=null;
				BufferedReader brToStorage2=null;
				if(ss2!=null){
					for(int i = 0;i<2;i++){
						Storage s=ss2[i];
						Socket toStorage = new Socket(s.getIp(),Integer.parseInt(s.getPort()));
						pwToStorage = new PrintWriter(new BufferedWriter(new OutputStreamWriter(toStorage.getOutputStream())),true);  
			            brToStorage = new BufferedReader(new InputStreamReader(toStorage.getInputStream())); 
			            pwToStorage.println(requests[0]+","+requests[1]+","+file2.getUuidEx()+requests[2]); //ֱ�Ӱ�����ҵ���Ϣ�ַ����͸�storage  Ȼ���storage�Լ����������Ϣ
			            pwToStorage.flush();
			            if(brToStorage.readLine().equals("1")){
			            	if(i==1){ //��ζ�ŵڶ�̨�洢��Ҳ�����ɹ��� ���Ҿ͸ı���
			            		UpdateUtils.updateName(requests[1], requests[2]);
			            		pw.println("1");
			            	}
			            	continue;
			            } else {
			            	pw.println("0");
			            	break;
			            }
					}
				} else {
					pw.println("0");
				}
				pw.flush();
				break;
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
