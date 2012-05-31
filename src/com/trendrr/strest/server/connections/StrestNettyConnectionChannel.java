/**
 * 
 */
package com.trendrr.strest.server.connections;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

import com.trendrr.strest.server.v2.models.*;
import com.trendrr.strest.server.v2.models.http.StrestHttpResponse;


/**
 * @author Dustin Norlander
 * @created Jan 12, 2011
 * 
 */
public class StrestNettyConnectionChannel extends StrestConnectionChannel {

	protected Log log = LogFactory.getLog(StrestNettyConnectionChannel.class);
	
	Channel channel;
	
	public StrestNettyConnectionChannel(Channel channel) {
		this.channel = channel;
	}
	
		
	public Channel getChannel() {
		return this.channel;
	}

	
	@Override
	public synchronized boolean isConnected() {
		if (this.channel != null) {
			return this.channel.isConnected();
		}
		return false;
	}
	
	
	@Override
	protected Object doSendMessage(StrestResponse response) throws Exception {
		if (channel == null || !channel.isOpen()) {
			log.info("channel is closed, user has disconnected");
			return null;
		}

		if (response instanceof StrestHttpResponse) {
			//TODO: we should probably just have an encoder in the pipeline..
			return channel.write(((StrestHttpResponse)response).getResponse());
		} else {
			return channel.write(response);
		}
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(StrestConnectionChannel o) {
		if (o instanceof StrestNettyConnectionChannel) {
			return this.channel.compareTo(((StrestNettyConnectionChannel)o).getChannel());
		}
		return -1;
	}

}
