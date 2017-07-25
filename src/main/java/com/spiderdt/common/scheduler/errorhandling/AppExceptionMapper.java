package com.spiderdt.common.scheduler.errorhandling;

import com.spiderdt.common.scheduler.filters.AppConstants;

import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {

	public Response toResponse(Exception ex) {
		Response.ResponseBuilder rb = null;
		if( ex instanceof ServletException){
	    	ErrorMessage em = new ErrorMessage();
	    	em.setStatus(AppConstants.GENERIC_APP_ERROR_CODE);
	    	em.setMessage(ex.getMessage());
			rb = Response.status(AppConstants.GENERIC_APP_EXCEPTION_CODE)
					.entity(em)
					.type(MediaType.APPLICATION_JSON);

		}else if ( ex instanceof  AppException){
			AppException exp = (AppException) ex;
			rb = Response.status(AppConstants.GENERIC_APP_EXCEPTION_CODE)
					.entity(new ErrorMessage(exp))
					.type(MediaType.APPLICATION_JSON);
		}
		return rb.build();
	}

}
