package com.jushen.web.socket;

public interface SocketEventListener {
    /**
     * Socket正在接收数据
     * */
    public void OnStreamRecive();
    /**
     * Socket接收数据完成
     * */
    public void OnStreamReciveFinish();
    /**
     * Socket有新的消息返回
     * */
    public void OnStreamComing(byte[] aStreamData);
    /**
     * Socket出现异常
     * */
    public void OnSocketPause();
    /**
     * Socket已修复,可用
     * */
    public void OnSocketAvaliable();
}
