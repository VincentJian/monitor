/* PerformanceCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.performance.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleGroupsModel;
import org.zkoss.zul.Window;
/**
 * @author Sam, Vincent
 */
public class PerformanceCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 1501597175706847516L;

	private final static String METER_STATUS = "METER_STATUS";
	private final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Wire
	private Button status;
	@Wire
	private Listbox monitorLBox;
	@Wire
	private Listcell serverLCell, clientLCell, networkCell, totalLCell;
	private Window bar, pie;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		refreshLabels();
	}

	@Listen("onClick = #status")
	public void start() {
		if (isMeterEnable()) {
			RequestMonitor.clearData();
			listRequestStatistics();
		}
		Sessions.getCurrent().setAttribute(METER_STATUS, !isMeterEnable());
		refreshLabels();
	}

	@Listen("onClick = #refresh")
	public void refreshStatistics() {
		listRequestStatistics();
		listSummaryStatistics();
	}

	@Listen("onClick = #clear")
	public void clearStatistics() {
		RequestMonitor.clearData();
		listRequestStatistics();
	}

	@Listen("onClick = #barChart")
	public void showBarChart() {
		Set<Listitem> selected = monitorLBox.getSelectedItems();
		if (selected.size() == 0) {
			showSelectedNoneErrorMsg();
			return;
		}
			
		List<RequestActivity> reqList = getSelectedRequestActivity(selected);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("RequestActivity", reqList);
		
		bar = (Window) Executions.createComponents("barchart.zul", null, map);
		bar.setTitle("Execution Time Bar Chart");
		bar.setBorder("normal");
		bar.setClosable(true);
		bar.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				bar.detach();
				bar = null;
			}
		});
		bar.doModal();
	}

	@Listen("onClick = #pieChart")
	public void showPieChart() {
		Set<Listitem> selected = monitorLBox.getSelectedItems();
		if (selected.size() == 0) {
			showSelectedNoneErrorMsg();
			return;
		}
		
		List<RequestActivity> reqList = getSelectedRequestActivity(selected);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("RequestActivity", reqList);
		
		pie = (Window) Executions.createComponents("piechart.zul", null, map);
		pie.setTitle("Execution Time Pie Chart");
		pie.setClosable(true);
		pie.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				pie.detach();
				pie = null;
			}
		});
		pie.doModal();
	}

	public static Boolean isMeterEnable() {
		Session sess = Sessions.getCurrent();
		Boolean enable = (Boolean) sess.getAttribute(METER_STATUS);
		if (enable == null) {
			enable = false;
			sess.setAttribute(METER_STATUS, enable);
		}
		return enable;
	}

	private void refreshLabels() {
		if (isMeterEnable()) {
			status.setLabel("Stop");
			status.setIconSclass("z-icon-stop stop");
		} else {
			status.setLabel("Start");
			status.setIconSclass("z-icon-play start");
		}
	}

	private void listSummaryStatistics() {
		Summary summary = Summary.getSummary(Executions.getCurrent().getDesktop().getWebApp());
		summary.cleanResource();
		
		long serverExeAver = summary.getServerExeAverage();
		serverLCell.setLabel(serverExeAver + " (" + summary.getServerPercentage() + ")");
		long clientExeAver = summary.getClientExeAverage();
		clientLCell.setLabel(clientExeAver + " (" + summary.getClientPercentage()+ ")");
		long networkAver = summary.getNetworkAverage();
		networkCell.setLabel(networkAver + " (" + summary.getNetworkPercentage() + ")");
		totalLCell.setLabel("" + summary.getAverageSum());
	}

	private void showSelectedNoneErrorMsg() {
		Messagebox.show("No item selected");
	}

	private static List<RequestActivity> getSelectedRequestActivity(Set<Listitem> selected) {		
		List<RequestActivity> reqList = new ArrayList<RequestActivity>();
		Iterator<Listitem> it = selected.iterator();
		while (it.hasNext()) {
			Listitem item = (Listitem) it.next();
			
			Listcell cell = (Listcell)item.getFirstChild();
			String label = cell.getLabel();
			
			RequestActivity req = RequestMonitor.getRequestActivity(label);
			if (req!= null)
				reqList.add(req);
		}
		return reqList;
	}

	private void listRequestStatistics() {

		List<RequestActivity> reqList = RequestMonitor.getData();		
		HashMap<String, List<RequestActivity>> reqGroup = groupByPath(reqList);
		
		String[] heads = reqGroup.keySet().toArray(new String[0]);
		RequestActivity[][] datas = new RequestActivity[reqGroup.size()][];
		int i = 0;
		for (Entry<String, List<RequestActivity>> entry : reqGroup.entrySet()) 
			datas[i++] = entry.getValue().toArray(new RequestActivity[0]);
		
		SimpleGroupsModel<RequestActivity, String, Object, Object> model = 
				new SimpleGroupsModel<RequestActivity, String, Object, Object>(datas, heads);
		
		model.setMultiple(true);
		monitorLBox.setModel(model);
		monitorLBox.setItemRenderer(itemRenderer);
	}

	private static HashMap<String, List<RequestActivity>> groupByPath(List<RequestActivity> requests) {
		HashMap<String, List<RequestActivity>> group = new HashMap<String, List<RequestActivity>>();
		for (RequestActivity requestActivity : requests) {
			List<RequestActivity> list = group.get(requestActivity.getContextPath());
			if (list == null) {
				list = new ArrayList<RequestActivity>();
				group.put(requestActivity.getContextPath(), list);
			}
			
			list.add(requestActivity);
		}
		return group;
	}

	private ListitemRenderer<Object> itemRenderer = new ListitemRenderer<Object>() {

		public void render(Listitem item, Object obj, int index) throws Exception {
			if (obj instanceof String) { //listgroup
				item.appendChild(new Listcell((String)obj));
				return;
			}
			
			RequestActivity activity = (RequestActivity)obj;
			Long networkLat = activity.getNetworkLatency();
			Long serverExe = activity.getServerExecution();
			Long clientExe = activity.getBrowserExecution();
			
			item.appendChild(new Listcell(activity.getRequestId()));
			item.appendChild(new Listcell(dateFormat.format(activity.getTimeStamp())));
			item.appendChild(new Listcell(serverExe != null ? "" + serverExe : "NaN"));
			item.appendChild(new Listcell(clientExe != null ? "" + clientExe : "NaN"));
			item.appendChild(new Listcell(networkLat != null ? "" + networkLat : "NaN"));
			item.appendChild(new Listcell("" + activity.getTotal()));
		}
	};
}