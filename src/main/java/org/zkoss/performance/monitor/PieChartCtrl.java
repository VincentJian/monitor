/* PieChartCtrl.java

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
import org.zkoss.chart.model.DefaultPieModel;
import org.zkoss.chart.model.PieModel;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 * @author Sam, Vincent
 */
public class PieChartCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 3689154024718343107L;

	@Wire
	private Charts chart;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		chart.setModel(createPieModel(getParam("RequestActivity")));
	}

	private static PieModel createPieModel(List<RequestActivity> reqActivities) {
		RequestStatistics stat = new RequestStatistics();
		stat.addRequestActivity(reqActivities);

		PieModel model = new DefaultPieModel();
		model.setValue("Server Execution", stat.getServerExecutionPercentage());
		model.setValue("Client Execution", stat.getClientExecutionPercentage());
		model.setValue("Network Latency", stat.getNetworkLatencyPercentage());

		return model;
	}

	@SuppressWarnings("unchecked")
	private static List<RequestActivity> getParam(String key) {
		return (List<RequestActivity>) Executions.getCurrent().getArg().get(key);
	}
}