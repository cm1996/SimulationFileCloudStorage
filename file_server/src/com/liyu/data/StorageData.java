package com.liyu.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ParseConversionEvent;

import com.liyu.model.MyFile;
import com.liyu.model.Storage;
/**
 * 
 * @author Doctor? Which doctor? Doctor who?
 * 这个玩意儿是 管理所有存储器信息的~
 *
 */
public class StorageData {
	PrintWriter pw;

	private static StorageData storageData; 
	//static ArrayList<Storage> storages = new ArrayList<>();
	static Map<String, Storage> storages ;
	//这个也是。。原谅我
	private StorageData(){
		
		 storages =new HashMap<String, Storage>();
		//读文件 然后建表
		
		try {
			File file = new File("storage.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String string;
			while((string=br.readLine())!=null){
				String[] strings = string.split(",");
				System.out.println("我从文件里读出的是     "+string);
				
					Storage storage = 	new Storage(strings[0]
						, strings[1]
						, strings[2]
						, Long.parseLong(strings[3])
						, Long.parseLong(strings[4])
						, Integer.parseInt(strings[5])
						, strings[6].equals("true")?true:false);
				
				storages.put(strings[0],storage);
			}
			br.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("storage.txt"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("storage.txt"))));
		for(String string:storages.keySet()){
			Storage storage = storages.get(string);
			pw.println(storage.getStorageName()+","+storage.getIp()+","+storage.getPort()+","+storage.getSize()+","+storage.getLeftSize()+","+storage.getFileNum()+","+storage.isUsable());
		}
		pw.flush();
		pw.close();
		super.finalize();
	}*/
	public void addStorage(Storage storage){
		storages.put(storage.getStorageName(), storage);
		pw.println(storage.getStorageName()+","+storage.getIp()+","+storage.getPort()+","+storage.getSize()+","+storage.getLeftSize()+","+storage.getFileNum()+","+storage.isUsable());
		pw.flush();
	}
	public static StorageData getStorageData(){
		if(storageData==null){
			System.out.println("shit 11111");
			storageData = new StorageData();
			System.out.println("shit 22222");
		}
		return storageData;
	}
	public Storage getStorageByName(String name){
		return storages.get(name);
	}
	/**
	 * 通过传入的文件，去表中查找可用的存储器
	 * @param f 要进行存储的文件
	 * @return 返还一个Storage数组
	 */
	public Storage[] getUsableStorage(MyFile f){
		Storage[] ss = new Storage[2];
		for(Storage storage:storages.values()){
			if(storage.isUsable()&&storage.getLeftSize()>=f.getFileLength()){
				if(ss[0]==null){
					ss[0] = storage;
					continue;
				} else if(ss[1]==null){
					ss[1] = storage;
					break;
				}	
			} 
		}
		return ss;
	}
	public void  writeFile() {
		File file = new File("storage.txt");
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			for(String name:storages.keySet()){
			Storage storage = storages.get(name);
			pw.println(storage.getStorageName()+","+storage.getIp()+","+storage.getPort()+","+storage.getSize()+","+storage.getLeftSize()+","+storage.getFileNum()+","+storage.isUsable());
			pw.flush();
			}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}









