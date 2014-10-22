/* MonitorCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
 */
package org.zkoss.performance.monitor;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

/**
 * @author Sam, Vincent
 */
public class MonitorCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = -127646396581989142L;

	@Wire
	private Tabbox panelTBox;
	@WireVariable
	private Desktop desktop;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		desktop.setAttribute(MonitorCtrl.class.getCanonicalName(), this);
	}

	public static MonitorCtrl getCtrl(Desktop desktop) {
		return (MonitorCtrl) desktop.getAttribute(MonitorCtrl.class.getCanonicalName());
	}

	public Tabpanel createNewPanel(String label) {
		Tab tab = new Tab();
		tab.setLabel(label + panelTBox.getTabs().getChildren().size());
		tab.setClosable(true);
		tab.setParent(panelTBox.getTabs());

		Tabpanel tPanel = new Tabpanel();
		tPanel.setParent(panelTBox.getTabpanels());

		panelTBox.setSelectedTab(tab);

		return tPanel;
	}
}
