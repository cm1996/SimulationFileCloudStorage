package com.liyu.model;

import java.util.UUID;
import java.util.jar.Attributes.Name;

import com.liyu.data.FileData;
import com.liyu.data.StorageData;

public class MyFile {
	String fileName;
	String uuid;
	String uuidEx;
	public String getUuidEx() {
		return uuidEx;
	}
	
	public MyFile(String fileName, String uuid, String uuidEx, long fileLength,
			Storage[] storages) {
		super();
		this.fileName = fileName;
		this.uuid = uuid;
		this.uuidEx = uuidEx;
		this.fileLength = fileLength;
		this.storages = storages;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setUuidEx(String uuidEx) {
		this.uuidEx = uuidEx;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	long fileLength;
	Storage[] storages;
	public Storage[] getStorages() {
		return storages;
	}
	public void setStorages(Storage[] storages) {
		this.storages = storages;
	}
	//Storage location2;
	public MyFile(String fileName,String length){
		//通过读到的string 配置这个类 
		this.fileName = fileName;
		uuidEx = UUID.randomUUID().toString();
		uuid = uuidEx+fileName;
		fileLength = Integer.parseInt(length);	
		storages = new Storage[2];
	}
	public void updateName(String newName){
		uuid = uuidEx + newName;
		fileName = newName;
	}
	public String getUuid() {
		return uuid;
	}
	public long getFileLength() {
		return fileLength;
	}
	/*public void addToData(){
		//选择两个服务器 存储
		//storages = StorageData.getUsableStorage(this);
		FileData.getFileData().addFile(this);
		//改表操作集中在这里 
	}*/
}
