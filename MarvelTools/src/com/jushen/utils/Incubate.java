package com.jushen.utils;

import java.util.LinkedList;
import java.util.Queue;

public class Incubate<E>{

	Queue<E> _IncubateContainer = new LinkedList<E>();

	public E dequeue(){
		if(_IncubateContainer.size() == 0)
			return null;
		E pollE = _IncubateContainer.poll(); 
		//LoggerUtils.i("There is a recyle one");
		return pollE;
	}
	
	public void enqueue(E vE){
		_IncubateContainer.add(vE);
	}
}
