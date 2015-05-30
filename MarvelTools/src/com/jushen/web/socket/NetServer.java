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
				// 创建一个ServerSocket在端口4700监听客户请求
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);
				// 出错，打印出错信息
			}
			
			Socket socket = null;
			try {
				socket = server.accept();
				// 使用accept()阻塞等待客户请求，有客户
				// 请求到来则产生一个Socket对象，并继续执行
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtils.i("Server accept encouter error:" + e.toString());
				// 出错，打印出错信
			}
			
			LoggerUtils.i("Server Accept Client");
			
			int readchar = 0;
			 is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 由Socket对象得到输入流，并构造相应的BufferedReader对象
			 os = new PrintWriter(socket.getOutputStream());
			// 由Socket对象得到输出流，并构造PrintWriter对象
			//BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			// 由系统标准输入设备构造BufferedReader对象
			//LoggerUtils.i("From Client:" + is.readLine());
			
			// 在标准输出上打印从客户端读入的字符串
			
			// 从标准输入读入一字符串
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
				// 如果该字符串为 "bye"，则停止循环
				//os.append("Server Str");
				// 向客户端输出该字符串
				//os.flush();
				// 刷新输出流，使Client马上收到该字符串
				//System.out.println("Server:" + line);
				// 在系统标准输出上打印读入的字符串
			//	Log.i(LoggerUtils.TAG, "From Client:" + is.readLine());
				// 从Client读入一字符串，并打印到标准输出上
				//line = sin.readLine();
				// 从系统标准输入读入一字符串
			//	Thread.sleep(5000);
			// 继续循环
//			os.close(); // 关闭Socket输出流
//			is.close(); // 关闭Socket输入流
//			socket.close(); // 关闭Socket
//			server.close(); // 关闭ServerSocket
		} catch (Exception e) {
			System.out.println("Error:" + e);
			// 出错，打印出错信息
		}
	}
	
	public static void Send(String vString){
		Log.i(LoggerUtils.TAG, "Sever start to send:" + vString);
		os.write(vString);
		// 向客户端输出该字符串
		os.flush();
	}
}