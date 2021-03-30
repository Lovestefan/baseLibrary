package com.sdsk.base.http.upload;

public class ProgressModel {
    //当前读取字节长度
    private int progress;
    //是否读取完成
    private boolean done;
    public ProgressModel(int progress, boolean done) {
        this.progress = progress;
        this.done = done;
    }
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    @Override
    public String toString() {
        return "ProgressModel{" +
                "progress=" + progress +
                ", done=" + done +
                '}';
    }
}
