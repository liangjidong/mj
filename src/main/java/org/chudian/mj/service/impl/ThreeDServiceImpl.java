package org.chudian.mj.service.impl;

import org.chudian.mj.bean.ThreeD;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.ThreeDMapper;
import org.chudian.mj.service.ThreeDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ThreeDServiceImpl implements ThreeDService{

	@Autowired
	ThreeDMapper threeDMapper;
	@Override
	public int add(ThreeD obj) {
		if(threeDMapper.insertSelective(obj)>0){
			return Status.SUCCESS;
		}else{
			return Status.ERROR;
		}
	}

	@Override
	public int update(ThreeD obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(ThreeD obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ThreeD get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void query(QueryBase queryBase) {
		// TODO Auto-generated method stub
		
	}

}
