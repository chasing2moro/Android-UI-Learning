package com.jushen.utils;
//例子
public class Singleton {
	//对于volatile修饰的变量，jvm虚拟机只是保证从主内存加载到线程工作内存的值是最新的
	private volatile static Singleton singleton;

	private Singleton() {
	}

	public static Singleton getSingleton() {
		if (singleton == null) {
			//Java语言的关键字，当它用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。
			synchronized (Singleton.class) {
				if (singleton == null) {
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
}