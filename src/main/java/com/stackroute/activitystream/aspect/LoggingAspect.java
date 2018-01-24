package com.stackroute.activitystream.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/*
 * Code review comments: Why are you using S.O.P statements? You should use log.
 */
@Component
@Aspect
/*
 * Code review comments: As you have already created a bean in application context, @component is not required
 */
//@Component
public class LoggingAspect {
	/*
	 * Code review comments: Unnecessary code
	 */	
	private static Logger logger = LogManager.getLogger(LoggingAspect.class);


	public LoggingAspect() {
		System.out.println("I m in LoggingAspect constructor");
	}
	@Before(" execution(* com.stackroute.activitystream.daoimpl.UserDAOImpl.validate(..))")
	public void loggingDAOAdvice(){
	    logger.info("@Before advice calling validate function info log message");
	    logger.error("@Before advice calling validate function error log message");
		System.out.println("@Before advice calling validate function error log message" );
	}
	@Pointcut("within(@com.stackroute.activitystream.controller.UserAuthController)")
	public void controller() {
	}

	@Before("execution(* com.stackroute.activitystream.controller.UserAuthController.authenticateUser(..))")
	public void loggingAdvice() {
		System.out.println("login method called");	
		logger.info("info---@Before authenticateUser() method of UserAuthController");
		logger.error("error---@Before authenticateUser() method of UserAuthController");
	}
	@Before("execution(* com.stackroute.activitystream.controller.UserAuthController.logout(..))")
	public void loggingControllerAdvice()
	{
		logger.info("@Before info log message for logout method of controller");
		logger.debug("@Before info log message for logout method of controller");
		logger.error("@Before info log message for logout method of controller");
	}

	/*
	 * Code review comments: this method is causing circlular reference error due to the pointcut being used.
	 * Consider replacing it to a more specific target method name.  
	 */ /*
	@After("execution(public * *(..))")
	public void loggingAdvice2() {
		System.out.println("after methods calling in UserAuthController");
	}*/
	 @Before("execution(* com.stackroute.activitystream.controller.UserAuthController+.*(..))")
     public void advice(JoinPoint joinPoint) {
         // advise FooService methods as appropriate
		 System.out.println("+++++I am in LoggingAspect++++++++++++++++++++++++++++");
		 System.out.println(joinPoint.getClass().getTypeName());
		 logger.info("JoinPoint--"+joinPoint.getClass().getTypeName());
     }
}