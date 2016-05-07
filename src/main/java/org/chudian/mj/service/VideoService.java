package org.chudian.mj.service;

import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;

/**
 * Created by onglchen
 * on 15-3-13.
 */
public interface VideoService extends BaseService<Video>{
    public boolean transferVideo(String videoName, Video video);

    public boolean transferVideoWeb(String filePath, String videoName, Video video);

    public void queryAll(QueryBase queryBase);
}
