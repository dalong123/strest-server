/**
 * 
 */
package com.trendrr.strest.server.v2.models.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.trendrr.oss.DynMap;
import com.trendrr.strest.server.v2.models.StrestPacketBase;
import com.trendrr.strest.server.v2.models.StrestHeader.Name;


/**
 * @author Dustin Norlander
 * @created May 1, 2012
 * 
 */
public class StrestHttpBase implements StrestPacketBase {

	protected static Log log = LogFactory.getLog(StrestHttpBase.class);

	
	protected HttpMessage message;
	
	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String header, String value) {
		if (header == null || value == null)
			return;
		message.addHeader(header, value);
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#addHeader(com.trendrr.strest.server.v2.models.StrestHeader.Name, java.lang.String)
	 */
	@Override
	public void addHeader(Name header, String value) {
		this.addHeader(header.getHttpName(), value);
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#getHeader(com.trendrr.strest.server.v2.models.StrestHeader.Name)
	 */
	@Override
	public String getHeader(Name header) {
		return message.getHeader(header.getHttpName());
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String header) {
		return message.getHeader(header);
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#setStrestProtocolVersion(float)
	 */
	@Override
	public void setProtocol(String name, float version) {
		String tmp[] = Float.toString(version).split("\\.");
		int major = Integer.parseInt(tmp[0]);
		int minor = 0;
		if (tmp.length >0) {
			minor = Integer.parseInt(tmp[1]);
		}
		this.message.setProtocolVersion(new HttpVersion(name, major, minor, true));
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#getStrestProtocolVersion()
	 */
	@Override
	public float getProtocolVersion() {
		HttpVersion version= this.message.getProtocolVersion();
		if (version == null) {
			return 0;
		}
		return Float.parseFloat(this.message.getProtocolVersion().getMajorVersion() + "." + this.message.getProtocolVersion().getMinorVersion());
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#getProtocolName()
	 */
	@Override
	public String getProtocolName() {
		HttpVersion version= this.message.getProtocolVersion();
		if (version == null) {
			return "STREST";
		}
		return version.getProtocolName();
	}
	
	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#setTxnId(java.lang.String)
	 */
	@Override
	public void setTxnId(String id) {
		this.addHeader(Name.TXN_ID, id);
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#getTxnId()
	 */
	@Override
	public String getTxnId() {
		return this.getHeader(Name.TXN_ID);
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#setContent(com.trendrr.oss.DynMap)
	 */
	@Override
	public void setContent(DynMap content) {
		try {
			this.setContent("application/json", content.toJSONString().getBytes("utf8"));
		} catch (UnsupportedEncodingException e) {
			log.error("Caught", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#setContent(java.lang.String, java.lang.String)
	 */
	@Override
	public void setContent(String contentType, byte[] bytes) {
		this.message.setContent(ChannelBuffers.wrappedBuffer(bytes));
		this.message.setHeader("Content-Type", contentType);
		this.message.setHeader(HttpHeaders.Names.CONTENT_LENGTH, bytes.length);

	}
	
	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#setContent(java.lang.String, java.lang.String)
	 */
	@Override
	public void setContent(String contentType, String utf8Str) {
		try {
			this.setContent(contentType, utf8Str.getBytes("utf8"));
		} catch (UnsupportedEncodingException e) {
			log.error("Caught", e);
		}
		
	}
	

	
	/**
	 * Returns the content as a byte array.
	 */
	@Override
	public Object getContent() {
		int length = this.message.getContent().readableBytes();
		byte[] bytes = new byte[length];
		this.message.getContent().getBytes(0, bytes);
		return bytes;
	}
	
	public String getContentAsString() {
		try {
			return new String((byte[])this.getContent(), "utf8");
		} catch (Exception x) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#cleanup()
	 */
	@Override
	public void cleanup() {
		this.message = null;

	}

	/* (non-Javadoc)
	 * @see com.trendrr.strest.server.v2.models.StrestPacketBase#toByteArray()
	 */
	@Override
	public byte[] toByteArray() {
		return null;
	}




}
