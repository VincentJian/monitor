/* PieChartCtrl.java

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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Chart;
import org.zkoss.zul.ChartModel;
import org.zkoss.zul.SimplePieModel;
/**
 * 
 * @author sam
 *
 */
public class PieChartCtrl extends GenericForwardComposer{
	private Chart chart;
	
	private static Object getParam(String key){
		return Executions.getCurrent().getArg().get(key);
	}
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		ChartModel model = createSimplePieModel((List<RequestActivity>)getParam("RequestActivity"));
		chart.setModel(model);
	}

	private static SimplePieModel createSimplePieModel(List<RequestActivity> list) {
		RequestStatistics stat = new RequestStatistics();
		stat.addRequestActivity(list);
		
		SimplePieModel model = new SimplePieModel();
		model.setValue("Network Latency", stat.getNetworkLatencyPercentage());
		model.setValue("Server Execution", stat.getServerExecutionPercentage());
		model.setValue("Client Execution", stat.getClientExecutionPercentage());
		
		return model;
	}
}