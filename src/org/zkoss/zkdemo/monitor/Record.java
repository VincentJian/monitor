/* Record.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zkdemo.monitor;

import java.util.Calendar;
/**
 * 
 * @author Sam
 *
 */
public class Record {
    private Long timeStartAtClient;
    private Long timeStartAtServer;
    private Long timeCompleteAtServer;
    private Long timeRecieveAtClient;
    private Long timeCompleteAtClient;
	private long timestamp;
	
	public Record() {
		timestamp = Calendar.getInstance().getTimeInMillis();
	}
	
	public Long getTimeStartAtClient() {
		return timeStartAtClient;
	}


	public void setTimeStartAtClient(long timeStartAtClient) {
		this.timeStartAtClient = timeStartAtClient;
	}


	public Long getTimeStartAtServer() {
		return timeStartAtServer;
	}


	public void setTimeStartAtServer(long timeStartAtServer) {
		this.timeStartAtServer = timeStartAtServer;
	}


	public Long getTimeCompleteAtServer() {
		return timeCompleteAtServer;
	}


	public void setTimeCompleteAtServer(long timeCompleteAtServer) {
		this.timeCompleteAtServer = timeCompleteAtServer;
	}


	public Long getTimeRecieveAtClient() {
		return timeRecieveAtClient;
	}


	public void setTimeRecieveAtClient(long timeRecieveAtClient) {
		this.timeRecieveAtClient = timeRecieveAtClient;
	}


	public Long getTimeCompleteAtClient() {
		return timeCompleteAtClient;
	}


	public void setTimeCompleteAtClient(long timeCompleteAtClient) {
		this.timeCompleteAtClient = timeCompleteAtClient;
	}


	public Long getTimestamp() {
		return timestamp;
	}
}
