package com.bzh.activiti.interceptor;

import com.bzh.activiti.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

	public static Logger log = Logger.getLogger(AuthInterceptor.class);
	
	


	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		//controller方法调用之前
		
		RedisTemplate<String, String> redisTemplate=(RedisTemplate<String, String>)
				SpringUtil.getBean("redisTemplate");
		if(request.getMethod().equals("OPTIONS")){
			return true;
		}
		log.info("拦截请求:"+request.getRequestURI());
		log.info("请求IP:"+request.getRemoteUser());
		String token=request.getHeader("Access-Token");
		log.info(token);
		if(!StringUtils.isEmpty(token)){
			boolean isLogin=redisTemplate.hasKey(token);
			log.info(isLogin);
			if(isLogin){
				log.info("用户已经登录");
				return true;

			}

		}
		response.setStatus(401);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("{status:401,message:'token失效'}");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;



	}

	@Override
	public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		//请求处理之后进行调用，但是在视图被渲染之前，即Controller方法调用之后
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行，主要是用于进行资源清理工作
		
	}
	


}
