/* RequestActivity.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.performance.monitor;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author sam
 *
 */
public class RequestActivity implements Serializable {
	private static final long serialVersionUID = -3020978942070660917L;
	private String contextPath;
    private String desktopId;
    private String requestId;

    private long timeStartAtClient;
    private long timeStartAtServer;
    private long timeCompleteAtServer;
    private long timeRecieveAtClient;
    private long timeCompleteAtClient;
    
    private Date timeStamp;
    
    
    public RequestActivity (String contextPath, String desktopId, String requestId) {
    	this.contextPath = contextPath;
    	this.desktopId = desktopId;
    	this.requestId = requestId;
    	timeStamp = new Date();
    }

    
    /**
     * Returns the server side execution time
     * @return 
     */
    public Long getServerExecution() {
    	if (timeCompleteAtServer == 0 || timeStartAtServer == 0)
    		return null;
    	return timeCompleteAtServer - timeStartAtServer;
    }
    
    /**
     * Returns the client side execution time
     * @return 
     */
    public Long getBrowserExecution() {
    	if (timeRecieveAtClient == 0 || timeCompleteAtClient == 0)
    		return null;
    	return timeCompleteAtClient - timeRecieveAtClient; 
    }
    
    /**
     * Returns the total network time
     * @return 
     */
    public Long getNetworkLatency() {
    	if (timeStartAtClient == 0 || timeStartAtServer == 0 || timeCompleteAtServer == 0 || timeRecieveAtClient == 0)
    		return null;
    	long total = (timeRecieveAtClient - timeCompleteAtServer) + (timeStartAtServer - timeStartAtClient);

    	return total;
    }
    
    /**
     * 
     * @return total execution time
     */
    public Long getTotal() {
    	Long serverExe = getServerExecution();
    	Long clientExe = getBrowserExecution();
    	Long network = getNetworkLatency();
    	long total = 0;
    	total += serverExe != null ? serverExe : 0;
    	total += clientExe != null ? clientExe : 0;
    	total += network != null ? network : 0;
    	return total;
    }

    public Date getTimeStamp() {
    	return timeStamp;
    }
    
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getDesktopId() {
		return desktopId;
	}

	public void setDesktopId(String desktopId) {
		this.desktopId = desktopId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getTimeStartAtClient() {
		return timeStartAtClient;
	}

	public void setTimeStartAtClient(long timeStartAtClient) {
		this.timeStartAtClient = timeStartAtClient;
	}

	public long getTimeStartAtServer() {
		return timeStartAtServer;
	}

	public void setTimeStartAtServer(long timeStartAtServer) {
		this.timeStartAtServer = timeStartAtServer;
	}

	public long getTimeCompleteAtServer() {
		return timeCompleteAtServer;
	}

	public void setTimeCompleteAtServer(long timeCompleteAtServer) {
		this.timeCompleteAtServer = timeCompleteAtServer;
	}

	public long getTimeRecieveAtClient() {
		return timeRecieveAtClient;
	}

	public void setTimeRecieveAtClient(long timeRecieveAtClient) {
		this.timeRecieveAtClient = timeRecieveAtClient;
	}

	public long getTimeCompleteAtClient() {
		return timeCompleteAtClient;
	}

	public void setTimeCompleteAtClient(long timeCompleteAtClient) {
		this.timeCompleteAtClient = timeCompleteAtClient;
	}
}