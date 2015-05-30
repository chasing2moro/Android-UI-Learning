package com.jushen.framework.event;

import android.os.Bundle;

public class EventArg{
	public Bundle _Bundle;
	public EventArg(){
		_Bundle = new Bundle();
	}
	
	public void clear() {
		_Bundle.clear();
	}
	
//setter
    void putBoolean(String key, boolean value) {
    	_Bundle.putBoolean(key, value);
    }

    void putByte(String key, byte value) {
        _Bundle.putByte(key, value);
    }

    void putChar(String key, char value) {
        _Bundle.putChar(key, value);
    }

    void putShort(String key, short value) {
    	_Bundle.putShort(key, value);
    }

    public void putInt(String key, int value) {
    	_Bundle.putInt(key, value);
    }

    public void putLong(String key, long value) {
    	_Bundle.putLong(key, value);
    }

    void putFloat(String key, float value) {
    	_Bundle.putFloat(key, value);
    }

    public void putDouble(String key, double value) {
    	_Bundle.putDouble(key, value);
    }

    public void putString(String key, String value) {
    	_Bundle.putString(key, value);
    }
    
    //getter
    public boolean getBoolean(String key) {
        return _Bundle.getBoolean(key);
    }
    public boolean getBoolean(String key, boolean defaultValue) {
        return _Bundle.getBoolean(key, defaultValue);
    }

    public byte getByte(String key) {
        return _Bundle.getByte(key);
    }
    public Byte getByte(String key, byte defaultValue) {
        return _Bundle.getByte(key, defaultValue);
    }

    public char getChar(String key) {
        return _Bundle.getChar(key);
    }
    public char getChar(String key, char defaultValue) {
        return _Bundle.getChar(key, defaultValue);
    }

    public short getShort(String key) {
        return _Bundle.getShort(key);
    }
    public short getShort(String key, short defaultValue) {
        return _Bundle.getShort(key, defaultValue);
    }
    
    public int getInt(String key) {
        return _Bundle.getInt(key);
    }
    public int getInt(String key, int defaultValue) {
        return _Bundle.getInt(key, defaultValue);
    }

    public float getFloat(String key) {
        return _Bundle.getFloat(key);
    }
    public float getFloat(String key, float defaultValue) {
        return _Bundle.getFloat(key, defaultValue);
    }

    public String getString(String key) {
        return _Bundle.getString(key);
    }
    public String getString(String key, String defaultValue) {
        return _Bundle.getString(key, defaultValue);
    }


}
//http://blog.csdn.net/zuolongsnail/article/details/8703432
// ��Android��ͨ��Intentʹ��Bundle���ݶ���
//Android��������ʱ��Ҫ��Ӧ���л���̼䴫�ݶ���������ϸ����Intentʹ��Bundle���ݶ���ķ�����
//�����ݵĶ�����Ҫ��ʵ�����л��������л����������ַ�ʽ��java.io.Serializable��android.os.Parcelable
//
//Java��ʹ�õ���Serializable�����ȸ���Androidʹ�����Զ����Parcelable��
//�������л���ʽ������
//1.��ʹ���ڴ��ʱ��Parcelable��Serializable���ܸߣ��Ƽ�ʹ��Parcelable�ࣻ
//2.Serializable�����л���ʱ��������������ʱ�������Ӷ�����Ƶ����GC��
//3.Parcelable����ʹ����Ҫ�����ݴ洢�ڴ����ϵ��������ΪParcelable���ܺܺõı�֤���ݵĳ�����������б仯������£������������ʹ��Serializable��
//
//��һ�����������л�ʵ���ࣻ
//Serializable��ʽ��
///**
// * PersonSer
// * 
// * @author http://blog.csdn.net/zuolongsnail
// */
//public class PersonSer implements Serializable {
//
//	/**
//	 * serialVersionUID�����������޸�ʵ����󣬿������������л��ͷ����л����ڴ˲���˵����ҿ��Թȸ�ٶ��¡�
//	 */
//	private static final long serialVersionUID = -7620435178023928252L;
//
//	private String name;
//
//	private int age;
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public int getAge() {
//		return age;
//	}
//
//	public void setAge(int age) {
//		this.age = age;
//	}
//
//}
//Parcelable��ʽ��
///**
// * PersonPar
// * 
// * @author http://blog.csdn.net/zuolongsnail
// */
//public class PersonPar implements Parcelable {
//
//	private String name;
//
//	private int age;
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public int getAge() {
//		return age;
//	}
//
//	public void setAge(int age) {
//		this.age = age;
//	}
//
//	/**
//	 * ���л�ʵ����
//	 */
//	public static final Parcelable.Creator<PersonPar> CREATOR = new Creator<PersonPar>() {
//		public PersonPar createFromParcel(Parcel source) {
//			PersonPar personPar = new PersonPar();
//			personPar.name = source.readString();
//			personPar.age = source.readInt();
//			return personPar;
//		}
//
//		public PersonPar[] newArray(int size) {
//			return new PersonPar[size];
//		}
//	};
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	/**
//	 * ��ʵ��������д��Parcel
//	 */
//	@Override
//	public void writeToParcel(Parcel parcel, int flags) {
//		parcel.writeString(name);
//		parcel.writeInt(age);
//	}
//
//}
//
//�ڶ������������л�����
//Intent intent = new Intent(MainActivity.this, DestActivity.class);
//Bundle bundle = new Bundle();
//switch (v.getId()) {
//case R.id.serializable_btn:
//	// Serializable���ݶ���
//	PersonSer personSer = new PersonSer();
//	personSer.setName("zuolong");
//	personSer.setAge(26);
//	bundle.putSerializable(SER_KEY, personSer);
//	intent.putExtra("type", SER_TYPE);
//	intent.putExtras(bundle);
//	startActivity(intent);
//	break;
//case R.id.parcelable_btn:
//	// Parcelable���ݶ���
//	PersonPar personPar = new PersonPar();
//	personPar.setName("snail");
//	personPar.setAge(27);
//	// Bundle��putParcelableArray��putParcelableArrayList������Ҳ�Ϳ��Դ���������б�
//	bundle.putParcelable(PAR_KEY, personPar);
//	intent.putExtra("type", PAR_TYPE);
//	// IntentҲ��putParcelableArrayListExtra���������Դ���ʵ��Parcelable�ӿڵĶ����б�
//	intent.putExtras(bundle);
//	startActivity(intent);
//	break;
//}
//����������ȡ����
//StringBuffer sb = new StringBuffer();
//String type = getIntent().getStringExtra("type");
//// ����type�ж�����������
//if (type.equals(MainActivity.SER_TYPE)) {
//	// ��ȡSerializable����
//	PersonSer personSer = (PersonSer) getIntent().getSerializableExtra(
//			MainActivity.SER_KEY);
//	sb.append("----From Serializable----").append("\n");
//	sb.append("Name:").append(personSer.getName()).append("\n");
//	sb.append("Age:").append(personSer.getAge()).append("\n");
//} else if (type.equals(MainActivity.PAR_TYPE)) {
//	// ��ȡParcelable����
//	PersonPar personPar = (PersonPar) getIntent().getParcelableExtra(
//			MainActivity.PAR_KEY);
//	sb.append("----From Parcelable----").append("\n");
//	sb.append("Name:").append(personPar.getName()).append("\n");
//	sb.append("Age:").append(personPar.getAge()).append("\n");
//}