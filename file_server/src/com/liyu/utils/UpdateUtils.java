package com.liyu.utils;

import com.liyu.data.FileData;
import com.liyu.data.StorageData;
import com.liyu.model.MyFile;
import com.liyu.model.Storage;
/**
 * 
 * @author XXX
 * 更新数据的工具类 我命名不太规范 设计的也不行 耦合性太高 有些函数不该放到这里的  不过图个方便吧 将就着用
 */
public class UpdateUtils {
	
	/**
	 * @param f 要添加的新文件
	 */
	public static void addFile(MyFile f){
		FileData.getFileData().addFile(f);
	}
	/**
	 * 新的存储器开机后 ，会给我一个通知，然后我就把它的信息注册进来
	 * @param s 那个存储器
	 */
	public static void addStorage(Storage s){
		StorageData.getStorageData().addStorage(s);
	}
	/**
	 * 这个函数主要是把一个文件和它存放的存储器关联起来~
	 * @param storage 你给我传的那个存储器
	 * @param myFile  传的文件
	 */
	public static void bindFileToStorage(Storage storage,MyFile myFile){
		//在这个里面改表  可以调用实体类和数据类中的一些函数
		//先修改文件信息
		if(myFile.getStorages()==null){
			Storage[] storages = new Storage[2];
			myFile.setStorages(storages);
		} else {
			for(int i = 0;i<2;i++){
				if(myFile.getStorages()[i]==null){
					myFile.getStorages()[i] = storage;
					break;
				}
			}
		}
		//再修改storage信息
		storage.setFileNum(storage.getFileNum()+1);
		storage.setLeftSize(storage.getLeftSize()-myFile.getFileLength());
	}
	public static void deleteFile(MyFile f){
		FileData.getFileData().deleteFile(f);
		for(Storage s:f.getStorages()){
			s.setFileNum(s.getFileNum()-1);
			s.setLeftSize(s.getLeftSize()+f.getFileLength());
		}
		FileData.getFileData().writeFile();
		StorageData.getStorageData().writeFile();
	}
	public static void updateName(String uuid,String newName){
		FileData.getFileData().updateName(uuid, newName);
		FileData.getFileData().writeFile();
		StorageData.getStorageData().writeFile();
	}
	
}





