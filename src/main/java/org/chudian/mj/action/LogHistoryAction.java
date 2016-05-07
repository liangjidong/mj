package org.chudian.mj.action;

import javax.servlet.http.HttpServletRequest;

import org.chudian.mj.annotation.ManagerLoginAuthorized;
import org.chudian.mj.annotation.UserLoginAuthorized;
import org.chudian.mj.bean.LogHistory;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Response;
import org.chudian.mj.common.Status;
import org.chudian.mj.service.LogHistoryService;
import org.chudian.mj.service.UserService;
import org.chudian.mj.utils.ActionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogHistoryAction {
	@Autowired
	private LogHistoryService logHistoryService;
	@Autowired
	private UserService userService;

	public int addLogHistory(LogHistory logHistory) {
		if (logHistoryService.add(logHistory) == 0) {
			return Status.SUCCESS;
		}
		return Status.ERROR;
	}

	@ResponseBody
	@UserLoginAuthorized
	@RequestMapping(value = "/api/loghistory/{id}", method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request, @PathVariable int id) {
		int status;
		LogHistory logHistory = new LogHistory();
		logHistory.setId(id);
		logHistory = logHistoryService.get(id);
		System.out.println("蛤蛤");
		System.out.println(logHistory.getUserId());
		int session_userId = ActionUtil.getCurrentUser(request).getId();
		if (logHistory.getUserId().equals(session_userId)) {
			status = logHistoryService.delete(logHistory);
		} else {
			status = Status.INVALID_OPERATION;
		}
		return new Response(status);
	}

	@ResponseBody
	@RequestMapping(value = "/api/loghistory/query/{userId}", method = RequestMethod.GET)
	public Object query(
			HttpServletRequest request,
			QueryBase query,
			@RequestParam(value = "currentPage", required = false) String currentPage,
			@PathVariable int userId) { 
		int status;
		// get the User object from session
		Object obj = ActionUtil.getCurrentUser(request);
		User user_session = (User) obj;
		// get the User object in db related to the user in Session
		User u_db = userService.get(user_session.getId());
		if (user_session.getId() == userId || u_db.getRole().equals(2)) {
			String message;
			if (query == null) {
				query = new QueryBase();
			}
			if (currentPage != null) {
				long cuPage = Long.parseLong(currentPage);
				query.setCurrentPage(cuPage);
			}
			query.addParameter("userId", userId);
			logHistoryService.query(query);
			return new Response(Status.SUCCESS, query.getResults());
		} else {
			status = Status.INVALID_OPERATION;
			return new Response(status);
		} 
	}
}
