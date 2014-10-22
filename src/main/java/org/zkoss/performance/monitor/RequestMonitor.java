/* RequestMonitor.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.performance.monitor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Sessions;
/**
 * 
 * @author sam
 *
 */
public class RequestMonitor {
	private final static String ZUL_FILTER = "monitor.zul";
	private final static String REQUEST_ATTR = "REQUEST";
	/**
	 * 
	 * @return
	 */
	public static List<RequestActivity> getData() {
		List<RequestActivity> data = new LinkedList<RequestActivity>(getRequestActivityMap().values());
		Iterator<RequestActivity> iter = data.iterator();
		
		List<RequestActivity> list = new LinkedList<RequestActivity>();
		while(iter.hasNext()) {
			RequestActivity req = iter.next();
			if (isMonitorTarget(req.getContextPath()))
				list.add(req);
		}
		return list;
	}
	/**
	 * clear session monitor data
	 */
	public static void clearData() {
		getRequestActivityMap().clear();
	}
	
	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public static RequestActivity getRequestActivity(String requestId) {
		if (getRequestActivityMap() == null)
			createRequestActivityMap();
		return getRequestActivityMap().get(requestId);
	}
	
	/**
	 * 
	 * @param requestId
	 * @param exec
	 * @return
	 */
	public static RequestActivity getCurrentRequest(String requestId, Execution exec) {
		LinkedHashMap<String, RequestActivity> records = getRequestActivityMap();
		if (records.containsKey(requestId))
			return records.get(requestId);
		
		RequestActivity req = new RequestActivity(exec.getDesktop().getRequestPath(), exec.getDesktop().getId(), requestId);
		records.put(requestId, req);
		exec.getDesktop().getWebApp();
		return req;
	}

	private static LinkedHashMap<String, RequestActivity> createRequestActivityMap() {
		LinkedHashMap<String, RequestActivity> map = new LinkedHashMap<String, RequestActivity>();
		Sessions.getCurrent().setAttribute(REQUEST_ATTR, map);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private static LinkedHashMap<String, RequestActivity> getRequestActivityMap() {
		LinkedHashMap<String, RequestActivity> map = (LinkedHashMap<String, RequestActivity>)Sessions.getCurrent().getAttribute(REQUEST_ATTR);

		return map != null ? map : createRequestActivityMap();
	}
	
	private static boolean isMonitorTarget(String path) {
		return !path.contains(ZUL_FILTER);
	}
}