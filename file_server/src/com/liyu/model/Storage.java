package com.liyu.model;
/**
 * 
 * @author bastard
 * FileServer在内存中需要管理后端FileStorage服务器的信息，包括名称，ip，端口，容量，实际容量，剩余容量，文件数量，是否可用等信息。
 */
public class Storage {
	String storageName; //唯一的标志
	String ip;
	String port;
	long size;// 容量
	long leftSize;//实际容量
	int fileNum;//文件数量
	boolean isUsable;//是否可用
	public Storage(String storageName, String ip, String port, long size,
			long leftSize, int fileNum, boolean isUsable) {
		this.storageName = storageName;
		this.ip = ip;
		this.port = port;
		this.size = size;
		this.leftSize = leftSize;
		this.fileNum = fileNum;
		this.isUsable = isUsable;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getLeftSize() {
		return leftSize;
	}
	public void setLeftSize(long leftSize) {
		this.leftSize = leftSize;
	}
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	public boolean isUsable() {
		return isUsable;
	}
	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
	

}
