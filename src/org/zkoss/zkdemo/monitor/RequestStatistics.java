/* RequestStatistics.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zkdemo.monitor;

import java.util.List;
/**
 * 
 * @author sam
 *
 */
public class RequestStatistics {
	private String path;
	private int networkLatSum;
	private int serverExeSum;
	private int clientExeSum;
	private int networkCount;
	private int serverCount;
	private int clientCount;
	
	public void addRequestActivity(List<RequestActivity> list) {
		for (int i = 0; i < list.size(); i++)
			addRequestActivity(list.get(i));
	}
	
	public void addRequestActivity(RequestActivity req) {
		path = req.getContextPath();
		
		Long networkLat = req.getNetworkLatency();
		if (networkLat != null) {
			networkLatSum += networkLat;
			networkCount++;
		}
		
		Long serverExe = req.getServerExecution();
		if (serverExe != null) {
			serverExeSum += req.getServerExecution();
			serverCount++;
		}
		
		Long clientExe = req.getBrowserExecution();
		if (clientExe != null) {
			clientExeSum += req.getBrowserExecution();
			clientCount++;
		}
	}
	
	public String getPath() {
		return path;
	}

	public int getNetworkLatencyAverage() {
		return networkCount !=0 ? networkLatSum/networkCount : 0;
	}
	public int getNetworkLatencyPercentage() {
		return networkCount != 0 ? (int)((float)networkLatSum/networkCount*100) : 0;
	}
	
	public int getServerExecutionAverage() {
		return serverCount !=0 ? serverExeSum/serverCount : 0;
	}
	
	public int getServerExecutionPercentage() {
		return serverCount != 0 ? (int)((float)serverExeSum/serverCount*100) : 0;
	}

	public int getClientExecutionAverage() {
		return clientCount !=0 ? clientExeSum/clientCount : 0;
	}
	
	public int getClientExecutionPercentage() {
		return clientCount != 0 ? (int)((float)clientExeSum/clientCount*100) : 0;
	}
}