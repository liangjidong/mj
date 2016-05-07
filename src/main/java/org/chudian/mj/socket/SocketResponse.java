package org.chudian.mj.socket;

/**
 * Created by onglchen
 * on 15-3-30.
 */
public class SocketResponse implements java.io.Serializable  {
    private static final long serialVersionUID = 12322l;

    protected int status;

    protected String point;

    public SocketResponse(){

    }

    public SocketResponse(int status, String point, String videourl) {
        this.status = status;
        this.point = point;
        this.videourl = videourl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    protected String videourl;
}
