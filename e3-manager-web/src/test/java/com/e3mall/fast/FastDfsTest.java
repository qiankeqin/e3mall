package com.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
	@Test
	public void testUpload() throws Exception{
		//创建一个配置文件，文件名任意，内容就是tracker服务器的地址
		//使用全局对象加载配置文件
		ClientGlobal.init("G:/EclipseWorkstation/e3mall-trunk/e3mall/e3-manager-web/src/main/resources/config/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件
		//参数1：图片的全路径
		//参数2：图片的扩展名
		//参数3：元数据，如图片的像素，图片的名称，拍摄日期等等
		String[] strings = storageClient.upload_file("G:/EclipseWorkstation/leaf.jpg", "jpg", null);
		
		for(String str: strings){
			System.out.println(str);
		}
	}
	
	@Test
	public void testFastDFSClient() throws Exception{
		FastDFSClient fastDfsClient = new FastDFSClient("G:/EclipseWorkstation/e3mall-trunk/e3mall/e3-manager-web/src/main/resources/config/client.conf");
		String str = fastDfsClient.uploadFile("G:/EclipseWorkstation/guangao.jpg", "jpg", null);
		System.out.println(str);
	}
}
