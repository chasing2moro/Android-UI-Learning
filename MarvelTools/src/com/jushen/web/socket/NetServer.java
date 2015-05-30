package com.jushen.web.socket;

import java.io.*;
import java.net.*;

import android.content.IntentSender.SendIntentException;
import android.util.Log;

//http://www.cnblogs.com/linzheng/archive/2011/01/23/1942328.html
import com.jushen.utils.log.LoggerUtils;

public class NetServer {
	static BufferedReader is;
	static PrintWriter os;
	public static void Start() {
		LoggerUtils.i("Server Wait Client");
		try {
			ServerSocket server = null;
			
			try {
				server = new ServerSocket(4700);
				// ����һ��ServerSocket�ڶ˿�4700�����ͻ�����
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);
				// ������ӡ������Ϣ
			}
			
			Socket socket = null;
			try {
				socket = server.accept();
				// ʹ��accept()�����ȴ��ͻ������пͻ�
				// �����������һ��Socket���󣬲�����ִ��
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtils.i("Server accept encouter error:" + e.toString());
				// ������ӡ������
			}
			
			LoggerUtils.i("Server Accept Client");
			
			int readchar = 0;
			 is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// ��Socket����õ�����������������Ӧ��BufferedReader����
			 os = new PrintWriter(socket.getOutputStream());
			// ��Socket����õ��������������PrintWriter����
			//BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			// ��ϵͳ��׼�����豸����BufferedReader����
			//LoggerUtils.i("From Client:" + is.readLine());
			
			// �ڱ�׼����ϴ�ӡ�ӿͻ��˶�����ַ���
			
			// �ӱ�׼�������һ�ַ���
			while (true) {
				readchar = is.read();
				if(readchar == -1){
					LoggerUtils.i("end of buffer");
				}else{
					LoggerUtils.i("Socket byte from Client:" + readchar);
				}
				Thread.sleep(2000);
				//Log.i(LoggerUtils.TAG, "From Client:" + line);
				//Thread.sleep(2000);
			}
				// ������ַ���Ϊ "bye"����ֹͣѭ��
				//os.append("Server Str");
				// ��ͻ���������ַ���
				//os.flush();
				// ˢ���������ʹClient�����յ����ַ���
				//System.out.println("Server:" + line);
				// ��ϵͳ��׼����ϴ�ӡ������ַ���
			//	Log.i(LoggerUtils.TAG, "From Client:" + is.readLine());
				// ��Client����һ�ַ���������ӡ����׼�����
				//line = sin.readLine();
				// ��ϵͳ��׼�������һ�ַ���
			//	Thread.sleep(5000);
			// ����ѭ��
//			os.close(); // �ر�Socket�����
//			is.close(); // �ر�Socket������
//			socket.close(); // �ر�Socket
//			server.close(); // �ر�ServerSocket
		} catch (Exception e) {
			System.out.println("Error:" + e);
			// ������ӡ������Ϣ
		}
	}
	
	public static void Send(String vString){
		Log.i(LoggerUtils.TAG, "Sever start to send:" + vString);
		os.write(vString);
		// ��ͻ���������ַ���
		os.flush();
	}
}