package org.chudian.mj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.MjproductMapper;
import org.chudian.mj.mapper.VideoMapper;
import org.chudian.mj.service.VideoService;
import org.chudian.mj.utils.FileUtil;
import org.chudian.mj.utils.MediocreExecJavac2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by onglchen
 * on 15-3-13.
 */
@Service
public class VideoServiceImpl implements VideoService {

    private Log logger = LogFactory.getLog(VideoServiceImpl.class);

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private MjproductMapper mjproductMapper;

    private String VIDEO_UPLOAD_DIR_SERVICE = "/home/onglchen/proenv/userlib/videos";
    private String VIDEO_UPLOAD_DIR = "/usr/local/nginx/html";
    private String TOMCAT_VIDEO_PATH = "videos";
    /**
     *
     * @param video
     * @return 0 成功  1 失败
     */
    @Override
    public int add(Video video) {
        if (videoMapper.insert(video) > 0) {
            logger.debug("添加文件成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }


    /**
     *
     * @param video
     * @return 0 成功  1 失败  7 不存在
     */
    @Override
    public int update(Video video) {
        Video f = videoMapper.selectByPrimaryKey(video.getId());
        if (f == null) {
            logger.warn("尝试更新文件，但是文件不存在");
            return Status.NOT_EXISTS;
        }
        if (videoMapper.updateByPrimaryKeySelective(video) > 0) {
            logger.debug("更新文件成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    /**
     *
     * @param video
     * @return 0 成功  1 失败  7 不存在  3  不能删除（存在关联产品）
     */
    @Override
    public int delete(Video video) {
        Video f = videoMapper.selectByPrimaryKey(video.getId());
        if (f == null) {
            logger.warn("尝试删除文件，但是文件不存在");
            return Status.NOT_EXISTS;
        }
        if(mjproductMapper.isExistsByVideoId(video.getId())){
            return Status.NO_DELETE;
        }
        if (videoMapper.deleteByPrimaryKey(video.getId()) > 0) {
            //FileUtil.deleteFile(MjproductServiceImpl.VIDEO_DIR+"/"+video.getName());
            logger.debug("删除文件成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    /**
     *
     * @param id
     * @return file
     */
    @Override
    public Video get(int id) {
        Video video_db = videoMapper.selectByPrimaryKey(id);
        if (video_db == null) {
            logger.warn("文件 ID：" + " 不存在");
        } else {
            logger.debug("文件 ID：" + " 成功");
        }
        return video_db;
    }

    public boolean transferVideoWeb(String filePath, String videoName, Video video){
        MediocreExecJavac2 me = new MediocreExecJavac2();
        String reName = FileUtil.rename(videoName);
        String videoName_new = FileUtil.getFileName(reName) + ".mp4" ;
//        String infile = UPLOAD_DIR + "/" + videoName;
        String infile = filePath;
        String oufileDir = VIDEO_UPLOAD_DIR_SERVICE ;
        video.setOriginUrl(filePath);
        video.setName(videoName_new);
        video.setFinalUrl( TOMCAT_VIDEO_PATH + "/" + videoName_new);
        return me.excuteTransfer(infile,oufileDir, videoName_new, "mp4");
    }

    public boolean transferVideo(String videoName, Video video){
        MediocreExecJavac2 me = new MediocreExecJavac2();
        String reName = FileUtil.rename(videoName);
        String videoName_new = FileUtil.getFileName(reName) + ".mp4" ;
        String infile = VIDEO_UPLOAD_DIR + "/" + videoName;
        String oufileDir = VIDEO_UPLOAD_DIR_SERVICE ;
        video.setName(videoName_new);
        video.setFinalUrl( TOMCAT_VIDEO_PATH + "/" + videoName_new);
       return me.excuteTransfer(infile,oufileDir, videoName_new, "mp4");
    }


    /**
     *
     * @param queryBase
     */
    @Override
    public void query(QueryBase queryBase) {
        if (logger.isDebugEnabled()) {
            logger.debug("根据参数 " + queryBase.getParameters() + "  查询文件");
        }
        queryBase.setResults(videoMapper.queryVideos(queryBase));
        queryBase.setTotalRow(videoMapper.countVideos(queryBase));
    }

    /**
     *
     * @param queryBase
     */
    @Override
    public void queryAll(QueryBase queryBase) {
        if (logger.isDebugEnabled()) {
            logger.debug("根据参数 " + queryBase.getParameters() + "  查询文件");
        }
        queryBase.setResults(videoMapper.queryVideosAll(queryBase));
        queryBase.setTotalRow(videoMapper.countVideos(queryBase));
    }
}