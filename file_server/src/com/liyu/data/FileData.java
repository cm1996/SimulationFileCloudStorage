package com.liyu.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.liyu.model.*;
import com.liyu.utils.UpdateUtils;
/**
 * 
 * @author not me, but sunny!
 * �������Ǳ�������File��Ϣ�ģ����ǰ���Ϣ������һ�������ļ���
 * ���� �ṩ��һЩ���������Զ��ļ���Ϣ���в���
 */
public class FileData {
	private static FileData fileData = new FileData();
	static private Map<String, MyFile> table = new HashMap<String, MyFile>(); //String �� UUID
	
	PrintWriter pw;
	
	//��ʵ�ڲ���д���ļ��ġ���Ҫ�������д �ҿ��Ű�2333
	private FileData(){
		//���ļ� �ó�һ��map
		try {
			File f = new File("file.txt");
			if(!f.exists())
				f.createNewFile();
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s=br.readLine())!=null){
				String[] strings = s.split(",");
				table.put(strings[1], new MyFile(strings[0]
						, strings[1]
						, strings[2]
						, Long.parseLong(strings[3])
						,new Storage[]{StorageData.getStorageData().getStorageByName(strings[4]),
					StorageData.getStorageData().getStorageByName(strings[5])}));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
		
		try {
			File f = new File("file.txt");
			FileWriter fw;
			fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//��Ȼд�ļ���Ҳ����д
	/*@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		File f = new File("file.txt");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		for(String name:table.keySet()){
			MyFile file = table.get(name);
			pw.println(file.getFileName()+","+file.getUuid()+","+file.getUuidEx()+","+file.getFileLength()+","+file.getStorages()[0].getStorageName()+","+file.getStorages()[1].getStorageName());
		}
		pw.flush();
		pw.close();
		
		super.finalize();
	}*/
	public void deleteFile(MyFile f){
		table.remove(f.getUuid());
		for(String name:table.keySet()){
			MyFile file = table.get(name);
			pw.println(file.getFileName()+","+file.getUuid()+","+file.getUuidEx()+","+file.getFileLength()+","+file.getStorages()[0].getStorageName()+","+file.getStorages()[1].getStorageName());
		}
		pw.flush();
	}
	public void addFile(MyFile file){
		table.put(file.getUuid(), file);
		
	}
	public void writeToFile(MyFile file){
		if(file.getStorages()[1]!=null){
			pw.println(file.getFileName()+","+file.getUuid()+","+file.getUuidEx()+","+file.getFileLength()+","+file.getStorages()[0].getStorageName()+","+file.getStorages()[1].getStorageName());
			pw.flush();
		}
	}
	public void writeFile(){
		File file = new File("file.txt");
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			for(String uuid : table.keySet()){
				MyFile myFile = getFileByUUID(uuid);
				pw.println(myFile.getFileName()+","+myFile.getUuid()+","+myFile.getUuidEx()+","+myFile.getFileLength()+","+myFile.getStorages()[0].getStorageName()+","+myFile.getStorages()[1].getStorageName());
				pw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static FileData getFileData() {
		return fileData;
	}
	public MyFile getFileByUUID(String uuid){
		System.out.println("wocao !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+table.size());
		System.out.println(uuid);
		System.out.println(table.get(uuid).getFileName());
		return table.get(uuid);
	}
	public void updateName(String uuid,String newName){
		//���ļ�
		MyFile file = FileData.getFileData().getFileByUUID(uuid);
		file.updateName(newName);
		//�ı�
		table.remove(uuid);
		table.put(file.getUuid(), file);
		
	}
	
}