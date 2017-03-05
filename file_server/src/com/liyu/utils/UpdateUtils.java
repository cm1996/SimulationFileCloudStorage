package com.liyu.utils;

import com.liyu.data.FileData;
import com.liyu.data.StorageData;
import com.liyu.model.MyFile;
import com.liyu.model.Storage;
/**
 * 
 * @author XXX
 * �������ݵĹ����� ��������̫�淶 ��Ƶ�Ҳ���� �����̫�� ��Щ�������÷ŵ������  ����ͼ������� ��������
 */
public class UpdateUtils {
	
	/**
	 * @param f Ҫ��ӵ����ļ�
	 */
	public static void addFile(MyFile f){
		FileData.getFileData().addFile(f);
	}
	/**
	 * �µĴ洢�������� �������һ��֪ͨ��Ȼ���ҾͰ�������Ϣע�����
	 * @param s �Ǹ��洢��
	 */
	public static void addStorage(Storage s){
		StorageData.getStorageData().addStorage(s);
	}
	/**
	 * ���������Ҫ�ǰ�һ���ļ�������ŵĴ洢����������~
	 * @param storage ����Ҵ����Ǹ��洢��
	 * @param myFile  �����ļ�
	 */
	public static void bindFileToStorage(Storage storage,MyFile myFile){
		//���������ı�  ���Ե���ʵ������������е�һЩ����
		//���޸��ļ���Ϣ
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
		//���޸�storage��Ϣ
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





