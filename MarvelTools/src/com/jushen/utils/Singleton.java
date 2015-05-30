package com.jushen.utils;
//����
public class Singleton {
	//����volatile���εı�����jvm�����ֻ�Ǳ�֤�����ڴ���ص��̹߳����ڴ��ֵ�����µ�
	private volatile static Singleton singleton;

	private Singleton() {
	}

	public static Singleton getSingleton() {
		if (singleton == null) {
			//Java���ԵĹؼ��֣�������������һ����������һ��������ʱ���ܹ���֤��ͬһʱ�����ֻ��һ���߳�ִ�иöδ��롣
			synchronized (Singleton.class) {
				if (singleton == null) {
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
}