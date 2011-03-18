/**
 * 
 */
package com.trendrr.strest.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.trendrr.oss.DynMap;
import com.trendrr.strest.annotations.Strest;
import com.trendrr.strest.server.StrestController;


/**
 * @author Dustin Norlander
 * @created Jan 27, 2011
 * 
 */
@Strest(
		route = "/hello/:param"
)
public class HelloWorld extends StrestController {

	protected Log log = LogFactory.getLog(HelloWorld.class);

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.StrestController#action(com.trendrr.oss.DynMap)
	 */
	@Override
	public void handleGET(DynMap params) throws Exception {
		log.info("Hello1");
		this.setResponseBytes(
				("Hello " + params.get(String.class, "param", "World").toUpperCase() + "!").getBytes("utf8"), 
				"text/plain");
	}
}
