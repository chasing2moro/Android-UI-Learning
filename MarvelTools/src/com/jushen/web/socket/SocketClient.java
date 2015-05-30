package com.jushen.web.socket;

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import org.apache.http.util.ByteArrayBuffer;

import android.R.bool;
import android.util.Log;

import com.jushen.utils.log.LoggerUtils;
//http://www.2cto.com/kf/201405/301570.html
public class SocketClient {
	Selector mSelector = null;
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

	public boolean Connect(String site, int port) {
		if (mSocketEventListener != null) {
			mSocketEventListener.OnSocketPause();
		}

		boolean ret = false;
		try {
			mSelector = Selector.open();
			client = SocketChannel.open();
			client.socket().setSoTimeout(5000);
			isa = new InetSocketAddress(site, port);
			boolean isconnect = client.connect(isa);
			// 将客户端设定为异步
			client.configureBlocking(false);
			// 在轮讯对象中注册此客户端的读取事件(就是当服务器向此客户端发送数据的时候)
			client.register(mSelector, SelectionKey.OP_READ);
			
			long waittimes = 0;

			if (!isconnect) {
				while (!client.finishConnect()) {
					Thread.sleep(50);
					if (waittimes < 100) {
						waittimes++;
					} else {
						LoggerUtils.i("等待非阻塞连接建立失败");
						break;
					}
				}

				if(client.finishConnect()){
					LoggerUtils.i("等待非阻塞连接建立成功");
				}
			}else{
				LoggerUtils.i("非阻塞连接竟然马上建立成功了。(⊙﹏⊙)b");
			}
			

			Thread.sleep(500);
			// haverepaired();
			startListener();
			ret = true;
		} catch (Exception e) {
			LoggerUtils.e(" - Connect error", e != null ? e.toString() : "null");
			try {
				Thread.sleep(1000 * 10);
			} catch (Exception e1) {
				LoggerUtils.e(" - Connect error", e1 != null ? e1.toString() : "null");
			}
			ret = false;
		}
		return ret;
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
            		LoggerUtils.e("发送数据失败:" + e.toString());

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
							// 如果客户端连接没有打开就退出循环
							if (!client.isOpen())
								break;

							// 此方法为查询是否有事件发生如果没有就阻塞,有的话返回事件数量
							int eventcount = mSelector.select();

							// 如果没有事件返回循环
							if (eventcount > 0) {
								starttime = System.currentTimeMillis();
								LoggerUtils.iConsleOnly("eventcount > 0");
								// 遍例所有的事件
								for (SelectionKey key : mSelector.selectedKeys()) {
									
									// 删除本次事件
									mSelector.selectedKeys().remove(key);
									// 如果本事件的类型为read时,表示服务器向本客户端发送了数据
									if (key.isValid() && key.isReadable()) {
										if (mSocketEventListener != null) {
											mSocketEventListener.OnStreamRecive();
										}
										boolean readresult = ReceiveDataBuffer((SocketChannel) key.channel());
										
										if (mSocketEventListener != null) {
											mSocketEventListener.OnStreamReciveFinish();
										}

										if (readresult) {
											key.interestOps(SelectionKey.OP_READ);
											sleep(200);
										} else {
											throw new Exception();
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
		// n 有数据的时候返回读取到的字节数。
		// 0 没有数据并且没有达到流的末端时返回0。
		// -1 当达到流末端的时候返回-1。
		boolean ret = false;

		ByteArrayBuffer bab = new ByteArrayBuffer(8 * 1024);
		while (true) {
			try {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 1);
				readBuffer.clear();
				int readsize = aSocketChannel.read(readBuffer);

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
								LoggerUtils.i("块数据大小：" + blockdatasize);
								byte[] blockdata = new byte[blockdatasize];
								System.arraycopy(readdata, readdataoffset,
										blockdata, 0, blockdatasize);

								long starttime = System.currentTimeMillis();
								ReceiveData(blockdata);
								long endtime = System.currentTimeMillis();
								LoggerUtils.i("解析数据用时：" + (endtime - starttime)
										+ "ms");
								readdataoffset += blockdatasize;
								blockdata = null;
							} else if (blockdatasize < 10240) {// 小于10k，则属于正常包
								LoggerUtils.i("块数据大小:" + blockdatasize
										+ ",小于10k，说明数据不完整，继续获取。");
								// 将未解析数据存到临时buffer
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
							} else {// 异常包
								LoggerUtils.i("块数据错误大小：" + blockdatasize);
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
							// compressed bit 暂时不压缩
							byte compressed = aDataBlock[offset];
							offset += 1;

							if (compressed == 1) {// 解压缩
													// 跳过头4个字节，此处用于解压缩后的数据大小，暂时不需要
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
								LoggerUtils.i("解压缩数据用时："
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
							} else {// 数据未压缩
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
