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
 * @author 都说了不是我 今天不想写了 就来写点注释吧 想想我这个要干什么 就是主要和你通信的 额，就是处理storage的请求吧
 *         ********************************************************
 *         storage启动后会主动给我说的 然后我就在这里给你注册一下信息 格式就用
 *         1,storageName,ip,port,size,leftSize, fileNum,isUsable吧 前面的1表示 消息类型
 *         意思就是我这次来呢 就是告诉你一声 我启动啦 你宕机之后。。。再给我传一遍上面的信息 不过第一个数字是0，isUsable肯定就是0了
 *         ******************************************************** 然后好像就没有了吧？
 *         貌似就没有别的联系了，因为很多要做的事 都在ClientProtocol 里完成了 哈哈 我这个就要写好了呢 不过还是你效率比较高啊
 *         我一共才写了点server 你要写两个东西 而且我一想你那个storage貌似很有点工作量 所以我想在周末把我这里的大概弄完吧
 *         之后可以帮你了 哦 对了 我留了两个读写文件的坑给你。。。我懒
 */
public class StorageProtocol implements Protocol {
	InetAddress ia = null; // storage的IP
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
			case 1: // 启动，告诉我,我给你注册信息,改我的表
				// 1,name,ip,port,volume
				// 1,storageName,ip,port,size,leftSize, fileNum,isUsable
				Storage storage = StorageData.getStorageData()
						.getStorageByName(requests[1]);
				// 这是第一次启动时
				if (storage == null) {
					StorageData.getStorageData().addStorage(
							new Storage(requests[1] // name
									, requests[2] // ip
									, requests[3] // port
									, Long.parseLong(requests[4]) * 1024 // 大小
									, Long.parseLong(requests[4]) * 1024 // 剩余大小
									, 0 // 文件数
									, true)); // 可用
				}
				// 这是宕机后重新启动时 把所有信息还是重新写一遍
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
				 * pw.println("1"); //朕知道了 pw.flush();
				 */

				break;
			case 2: // 你宕机了 我改表
				System.out.println("shit 是不是有个人宕机了？？？？？？？？？");
				StorageData.getStorageData().getStorageByName(requests[1])
						.setUsable(false);
				// 我就不回你了 毕竟你已经再也看不到了
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
