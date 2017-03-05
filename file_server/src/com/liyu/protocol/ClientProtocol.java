package com.liyu.protocol;
/**
	 * 上传文件的请求
	 * 1、得到客户端信息
	 * 2、通过信息查表（文件），选择出两台存储结点
	 * 3、返还给客户端节点信息（ip port）
	 * 4、修改表的值
	 * ************************************************
	 * 下载文件的请求
	 * 1、得到客户信息
	 * 2、查表 ，选择一台服务器
	 * 3、把服务器的信息 IP port传回给客户端
	 * ************************************************
	 * 删除
	 * 1、得到客户信息
	 * 2、通知云删除
	 * 3、改表
	 * 4、返还客户端
	 * ************************************************
	 * 改名
	 * 1、得到客户信息
	 * 2、通知云改名
	 * 3、改表
	 * 4、返还客户端
	 * ************************************************
	 * 所有改表操作由后台Storage通知我之后我再执行，不要擅作主张
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
	InetAddress ia = null;  //客户端IP
	//OutputStream os = null;   
    //InputStream is = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	
	@Override
	public void service(Socket socket) {
		ia = socket.getInetAddress();
		//得到客户端传给我的字符串
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
			case 1://上传文件
				/**
				 * 在这里我通过查表 得到两台服务器的 ip 和 port  然后我传给她
				 * 
				 */
				thisFile = new MyFile(requests[1], requests[2]);//创建文件
				Storage[] storages = StorageData.getStorageData().getUsableStorage(thisFile); //得到可用存储器
				thisFile.setStorages(storages);
				UpdateUtils.addFile(thisFile); //将新文件加入表中
				if(storages[0]!=null&&storages[1]!=null){
					//pw.println("1,127.0.0.1,10001,127.0.0.1,10002,uuid"+requests[1]);
				pw.println("1"
						+","+storages[0].getIp()
						+","+storages[0].getPort()
						+","+storages[1].getIp()
						+","+storages[1].getPort()
						+","+thisFile.getUuid());
				
				} else {
					pw.println("0"); //如果没有可用存储器，那就没办法咯
				}
				
				pw.flush();
				break;
				
			case 2://下载文件
				thisFile = FileData.getFileData().getFileByUUID(requests[1]); //找到文件了
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
							//pw.println("1,"+s.getIp()+","+s.getPort()); //找到存储器 就给你返回 然后你就自己去找他吧。。。
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
					pw.println("0");//无可奉告
					pw.flush();
				}
				break;
			case 3://删除文件
				//先接受你的命令 然后我去告诉storage 弄好了之后 storage返还我 然后我改表 然后我返还给你
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
			            pwToStorage.println(str); //直接把你给我的信息字符串送给storage  然后叫storage自己读懂你的信息
			            pwToStorage.flush();
			            if(brToStorage.readLine().equals("1")){
			            	//改表吧 这样不太规范 但效率高点
			            	if(i==1){ //意味着第二台存储器也删除成功了
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
			case 4://改名  这里复制上面的来的  命名都有些粗暴 额。。。现在是 星期六 8点55 我好饿啊
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
			            pwToStorage.println(requests[0]+","+requests[1]+","+file2.getUuidEx()+requests[2]); //直接把你给我的信息字符串送给storage  然后叫storage自己读懂你的信息
			            pwToStorage.flush();
			            if(brToStorage.readLine().equals("1")){
			            	if(i==1){ //意味着第二台存储器也改名成功了 那我就改表了
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
