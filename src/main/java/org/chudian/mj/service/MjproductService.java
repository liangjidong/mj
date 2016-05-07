package org.chudian.mj.service;

import org.chudian.mj.bean.Mjproduct;

import java.util.HashMap;

/**
 * Created by onglchen
 * on 15-3-11.
 */
public interface MjproductService extends BaseService<Mjproduct> {
	public void updateTrainStatus();
	
	public void updateclicktimes(Integer id);

	public HashMap queryARInfo(String fileName, String path, int userId);

	public HashMap queryARInfo(String fileName, String path);

}