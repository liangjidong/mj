package org.chudian.mj.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.Response;
import org.chudian.mj.mapper.UserMapper;
import org.chudian.mj.utils.ActionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Wang Yu on 7/25/2015.
 */
@Aspect
public class ActionIntercepters {
	private Log logger = LogFactory.getLog(ActionIntercepters.class);
	
	/*
	 * User class
	 * role:
	 *      1:normal user
	 *      2:manager
	 */
	
	@Resource(name="invalidOperationResponse")  //在Spring-MVC文件中有注解
    Response invalidResponse;
	
	@Autowired
	UserMapper userMapper;
	
	@Around("@annotation(org.chudian.mj.annotation.UserLoginAuthorized)")
	public Object checkLoginedUserAuthorized(ProceedingJoinPoint point) throws Throwable{
		try{
			HttpServletRequest request=(HttpServletRequest) point.getArgs()[0];
			//HttpServletResponse response=(HttpServletResponse) point.getArgs()[1];
			User obj=ActionUtil.getCurrentUser(request);
			if(obj!=null&&obj instanceof User){
				System.out.println("userName ===" + obj.getName());
				return point.proceed();
				/*User u=(User) obj;
				User u_db=userMapper.selectByPrimaryKey(u.getId());

				if(u_db.getRole()==1){
					return point.proceed();
				}*/
			}
			System.out.println("Redirect ====");
			//response.sendRedirect("/mj/login.html");
			  //return  new ModelAndView("login");

		}catch(Exception e){
			e.printStackTrace();
		}
		return invalidResponse;
	}
	@Around("@annotation(org.chudian.mj.annotation.ManagerLoginAuthorized)")
	public Object checkLoginedManagerAuthorized(ProceedingJoinPoint point) throws Throwable{
		try{
			HttpServletRequest request=(HttpServletRequest) point.getArgs()[0];
			Object obj=ActionUtil.getCurrentUser(request); 
			if(obj!=null&&obj instanceof User){
				User u=(User) obj; 
				User u_db=userMapper.selectByPrimaryKey(u.getId());
				if(u_db.getRole()==2){
					return point.proceed();
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return invalidResponse;		
	}
	@Around("@annotation(org.chudian.mj.annotation.UserSelfAndManagerAuthorized)")
	public Object checkLoginedUserSelfAndManagerAuthorized(ProceedingJoinPoint point) throws Throwable{
		try{
			HttpServletRequest request=(HttpServletRequest) point.getArgs()[0];
			System.out.println("看看呢");
			System.out.println(request.getParameterValues("name"));
			System.out.println(request.getParameterMap().toString());
			return point.proceed();
		}catch(Exception e){
			e.printStackTrace();
		} 
		return invalidResponse;		
	}
	
}
