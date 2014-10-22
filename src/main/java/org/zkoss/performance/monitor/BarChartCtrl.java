/* BarChartCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.performance.monitor;

import java.util.List;

import org.zkoss.chart.Charts;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;
/**
 * @author Sam, Vincent
 */
public class BarChartCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 4206494946500479856L;

	@Wire
	private Charts chart;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		chart.setModel(createCategoryModel(getParam("RequestActivity")));
	}

	private static CategoryModel createCategoryModel(List<RequestActivity> reqActivities) {
		CategoryModel model = new DefaultCategoryModel();
		for (RequestActivity reqActivity : reqActivities) {
			String ctxPath = " (" + reqActivity.getContextPath() + ")";
			String reqId = reqActivity.getRequestId();
			if (reqActivity.getServerExecution() != null)
				model.setValue(reqId + ctxPath, "Server Execution", reqActivity.getServerExecution());
			if (reqActivity.getBrowserExecution() != null)
				model.setValue(reqId + ctxPath, "Client Execution", reqActivity.getBrowserExecution());
			if (reqActivity.getNetworkLatency() != null)
				model.setValue(reqId + ctxPath, "Network Latency", reqActivity.getNetworkLatency());
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	private static List<RequestActivity> getParam(String key){
		return (List<RequestActivity>) Executions.getCurrent().getArg().get(key);
	}

}