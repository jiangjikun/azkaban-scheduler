package com.spiderdt.common.scheduler.errorhandling;

import com.spiderdt.common.scheduler.filters.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;


@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static Logger log = LoggerFactory.getLogger(GenericExceptionMapper.class);
	public Response toResponse(Throwable ex) {
		
		ErrorMessage errorMessage = new ErrorMessage();		
		setHttpStatus(ex, errorMessage);
		errorMessage.setCode(AppConstants.GENERIC_APP_ERROR_CODE);
		errorMessage.setMessage(ex.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		//ex.printStackTrace(new PrintWriter(errorStackTrace));
		log.error(new PrintWriter(errorStackTrace).toString());
		//errorMessage.setDeveloperMessage(errorStackTrace.toString());

		return Response.status(errorMessage.getStatus())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();	
	}

	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		if(ex instanceof WebApplicationException ) { //NICE way to combine both of methods, say it in the blog 
			errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		} else {
			errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
		}
	}
}

