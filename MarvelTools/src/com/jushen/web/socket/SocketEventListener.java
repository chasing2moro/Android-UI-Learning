package com.jushen.web.socket;

public interface SocketEventListener {
    /**
     * Socket���ڽ�������
     * */
    public void OnStreamRecive();
    /**
     * Socket�����������
     * */
    public void OnStreamReciveFinish();
    /**
     * Socket���µ���Ϣ����
     * */
    public void OnStreamComing(byte[] aStreamData);
    /**
     * Socket�����쳣
     * */
    public void OnSocketPause();
    /**
     * Socket���޸�,����
     * */
    public void OnSocketAvaliable();
}
