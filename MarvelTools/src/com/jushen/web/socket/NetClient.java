package com.jushen.web.socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

import com.jushen.utils.log.LoggerUtils;

public class NetClient {

	public static void Start() {
		LoggerUtils.i("NetClient Start");
		try {
			Socket socket = new Socket("127.0.0.1", 4700);
			
			// �򱾻���4700�˿ڷ����ͻ�����
		//	BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			
			// ��ϵͳ��׼�����豸����BufferedReader����
			BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
			
			// ��Socket����õ��������������PrintWriter����
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//��Socket����õ�����������������Ӧ��BufferedReader����
			String readline = is.readLine();
		//	readline = sin.readLine(); // ��ϵͳ��׼�������һ�ַ���
			while (!readline.isEmpty()) {
				// ���ӱ�׼���������ַ���Ϊ "bye"��ֹͣѭ��
				//os.println(readline);
				//os.write(1);
				// ����ϵͳ��׼���������ַ��������Server
				//os.flush();
				// ˢ���������ʹServer�����յ����ַ���
			//	System.out.println("Client:" + readline);
				// ��ϵͳ��׼����ϴ�ӡ������ַ���
//				String tmpStr= "";
//				while (true) {
//					try {
//						tmpStr = is.readLine();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					if(!tmpStr.isEmpty())
//						LoggerUtils.i("From Server:" + tmpStr);
//					Thread.sleep(1000);
//				}
			
				// ��Server����һ�ַ���������ӡ����׼�����
				Log.i(LoggerUtils.TAG, readline);
				readline = is.readLine(); // ��ϵͳ��׼�������һ�ַ���
			} // ����ѭ��
		//	os.close(); // �ر�Socket�����
		//	is.close(); // �ر�Socket������
		//	socket.close(); // �ر�Socket
		} catch (Exception e) {
			System.out.println("Error" + e); // �������ӡ������Ϣ
		}
	}
}
/*
 Socket socket = new Socket();
socket .connect(new InetSocketAddress(host, port), 20000);
socket .setSoTimeout(45000);
byte[] byte = date;//��Ҫ���������
BufferedInputStream is = new BufferedInputStream(socket.getInputStream(), 512);
DataInputStream dis = new DataInputStream(is);
out = new BufferedOutputStream(socket.getOutputStream());
out.write(bytes);
 * */
 