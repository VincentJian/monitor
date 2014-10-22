/* PerformanceMeter.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.performance.monitor;

import org.zkoss.zk.ui.Execution;
/**
 * 
 * @author sam
 *
 */
public class PerformanceMeter implements org.zkoss.zk.ui.util.PerformanceMeter{
	private final static String ZUL_FILTER = "monitor.zul";
	
	@Override
	public void requestCompleteAtClient(String requestId, Execution exec, long time) {
		if (!isMonitorTarget(exec.getDesktop().getRequestPath()))
			return;
		
		Summary.getSummary(exec.getDesktop().getWebApp()).addRequestCompleteAtClient(requestId, time);
		
		if (!PerformanceCtrl.isMeterEnable())
			return;
		RequestActivity request = RequestMonitor.getCurrentRequest(requestId, exec);
		request.setTimeCompleteAtClient(time);
	}

	@Override
	public void requestCompleteAtServer(String requestId, Execution exec, long time) {
		if (!isMonitorTarget(exec.getDesktop().getRequestPath()))
			return;
		
		Summary.getSummary(exec.getDesktop().getWebApp()).addRequestCompleteAtServer(requestId, time);
		
		if (!PerformanceCtrl.isMeterEnable())
			return;
		RequestActivity request = RequestMonitor.getCurrentRequest(requestId, exec);
		request.setTimeCompleteAtServer(time);
	}

	@Override
	public void requestReceiveAtClient(String requestId, Execution exec, long time) {
		if (!isMonitorTarget(exec.getDesktop().getRequestPath()))
			return;
		
		Summary.getSummary(exec.getDesktop().getWebApp()).addRequestReceiveAtClient(requestId, time);
		
		if (!PerformanceCtrl.isMeterEnable())
			return;
		RequestActivity request = RequestMonitor.getCurrentRequest(requestId, exec);
		request.setTimeRecieveAtClient(time);
	}

	@Override
	public void requestStartAtClient(String requestId, Execution exec, long time) {
		if (!isMonitorTarget(exec.getDesktop().getRequestPath()))
			return;
		
		Summary.getSummary(exec.getDesktop().getWebApp()).addRequestStartAtClient(requestId, time);
		
		if (!PerformanceCtrl.isMeterEnable())
			return;
		RequestActivity request = RequestMonitor.getCurrentRequest(requestId, exec);
		request.setTimeStartAtClient(time);
	}

	@Override
	public void requestStartAtServer(String requestId, Execution exec, long time) {
		if (!isMonitorTarget(exec.getDesktop().getRequestPath()))
			return;
		
		Summary.getSummary(exec.getDesktop().getWebApp()).addRequestStartAtServer(requestId, time);
		
		if (!PerformanceCtrl.isMeterEnable())
			return;
		RequestActivity request = RequestMonitor.getCurrentRequest(requestId, exec);
		request.setTimeStartAtServer(time);
	}
	
	private static boolean isMonitorTarget(String path) {
		return !path.endsWith(ZUL_FILTER);
	}
}