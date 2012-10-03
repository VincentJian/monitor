/* PerformanceCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zkdemo.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.GroupsModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleGroupsModel;
/**
 * 
 * @author sam
 *
 */
public class PerformanceCtrl extends GenericForwardComposer{
	private Button status;
	private Listbox monitorLBox;
	private Listcell serverLCell, clientLCell, networkCell, totalLCell;
	private final static String METER_STATUS = "METER_STATUS";
	private final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		refreshLabels();
	}
	public void onSelect$monitorLBox() {
		Iterator<Listitem> items = monitorLBox.getSelectedItems().iterator();
		while (items.hasNext()) {
			Listitem listitem = (Listitem) items.next();
			if (listitem instanceof Listgroup) {
				setListitemSelected(listitem.getLabel());
			}	
		}
	}
	private List<Listitem> getGroupingItems(Listgroup group) {
		List<Listitem> items = monitorLBox.getItems();
		List<Listitem> list = new ArrayList<Listitem>();
		boolean insideGroup = false;
		for (Listitem item : items) {
			if (item instanceof Listgroup) {
				insideGroup = item.equals(group);
				continue;
			}
			if (insideGroup)
				list.add(item);
		}
		return list;
	}
	
	private void setListitemSelected(String path) {
		List<Listitem> items = monitorLBox.getItems();
		for (Listitem item : items) {
			String requestId = item.getLabel();
			RequestActivity ra = RequestMonitor.getRequestActivity(requestId);
			if (ra != null && ra.getContextPath().equals(path))
				item.setSelected(true);
		}
	}
	
	private void refreshLabels() {
		if(isMeterEnable()) {
			status.setLabel("Stop");
			status.setImage("/img/stop.png");
		} else {
			status.setLabel("Start");
			status.setImage("/img/play.png");
		}	
	}
	
	public static Boolean isMeterEnable() {
		Boolean enable = (Boolean)Sessions.getCurrent().getAttribute(METER_STATUS);
		if (enable == null) {
			enable = false;
			Sessions.getCurrent().setAttribute(METER_STATUS, enable);
		}
		return enable;
	}
	
	public void onClick$status() {
		if (isMeterEnable()) {
			RequestMonitor.clearData();
			listRequestStatistics();
		}
		Sessions.getCurrent().setAttribute(METER_STATUS, !isMeterEnable());
		refreshLabels();
	}
	public void onClick$refresh() {
		listRequestStatistics();
		listSummaryStatistics();
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

	public void onClick$clear() {
		RequestMonitor.clearData();
		listRequestStatistics();
	}
	
	public void onClick$barChart() {
		Set<Listitem> selected = monitorLBox.getSelectedItems();
		if (selected.size() == 0) {
			showSelectedNoneErrorMsg();
			return;
		}
			
		List<RequestActivity> reqList = getSelectedRequestActivity(selected);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("RequestActivity", reqList);
		
		Component parent = MonitorCtrl.getCtrl(desktop).createNewPanel("Stage Comparison Chart");
		Executions.createComponents("barchart.zul", parent, map);
	}
	
	public void onClick$pieChart() {
		Set<Listitem> selected = monitorLBox.getSelectedItems();
		if (selected.size() == 0) {
			showSelectedNoneErrorMsg();
			return;
		}
		
		List<RequestActivity> reqList = getSelectedRequestActivity(selected);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("RequestActivity", reqList);
		
		Component parent = MonitorCtrl.getCtrl(desktop).createNewPanel("Time Percentage Chart");
		Executions.createComponents("piechart.zul", parent, map);
	}
	
	private void showSelectedNoneErrorMsg() {
		Messagebox.show("Selected Item = 0");
		return;
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
	
		List<List<RequestActivity>> reqs = new ArrayList<List<RequestActivity>>(reqGroup.values());
		Object[][] datas = new Object[reqGroup.values().size()][];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = reqs.get(i).toArray();
		}

		GroupsModel model = new SimpleGroupsModel(datas, getGroupHeader(reqGroup));
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
	
	private static Object[] getGroupHeader(HashMap<String, List<RequestActivity>> group) {
		return group.keySet().toArray();
	}

	private ListitemRenderer itemRenderer = new ListitemRenderer() {

		@Override
		public void render(Listitem item, Object obj, int index) throws Exception {
			if (obj instanceof String) {
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