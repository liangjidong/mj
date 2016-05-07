package org.chudian.mj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.PictureVideo;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.PictureVideoMapper;
import org.chudian.mj.service.PictureVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by onglchen
 * on 15-3-13.
 */
@Service
public class PictureVideoServiceImpl implements PictureVideoService {

    private Log logger = LogFactory.getLog(PictureVideoServiceImpl.class);

    @Autowired
    private PictureVideoMapper pictureVideoMapper;
    /**
     *
     * @param
     * @return 0 成功  1 失败
     */
    @Override
    public int add(PictureVideo video) {
        if (pictureVideoMapper.insert(video) > 0) {
            logger.debug("添加文件成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }


    /**
     *
     * @param
     * @return 0 成功  1 失败  7 不存在
     */
    @Override
    public int update(PictureVideo video) {
        PictureVideo f = pictureVideoMapper.selectByPrimaryKey(video.getId());
        if (f == null) {
            logger.warn("尝试更新文件，但是文件不存在");
            return Status.NOT_EXISTS;
        }
        if (pictureVideoMapper.updateByPrimaryKey(video) > 0) {
            logger.debug("更新文件成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    /**
     *
     * @param
     * @return 0 成功  1 失败  7 不存在
     */
    @Override
    public int delete(PictureVideo video) {
        PictureVideo f = pictureVideoMapper.selectByPrimaryKey(video.getId());
        if (f == null) {
            logger.warn("尝试删除文件，但是文件不存在");
            return Status.NOT_EXISTS;
        }
        if (pictureVideoMapper.deleteByPrimaryKey(video.getId()) > 0) {
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
    public PictureVideo get(int id) {
        PictureVideo video_db = pictureVideoMapper.selectByPrimaryKey(id);
        if (video_db == null) {
            logger.warn("文件 ID：" + " 不存在");
        } else {
            logger.debug("文件 ID：" + " 成功");
        }
        return video_db;
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
//        queryBase.setResults(pic);
//        queryBase.setTotalRow(pictureMapper.countFiles(queryBase));
    }
}