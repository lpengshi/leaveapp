package sg.edu.iss.leave.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView handleForeignKeyException(DataIntegrityViolationException ex)
	{
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error");
	    modelAndView.addObject("message", "Error: You cannot delete records that are being referenced in other records");
	   
	    //modelAndView.addObject("message", ex.getMessage());
	    
	    LOG.error(ex.getMessage());
	    
	    return modelAndView;
	}
	
	@ExceptionHandler(DeleteAdminException.class)
	public ModelAndView deleteAdminException(DeleteAdminException ex)
	{
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error");
	    modelAndView.addObject("message", "Error: " + ex.getMessage());
	    
	    LOG.error(ex.getMessage());
	    
	    return modelAndView;
	}
	
	
	@ExceptionHandler({NullPointerException.class, ArrayIndexOutOfBoundsException.class, IOException.class})
	public ModelAndView handleNullException(NullPointerException ex)
	{
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error");
	    modelAndView.addObject("message", "Error: Your session has expired. Please login again.");
	    
	    //modelAndView.addObject("message", ex.getMessage());
	    
	    LOG.error(ex.getMessage());
	    
	    return modelAndView;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleOtherException(Exception ex)
	{
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error");
	    modelAndView.addObject("message", "Please contact support@laps.sg for assistance.");
	    
	    //modelAndView.addObject("message", ex.getMessage());
	    
	    LOG.error(ex.getMessage());
	    
	    return modelAndView;
	}
	
}
