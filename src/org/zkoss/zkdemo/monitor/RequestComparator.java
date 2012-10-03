/* RequestComparator.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 28 17:57:11 TST 2009, Created by sam

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zkdemo.monitor;

import java.util.Comparator;
/**
 * 
 * @author sam
 *
 */
public class RequestComparator {
	public static Comparator<RequestActivity> path = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return req1.getContextPath().compareTo(req2.getContextPath());
		}
	};
	
	public static Comparator<RequestActivity> _path = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -req1.getContextPath().compareTo(req2.getContextPath());
		}
	};
	
	public static Comparator<RequestActivity> desktopId = new Comparator<RequestActivity>() {
		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return req1.getDesktopId().compareTo(req2.getDesktopId());
		}
	};
	
	public static Comparator<RequestActivity> _desktopId = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -req1.getDesktopId().compareTo(req2.getDesktopId());
		}
	};
	
	public static Comparator<RequestActivity> requestId = new Comparator<RequestActivity>(){
		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return req1.getRequestId().compareTo(req2.getRequestId());
		}
	};
	
	public static Comparator<RequestActivity> _requestId = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -req1.getRequestId().compareTo(req2.getRequestId());
		}
	};
	
	public static Comparator<RequestActivity> timeStamp = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return req1.getTimeStamp().compareTo(req2.getTimeStamp());
		}
	};
	
	public static Comparator<RequestActivity> _timeStamp = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -req1.getTimeStamp().compareTo(req2.getTimeStamp());
		}
	};
	/*
	public static Comparator<RequestActivity> requestLat = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return (int)(req1.getRequestLatency() - req2.getRequestLatency());
		}
	};
	
	public static Comparator<RequestActivity> _requestLat = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -(int)(req1.getRequestLatency() - req2.getRequestLatency());
		}
	};
	*/
	
	public static Comparator<RequestActivity> serverExecution = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return (int)(req1.getServerExecution() - req2.getServerExecution());
		}
	};
	
	public static Comparator<RequestActivity> _serverExecution = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -(int)(req1.getServerExecution() - req2.getServerExecution());
		}
		
	};

	public static Comparator<RequestActivity> clientExecution = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return (int)(req1.getBrowserExecution() - req2.getBrowserExecution());
		}
	};
	
	public static Comparator<RequestActivity> _clientExecution = new Comparator<RequestActivity>() {

		@Override
		public int compare(RequestActivity req1, RequestActivity req2) {
			return -(int)(req1.getBrowserExecution() - req2.getBrowserExecution());
		}
	};
}