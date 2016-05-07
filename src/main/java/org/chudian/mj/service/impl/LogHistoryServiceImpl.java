package org.chudian.mj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.LogHistory;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.LogHistoryMapper;
import org.chudian.mj.service.LogHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogHistoryServiceImpl implements LogHistoryService{

	private Log logger = LogFactory.getLog(MjproductServiceImpl.class);
	
	@Autowired //省去了getter 和setter
	private LogHistoryMapper logHistoryMapper;
	
	@Override
	public int add(LogHistory logHistory) {
		if (logHistoryMapper.insertSelective(logHistory) > 0) {
			logger.debug("记录id为"+logHistory.getUserId()+"用户的行为成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	@Override
	public int update(LogHistory obj) {
		// TODO Auto-generated method stub
		return Status.LOCK;
	}

	@Override
	public int delete(LogHistory logHistory) {
		if (logHistoryMapper.deleteByPrimaryKey(logHistory.getId()) > 0) {
			logger.debug("删除id为"+logHistory.getUserId()+"用户的行为成功");
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	@Override
	public LogHistory get(int id) {
		LogHistory logHistory = logHistoryMapper.selectByPrimaryKey(id);
	        if (logHistory == null) {
	            logger.warn("查询记录ID: " + id + " 不存在");
	        } else {
	            logger.debug("查询记录 ID: " + id + "成功");
	        }
	        return logHistory;
	}

	@Override
	public void query(QueryBase queryBase) {
		if (logger.isDebugEnabled()) { 
			logger.debug("根据参数 " + queryBase.getParameters() + "  查询用户操作记录");
		}
		queryBase.setResults(logHistoryMapper.queryByUserId(queryBase));
		queryBase.setTotalRow(logHistoryMapper.countLogHistory(queryBase));
	}

}
