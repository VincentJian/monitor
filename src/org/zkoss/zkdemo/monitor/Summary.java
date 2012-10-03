/* Summary.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zkdemo.monitor;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import org.zkoss.zk.ui.WebApp;
/**
 * 
 * @author sam
 *
 */
public class Summary {
	private final static DecimalFormat formater = new DecimalFormat(".##");
	private ConcurrentSkipListMap<String, Record> unfinishRequests = new ConcurrentSkipListMap<String, Record>();/*key: request-id*/
	private long serverSum, clientSum, networkLatencySum, serverCount, clientCount, networkCount;
	private int _clearTime =  60 * 1000; /*default 60 * 1000 */
	private Thread cleanThread;
	private final static String SUMMARY_ATTR = "SUMMARY";
	
	public Summary() {
		evalClearTime();
		cleanThread = new Thread(new CleanWorker());
		cleanThread.start();
	}
	
	private void evalClearTime() {
		final String time = org.zkoss.lang.Library.getProperty(
				"org.zkoss.monitor.cleartime", "30000");
		try {
			_clearTime = Integer.parseInt(time);
		} catch (NumberFormatException ex){
			_clearTime = 60 * 1000;
		}
		return;
	}
	
	public void setClearTime(int clearTime) {
		if (clearTime <= 0)
			_clearTime = 60 * 1000;
		else
			_clearTime = clearTime;
	}
	
	public int getClearTime() {
		return _clearTime;
	}
	
	public static Summary getSummary(WebApp webapp) {
		Summary summary = (Summary)webapp.getAttribute(SUMMARY_ATTR);
		if(summary == null) {
			summary = createSummary(webapp);
		}
		return summary;
	}
	private synchronized static Summary createSummary(WebApp webapp) {
		Summary summary = new Summary();
		webapp.setAttribute(SUMMARY_ATTR, summary);
		return summary;
	}
	
	/**
	 * call this function to free memory
	 */
	public void cleanResource() {
		Iterator<String> iter = unfinishRequests.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Record record = (Record) unfinishRequests.get(key);
			if ((Calendar.getInstance().getTimeInMillis() - record.getTimestamp()) > _clearTime)
				unfinishRequests.remove(key);
		}
	}
	
	/**
	 * 
	 * @return: server execution time sum
	 */
	public long getServerExeAverage() {
		return serverCount != 0 ? serverSum / serverCount : 0;
	}
	/**
	 * 
	 * @return: server execution time percentage
	 */
	public String getServerPercentage() {
		long totalAverage = getAverageSum();
		float per = totalAverage != 0 ? (float)getServerExeAverage() / totalAverage * 100: 0;
		return formater.format(per) + " %";
	}
	/**
	 * 
	 * @return
	 */
	public long getNetworkAverage() {
		return networkCount !=0 ? networkLatencySum / networkCount : 0;
	}
	/**
	 * 
	 * @return
	 */
	public String getNetworkPercentage() {
		long totalAverage = getAverageSum();
		float per = totalAverage!= 0 ? (float)getNetworkAverage() / totalAverage * 100 : 0;
		return formater.format(per) + " %";
	}
	/**
	 * 
	 * @return client execution time sum
	 */
	public long getClientExeAverage() {
		return clientCount != 0 ? clientSum / clientCount: 0;
	}
	/**
	 * 
	 * @return: client execution time percentage
	 */
	public String getClientPercentage() {
		long totalAverage = getAverageSum();
		float per = totalAverage!= 0 ? (float)getClientExeAverage() / totalAverage * 100 : 0;
		return formater.format(per) + " %";
	}
	
	/**
	 * 
	 * @return
	 */
	public long getAverageSum() {
		long serverAverage = getServerExeAverage();
		long clientAverage = getClientExeAverage();
		long netAverage = getNetworkAverage();
		return serverAverage + clientAverage + netAverage;
	}
	
	/**
	 * 
	 * @param requestId
	 * @param time
	 */
	public void addRequestStartAtClient(String requestId, long time) {
		getRecord(requestId).setTimeStartAtClient(time);
	}
	
	/**
	 * 
	 * @param requestId
	 * @param time
	 */
	public void addRequestStartAtServer(String requestId, long time) {
		getRecord(requestId).setTimeStartAtServer(time);
	}
	/**
	 * 
	 * @param requestId
	 * @param time
	 */
	public void addRequestCompleteAtServer(String requestId, long timeCompleteAtServer) {
		Record rec = getRecord(requestId);
		rec.setTimeCompleteAtServer(timeCompleteAtServer);
		
		Long timeStartAtServer = rec.getTimeStartAtServer();
		if (timeStartAtServer != null) {
			serverSum += (timeCompleteAtServer - timeStartAtServer);
			serverCount += 1;
		}
	}
	/**
	 * 
	 * @param requestId
	 * @param time
	 */
	public void addRequestReceiveAtClient(String requestId, long timeRecieveAtClient) {
		Record rec = getRecord(requestId);
		rec.setTimeRecieveAtClient(timeRecieveAtClient);
		
		Long timeStartAtClient = rec.getTimeStartAtClient();
		Long timeStartAtServer = rec.getTimeStartAtServer();
		Long timeCompleteAtServer = rec.getTimeCompleteAtServer();
		long networkLat = 0;
		if (timeStartAtClient == null || timeStartAtServer == null || timeCompleteAtServer == null)
			return;
		else {
			networkLat = (timeRecieveAtClient - timeCompleteAtServer) + (timeStartAtServer - timeStartAtClient);
			networkLatencySum += networkLat;
			networkCount += 1;
		}
	}

	/**
	 * 
	 * @param requestId
	 * @param time
	 */
	public void addRequestCompleteAtClient(String requestId, long timeCompleteAtClient) {
		Record rec = getRecord(requestId);
		rec.setTimeCompleteAtClient(timeCompleteAtClient);
		
		Long timeRecieveAtClient = rec.getTimeRecieveAtClient();
		if (timeRecieveAtClient != null) {
			clientSum += (timeCompleteAtClient - timeRecieveAtClient);
			clientCount += 1;
		}
	}

	private Record getRecord(String requestId) {
		Record rec = unfinishRequests.get(requestId);
		if (rec == null) {
			rec = new Record();
			unfinishRequests.put(requestId, rec);
		}
		return rec;
	}
	
	private class CleanWorker implements Runnable{
		@Override
		public void run() {			
			try {
				while(true) {
					Thread.sleep(_clearTime);
					cleanResource();
				}
			} catch (InterruptedException e) {
			}
		}
	}
}