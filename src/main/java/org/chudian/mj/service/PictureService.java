package org.chudian.mj.service;

import org.chudian.mj.bean.Picture;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by onglchen
 * on 15-3-13.
 */
public interface PictureService  extends BaseService<Picture>{

    public int copyPictureToTrain(String fileName ,Picture picture);
    public Picture getByName(String  fileName );
    @Transactional(isolation= Isolation.READ_COMMITTED,rollbackFor=Throwable.class)
    public int updateonly(Picture picture);
    public int moveToPictureToTrain(String srcFile, String fileName ,Picture picture);
    public int copyPictureToTrainWeb(String srcFile, String fileName ,Picture picture);

    public HashMap getVidioByPicName(String fileName, String path);

    public HashMap queryARInfo(String fileName, String path);

    public int updateOnly(Picture picture);

    public boolean isPicturExist();
}
