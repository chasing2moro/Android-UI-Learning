package com.jushen.web.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import org.apache.http.util.ByteArrayBuffer;

import android.R.bool;
import android.R.xml;
import android.util.Log;

import com.jushen.utils.log.LoggerUtils;
//http://www.2cto.com/kf/201405/301570.html
public class SocketClient {
	private static SocketClient _instance;
	public static SocketClient singleton() {
		if(_instance == null){
			_instance = new SocketClient();
		}
		return _instance;
	}
	Selector mSelector = null;
	
	//store byte stream from socket
	ByteBuffer sendBuffer = null;
	SocketChannel client = null;
	InetSocketAddress isa = null;
	SocketEventListener mSocketEventListener = null;

	Thread mReadThread = null;
	boolean mRunRead;
	long mSendMsgTime;
	
	public SocketClient() {
		sendBuffer = ByteBuffer.allocate(1024);
	}

	public void ConnectAsync(String site, int port) {
		final String aSite = site;
		final int aPort = port;
		Thread connectThread = new Thread(){
			@Override
			public void run() {
				Connect(aSite, aPort);
			}
		};
		
		connectThread.start();
	}
	public boolean Connect(String site, int port) {
		site = "192.168.16.40";
		port = 6101;
		if (mSocketEventListener != null) {
			mSocketEventListener.OnSocketPause();
		}

		boolean ret = false;
		try {
			mSelector = Selector.open();
			client = SocketChannel.open();
			client.socket().setSoTimeout(5000);
			LoggerUtils.i("Connect:" + site + " " + port);
			isa = new InetSocketAddress(site, port);
			
			//work as Async
			client.configureBlocking(false);
			
			//connect
			boolean isconnect = client.connect(isa);
			
			client.register(mSelector, SelectionKey.OP_READ);
			
			long waittimes = 0;

			if (!isconnect) {
				while (!client.finishConnect()) {
					Thread.sleep(50);
					if (waittimes < 100) {
						waittimes++;
					} else {
						LoggerUtils.i("Connect time out :" + (50 * 100) + " ms");
						break;
					}
				}

				if(client.finishConnect()){
					LoggerUtils.i("connection is finished later");
					ret = true;
				}
			}else{
				LoggerUtils.i("connection is finished immediately");
				ret = true;
			}
			

			Thread.sleep(500);
			// haverepaired();
			//startListener();
			
		} catch (Exception e) {
			LoggerUtils.e(" - Connect error", e != null ? e.toString() : "null");
			try {
				Thread.sleep(1000 * 10);
			} catch (Exception e1) {
				LoggerUtils.e(" - Connect error", e1 != null ? e1.toString() : "null");
			}
		}
		return ret;
	}

	public boolean  Send1stSocketMsg(byte[] aMessage) throws IOException{
		startListener();
		return SendSocketMsg(aMessage);
	}
	
	public boolean SendSocketMsg(byte[] aMessage) throws IOException
    {
            boolean ret = false;
            try
            {
            		LoggerUtils.i("Client send :" + String.valueOf(aMessage));
                    sendBuffer.clear();
                    sendBuffer = ByteBuffer.wrap(aMessage);
                    int sendsize = client.write(sendBuffer);
                    sendBuffer.flip();
                    sendBuffer.clear();
                    mSendMsgTime = System.currentTimeMillis();
                    ret = true;
            }
            catch (Exception e)
            {
            		LoggerUtils.e("��������ʧ��:" + e.toString());

                    if (mSocketEventListener != null)
                    {
                            mSocketEventListener.OnSocketPause();
                    }
//                    crash();
            }
            return ret;
    }
	
	long starttime;

	private void startListener() {
		if (mReadThread == null || mReadThread.isInterrupted()) {
			mReadThread = null;
			mReadThread = new Thread() {
				@Override
				public void run() {
					while (!this.isInterrupted() && mRunRead) {
						try {
							LoggerUtils.iConsleOnly("check client if opend");
							if (!client.isOpen())
								break;

							int eventcount = mSelector.select();
	
							if (eventcount > 0) {
								starttime = System.currentTimeMillis();
								LoggerUtils.iConsleOnly("eventcount :" + eventcount);

								for (SelectionKey key : mSelector.selectedKeys()) {
									LoggerUtils.iConsleOnly("eventType :" + key.interestOps());
									mSelector.selectedKeys().remove(key);
			
									if (key.isValid() && key.isReadable()) {
										if (mSocketEventListener != null) {
											mSocketEventListener.OnStreamRecive();
										}
										//key.channel() is the same to client
										boolean readresult = ReceiveDataBuffer((SocketChannel) key.channel());
										
										if (mSocketEventListener != null) {
											mSocketEventListener.OnStreamReciveFinish();
										}

										if (readresult) {
											key.interestOps(SelectionKey.OP_READ);
											sleep(200);
										} else {
											//throw new Exception();
											//this.interrupt();
										}
									}
									key = null;
								}
								mSelector.selectedKeys().clear();
							}
						} catch (Exception e) {
							mRunRead = false;
							mReadThread = null;
							if (e instanceof InterruptedException) {
								LoggerUtils.i("error startListener:" + e.toString());
							} else {
								break;
							}
						}
					}
				}
			};

			mReadThread.setName("Listener, " + System.currentTimeMillis());
			mRunRead = true;
			mReadThread.start();
		}
	}

	private boolean ReceiveDataBuffer(SocketChannel aSocketChannel) {
		// n �����ݵ�ʱ�򷵻ض�ȡ�����ֽ�����
		// 0 û�����ݲ���û�дﵽ����ĩ��ʱ����0��
		// -1 ���ﵽ��ĩ�˵�ʱ�򷵻�-1��
		boolean ret = false;

		ByteArrayBuffer bab = new ByteArrayBuffer(8 * 1024);
		while (true) {
			try {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 1);
				
				
				readBuffer.clear();
				
//				n 有数据的时候返回读取到的字节数。
//				0 没有数据并且没有达到流的末端时返回0。
//				-1 当达到流末端的时候返回-1。
				int readsize = aSocketChannel.read(readBuffer);
				
				//i.读取buffer的chanel  
//				InputStream is = aSocketChannel.socket().getInputStream();  
//				ReadableByteChannel readCh = Channels.newChannel(is);  
//				int readsize = readCh.read(readBuffer);
				
				if (readsize > 0) {
					LoggerUtils.i("readsize:" + readsize);
					byte[] readbytes = readBuffer.array();
					bab.append(readbytes, 0, readsize);
					readBuffer.clear();
					readBuffer.flip();
					ret = true;
				} else if (readsize == 0) {
					int buffersize = bab.length();
					byte[] readdata = bab.buffer();
					int readdataoffset = 0;
					boolean parsedata = true;
					LoggerUtils.i("read finish");
/*
					while (readdataoffset < buffersize && parsedata) {
						byte datatype = readdata[readdataoffset];
						if (false
								// datatype == PushUtils.PACKAGETYPE_HEARTBEAT
								 // || datatype ==
								 // PushUtils.PACKAGETYPE_HEARTBEAR_NODATA
								 ) {
							byte[] blockdata = new byte[] { datatype };
							ReceiveData(blockdata);
							readdataoffset += 1;
							blockdata = null;
						} else {
							byte[] blocklength = new byte[4];
							System.arraycopy(readdata, readdataoffset + 5,
									blocklength, 0, 4);
							int blocksize = 3;// CommonClass.bytes2int(CommonClass.LitteEndian_BigEndian(blocklength));
							blocklength = null;

							int blockdatasize = 5 + blocksize + 4;

							if (blockdatasize <= buffersize) {
								LoggerUtils.i("�����ݴ�С��" + blockdatasize);
								byte[] blockdata = new byte[blockdatasize];
								System.arraycopy(readdata, readdataoffset,
										blockdata, 0, blockdatasize);

								long starttime = System.currentTimeMillis();
								ReceiveData(blockdata);
								long endtime = System.currentTimeMillis();
								LoggerUtils.i("����������ʱ��" + (endtime - starttime)
										+ "ms");
								readdataoffset += blockdatasize;
								blockdata = null;
							} else if (blockdatasize < 10240) {// С��10k��������������
								LoggerUtils.i("�����ݴ�С:" + blockdatasize
										+ ",С��10k��˵�����ݲ�������������ȡ��");
								// ��δ�������ݴ浽��ʱbuffer
								int IncompleteSize = buffersize
										- readdataoffset;
								if (IncompleteSize > 0) {
									byte[] Incompletedata = new byte[IncompleteSize];
									System.arraycopy(readdata, readdataoffset,
											Incompletedata, 0, IncompleteSize);
									bab.clear();
									bab.append(Incompletedata, 0,
											IncompleteSize);
									parsedata = false;
									Incompletedata = null;
								}
							} else {// �쳣��
								LoggerUtils.i("�����ݴ����С��" + blockdatasize);
								LoggerUtils.i("blockdatasize error:"
										+ blockdatasize);
								ret = true;
								break;
							}
						}
					}
*/
					if (parsedata) {
						ret = true;
						break;
					}
				} else if (readsize == -1) {
					LoggerUtils.i("read encouter error, due to readsize:" + readsize);
					ret = false;
					break;
				} else {
					LoggerUtils.i("read unhandle , size:" + readsize);
					ret = true;
					break;
				}
			} catch (IOException e) {
				LoggerUtils.e("aSocketChannel IOException=>" + e.toString());
				ret = false;
				break;
			}
		}
		bab.clear();
		bab = null;
		return ret;
	}

	private void ReceiveData(byte[] aDataBlock) {
		try {
			LoggerUtils.i("ReceiveData:" + mSendMsgTime);
			if (mSendMsgTime != 0) {
				mSendMsgTime = 0;
			}

			byte[] ret = null;

			int offset = 0;

			byte datatype = aDataBlock[offset];
			offset += 1;

			if (datatype != -1) {
				if (datatype == PushUtils.PACKAGETYPE_HEARTBEAT) {
					ret = new byte[] { datatype };
				} else if (datatype == PushUtils.PACKAGETYPE_HEARTBEAR_NODATA) {
					ret = new byte[] { datatype };
				} else if (datatype == PushUtils.PACKAGETYPE_NORMAL
						|| datatype == PushUtils.PACKAGETYPE_HEARTBEAR_HAVEDATA) {
					byte[] databytelength = new byte[4];
					System.arraycopy(aDataBlock, offset, databytelength, 0, 4);
					offset += 4;
					int header = 3;// CommonClass.bytes2int(CommonClass.LitteEndian_BigEndian(databytelength));
					databytelength = null;

					if (header == PushUtils.PACKAGEHEADER) {
						byte[] datalengthbyte = new byte[4];
						System.arraycopy(aDataBlock, offset, datalengthbyte, 0,
								4);
						offset += 4;

						int datalength = 3;// CommonClass.bytes2int(CommonClass.LitteEndian_BigEndian(datalengthbyte));
						datalengthbyte = null;

						if (datalength > 4) {
							// compressed bit ��ʱ��ѹ��
							byte compressed = aDataBlock[offset];
							offset += 1;

							if (compressed == 1) {// ��ѹ��
													// ����ͷ4���ֽڣ��˴����ڽ�ѹ��������ݴ�С����ʱ����Ҫ
								offset += 4;
								int contentlength = datalength - 1 - 4;
								byte[] datacontentbyte = new byte[contentlength];
								System.arraycopy(aDataBlock, offset,
										datacontentbyte, 0, contentlength);
								offset += contentlength;

								byte[] compressdata = new byte[contentlength - 4];
								System.arraycopy(datacontentbyte, 0,
										compressdata, 0, contentlength - 4);

								long starttime = System.currentTimeMillis();
								byte[] decompressdatacontentbyte = null;// CommonClass.decompress(compressdata);
								long endtime = System.currentTimeMillis();
								LoggerUtils.i("��ѹ��������ʱ��"
										+ (endtime - starttime) + "ms");
								int decompressdatacontentbytelength = decompressdatacontentbyte.length;
								compressdata = null;
								int footer = PushUtils.getInt(datacontentbyte,
										contentlength - 4);

								if (footer == PushUtils.PACKAGEFOOTER) {
									ret = new byte[decompressdatacontentbytelength + 1];
									ret[0] = datatype;
									System.arraycopy(decompressdatacontentbyte,
											0, ret, 1,
											decompressdatacontentbytelength);
									datacontentbyte = null;
									decompressdatacontentbyte = null;
								}
							} else {// ����δѹ��
								int contentlength = datalength - 1;
								byte[] datacontentbyte = new byte[contentlength];
								System.arraycopy(aDataBlock, offset,
										datacontentbyte, 0, contentlength);
								offset += contentlength;

								int footer = PushUtils.getInt(datacontentbyte,
										contentlength - 4);

								if (footer == PushUtils.PACKAGEFOOTER) {
									ret = new byte[contentlength + 1 - 4];
									ret[0] = datatype;
									System.arraycopy(datacontentbyte, 0, ret,
											1, contentlength - 4);
									datacontentbyte = null;
								}
							}
						}
					}
				}

				if (mSocketEventListener != null) {
					mSocketEventListener.OnStreamComing(ret);
				}
			}
		} catch (Exception e) {
			LoggerUtils.i(" - ReceiveData error", e.toString());
		}
	}
//	public void closeSocket()
//    {
//            mRunRead = false;
//            if (mReadThread != null)
//            {
//                    if (!mReadThread.isInterrupted())
//                    {
//                            mReadThread.interrupt();
//                            mReadThread = null;
//                    }
//            }
//
//            if (mSelector != null && mSelector.isOpen())
//            {
//                    try
//                    {
//                            mSelector.close();
//                    }
//                    catch (IOException e)
//                    {
//                            MyLineLog.redLog(TAG + " - closeSocket error", e.toString());
//                    }
//                    mSelector = null;
//            }
//             
//            if (client != null)
//            {
//                    try
//                    {
//                            client.close();
//                            client = null;
//                    }
//                    catch (IOException e)
//                    {
//                            MyLineLog.redLog(TAG + " - closeSocket2 error", e.toString());
//                    }
//            }
//
//            System.gc();
//    }
}
