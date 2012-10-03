/* BarChartCtrl.java

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
import org.zkoss.zul.SimpleCategoryModel;
/**
 * 
 * @author sam
 *
 */
public class BarChartCtrl extends GenericForwardComposer{
	private Chart chart;
	private static Object getParam(String key){
		return Executions.getCurrent().getArg().get(key);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		ChartModel model = createCategoryModel((List<RequestActivity>)getParam("RequestActivity"));
		chart.setModel(model);
	}
	
	private static SimpleCategoryModel createCategoryModel(List<RequestActivity> list) {
		SimpleCategoryModel model = new SimpleCategoryModel();
		for (int i = 0; i < list.size(); i++) {
			RequestActivity req = list.get(i);
			
			String strPath = " (" + req.getContextPath() + ")";
			//if (req.getRequestLatency() != null)
			//	model.setValue(req.getRequestId() + strPath, "Request Latency", req.getRequestLatency());
			//if (req.getResponseLatency() != null)
			//	model.setValue(req.getRequestId() + strPath, "Response Latency", req.getResponseLatency());
			if (req.getServerExecution() != null)
				model.setValue(req.getRequestId() + strPath, "Server Execution", req.getServerExecution());
			if (req.getBrowserExecution() != null)
				model.setValue(req.getRequestId() + strPath, "Client Execution", req.getBrowserExecution());
			if (req.getNetworkLatency() != null)
				model.setValue(req.getRequestId() + strPath, "Network Latency", req.getNetworkLatency());
		}
		return model;
	}	
}