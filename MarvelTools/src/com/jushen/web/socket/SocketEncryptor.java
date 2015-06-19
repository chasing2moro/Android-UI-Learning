package com.jushen.web.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.http.util.ByteArrayBuffer;

import com.jushen.utils.log.LoggerUtils;

public class SocketEncryptor {
	public int LIMIT_KEY_LENGTH = 32;
	public int MSG_MAX_STC_PACK_LEN = 40000;
	
	public static int SizeOfInt = 4;
	public boolean IsSocketEncode = true;
	
	public String EncryptKey ;//abcdefghijklmnoqprstuvwxyz()_+|1";
	byte[] mEncryptKeyBuffer = null;
	ByteArrayInputStream ByteArrayInputStream_EncryptKeyBuffer = null;
	byte[] mBuffer0 = new byte[MSG_MAX_STC_PACK_LEN];
	
	int _Decode(int rand_key, byte [] dst, byte [] src)
	{			
		//ByteArray key = new ByteArray(EncryptKey.ToCharArray());
		//ByteArray _dst = new ByteArray(dst);
		//ByteArray _src = new ByteArray(src);
		ByteArrayInputStream ByteArrayInputStream_src = new ByteArrayInputStream(src);
		
		int srclen = src.length;
		int k = 0;
		int int_num = srclen - (srclen % /*sizeof(int)*/SizeOfInt);
		int i = 0;
		
		for(; i < (int)srclen;)
		{
			if(i < int_num)
			{
				int v = 0;
				int sv = 0;
				int kv = 0;
				

				//sv = BitConverter.ToInt32(src, i);
				sv = intFromByteArrayInputStream(ByteArrayInputStream_src, i);

				//kv = BitConverter.ToInt32(mEncryptKeyBuffer, k);
				kv = intFromByteArrayInputStream(ByteArrayInputStream_EncryptKeyBuffer, i);
				v = sv ^(kv^(rand_key+i));
				


			//	byte[] data = BitConverter.GetBytes(v);
				byte[] data = byteArrayFromInt(v);
				
			//	Array.Copy(data, 0, dst, i, data.length);
				System.arraycopy(data, 0, dst, i, data.length);
				/* C++ code.
				(*(int *)&dst[i]) = 
					(*(int *)&src[i]) 
					^ 
					((*(int *)&key[k]) ^ (rand_key + i));
				*/
				
				i += SizeOfInt;
			}
			else
			{
				byte v = 0;
				byte sv = 0;
				byte kv = 0;
				
				//_src.Get(out sv, i);
				sv = src[i];
				
				//key.Get(out kv, k);
				kv = mEncryptKeyBuffer[k];
				
				v = (byte)( sv ^(kv^(rand_key+i)) );
				
				//v = sv ^(kv^((byte)(rand_key+1)));
				
				//_dst.Write(v, i);
				dst[i] = v;
				
				/* C++ code.
				(*(char *)&dst[i]) = 
					(*(char *)&src[i]) 
					^ 
					((*(char *)&key[k]) ^ (rand_key + i));
				*/
				
				i ++;
			}
			k = i % LIMIT_KEY_LENGTH;
		}
		
		//_dst.CopyTo(dst);
		
		return i;
	}
	
	public int Encode(int pack_num, byte[] dst, byte[] src)
	{

		int ret_size;

		if (IsSocketEncode) {
			//clear
			for (int i = 0; i < mBuffer0.length; i++) {
				mBuffer0[i] = 0;
			}
			
			//Use the same method
			ret_size = (int)_Decode (pack_num, mBuffer0, src) + SizeOfInt;

			int _pack_key = pack_num ^ 5;
			byte[] array = byteArrayFromInt(_pack_key);
			//buf.putInt(_pack_key);
			System.arraycopy(array, 0, dst, 0, array.length);
			//buf.put (mBuffer0, 0, ret_size);
			System.arraycopy(mBuffer0, 0, dst, array.length, ret_size);
		}else{	
			ret_size = src.length;
			System.arraycopy(src, 0, dst, 0, ret_size);	
		}

		return ret_size;
	}
	
	public int Decode(byte[] dst, byte[] src)
	{
		//ByteArray src_buf = new ByteArray(src);
		ByteBuffer src_buf = ByteBuffer.wrap(src);
		byte[] srcTure = new byte[src.length - 4];
//#if Encode
		if (IsSocketEncode) {
			int rand_key = src_buf.getInt();
			src_buf.get (srcTure);
			return _Decode (rand_key, dst, srcTure);
		} else {
			//#else		
			//src_buf.CopyTo (dst);
			
			System.arraycopy(src, 0, dst, 0, src.length);
			return src.length;
		}
//#endif
		//const EncryptInfo *encinfo = static_cast<const EncryptInfo *>(src);
		
		//客户端发来的随机key是一个封包顺序号，但是做了简单加密，这里需要解密
		//int rand_key = encinfo->rand_key;
		//*outptr = s_buff_recv;

		//return _Decode(rand_key, s_buff_recv, encinfo->data, srclen - sizeof(encinfo->rand_key));
	}
	
	public void ResetKey()
	{
if (true)
		EncryptKey = "jili(a*nl&aoy%out#iaod&e2333!_-jj";
else
		EncryptKey = "abcdefghijklmnoqprstuvwxyz()_+|1";

		
		mEncryptKeyBuffer = EncryptKey.getBytes();//"UTF8"
		ByteArrayInputStream_EncryptKeyBuffer = new ByteArrayInputStream(mEncryptKeyBuffer);
	}
	
	public void SetKey(byte[] data)
	{
//		if (mEncryptKeyBuffer == null || mEncryptKeyBuffer.length != data.length)
//			mEncryptKeyBuffer = new byte[data.length];
//		else
//			System.Array.Clear(mEncryptKeyBuffer, 0, mEncryptKeyBuffer.length);
//		
//		Array.Copy(data, mEncryptKeyBuffer, data.length);
		mEncryptKeyBuffer = new byte[data.length];
		System.arraycopy(data, 0, mEncryptKeyBuffer, 0, data.length);
		ByteArrayInputStream_EncryptKeyBuffer = new ByteArrayInputStream(mEncryptKeyBuffer);
		//			string newKey = ("");
		//			for (int i=0; i<data.Length; ++i)
		//			{
		//				char c = (char)data[i];
		//				newKey += c;
		//			}
		//			EncryptKey = newKey;
		//			
		//			mEncryptKeyBuffer = System.Text.Encoding.UTF8.GetBytes(EncryptKey);
	}
	
	public static int intFromByteArray(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < 4; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	
	public static byte[] byteArrayFromInt(int vInt){
		ByteArrayOutputStream boutput = new ByteArrayOutputStream();
		DataOutputStream doutput = new DataOutputStream(boutput);
		try {
			doutput.writeInt(vInt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	byte[] data = BitConverter.GetBytes(v);
		byte[] data = boutput.toByteArray();
		return data;
	}
	
	public static int intFromByteArrayInputStream(ByteArrayInputStream vByteArrayInputStream, int vIndex) {
		byte[] intByteArray = new byte[SizeOfInt];
		if( vByteArrayInputStream.read(intByteArray, vIndex, SizeOfInt) > 0){
			return intFromByteArray(intByteArray);
		}else{
			LoggerUtils.e("read end");
			return -1;
		}
	}
}

