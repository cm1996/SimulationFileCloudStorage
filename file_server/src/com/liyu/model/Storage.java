package com.liyu.model;
/**
 * 
 * @author bastard
 * FileServer���ڴ�����Ҫ������FileStorage����������Ϣ���������ƣ�ip���˿ڣ�������ʵ��������ʣ���������ļ��������Ƿ���õ���Ϣ��
 */
public class Storage {
	String storageName; //Ψһ�ı�־
	String ip;
	String port;
	long size;// ����
	long leftSize;//ʵ������
	int fileNum;//�ļ�����
	boolean isUsable;//�Ƿ����
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
