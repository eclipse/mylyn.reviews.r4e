/*******************************************************************************
 * Copyright (c) 2012 Ericsson AB and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ericsson AB - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.reviews.r4e_gerrit.core.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.Platform;

// ESCA-JAVA0100:
/**
 * This class implements a simple trace facility using Eclipse that can be used to send debugging output to the console
 * and/or to file. It supports various levels of tracing
 * 
 * @author Sebastien Dubois
 */
public class Tracer {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	static final int DEFAULT_STACK_TRACE_ELEMENT = 3;

	// ------------------------------------------------------------------------
	// Member variables
	// ------------------------------------------------------------------------

	static boolean ERROR = false;

	static boolean WARNING = false;

	static boolean INFO = false;

	static boolean DEBUG = false;

	private String fPluginID;

	private BufferedWriter fTraceFile;

	private SimpleDateFormat fTimeFormat;

	private StringBuilder fTraceMessage;

	private boolean fLogToConsole;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	public Tracer() {
		fTraceFile = null;
		fTimeFormat = new SimpleDateFormat("HH:mm:ss:SSS");
		fTraceMessage = new StringBuilder();
		fLogToConsole = true;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Initialize the tracer object
	 * 
	 * @param aPluginId
	 *            - the calling plugin ID (as a String object)
	 */
	public void init(String aPluginId) {

		String traceKey = null;
		fPluginID = aPluginId;

		// Initialize tracer members from .options plugin file
		traceKey = Platform.getDebugOption(fPluginID + "/error");
		if (traceKey != null) {
			ERROR = (Boolean.valueOf(traceKey)).booleanValue();
		}

		traceKey = Platform.getDebugOption(fPluginID + "/warning");
		if (traceKey != null) {
			WARNING = (Boolean.valueOf(traceKey)).booleanValue();
		}

		traceKey = Platform.getDebugOption(fPluginID + "/info");
		if (traceKey != null) {
			INFO = (Boolean.valueOf(traceKey)).booleanValue();
		}

		traceKey = Platform.getDebugOption(fPluginID + "/debug");
		if (traceKey != null) {
			DEBUG = (Boolean.valueOf(traceKey)).booleanValue();
		}

		traceKey = Platform.getDebugOption(fPluginID + "/consoleLog");
		if (traceKey != null) {
			fLogToConsole = (Boolean.valueOf(traceKey)).booleanValue();
		}

		traceKey = Platform.getDebugOption(fPluginID + "/logfile");
		if (traceKey != null) {
			try {
				fTraceFile = new BufferedWriter(new FileWriter(traceKey));
			} catch (IOException e) {
				// ESCA-JAVA0265:
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close the trace output logfile (if used)
	 */
	public void closeLogfile() {
		if (fTraceFile == null) {
			return;
		}

		try {
			fTraceFile.close();
			fTraceFile = null;
		} catch (IOException e) {
			// ESCA-JAVA0265:
			e.printStackTrace();
		}
	}

	/**
	 * @param newValue
	 * @return
	 */
	public static boolean setError(boolean newValue) {
		boolean oldValue = ERROR;
		ERROR = newValue;
		return oldValue;
	}

	/**
	 * @param newValue
	 * @return
	 */
	public static boolean setDebug(boolean newValue) {
		boolean oldValue = DEBUG;
		DEBUG = newValue;
		return oldValue;
	}

	/**
	 * @param newValue
	 * @return
	 */
	public static boolean setInfo(boolean newValue) {
		boolean oldValue = INFO;
		INFO = newValue;
		return oldValue;
	}

	/**
	 * @param newValue
	 * @return
	 */
	public static boolean setWarning(boolean newValue) {
		boolean oldValue = WARNING;
		WARNING = newValue;
		return oldValue;
	}

	public static boolean isError() {
		return ERROR;
	}

	public static boolean isDebug() {
		return DEBUG;
	}

	public static boolean isInfo() {
		return INFO;
	}

	public static boolean isWarning() {
		return WARNING;
	}

	/**
	 * Output an error trace
	 * 
	 * @param aMsg
	 *            - the trace message to output
	 */
	public void traceError(String aMsg) {
		if (ERROR) {
			fTraceMessage.setLength(0);
			// Timestamp
			writeTimestamp(fTraceMessage);
			fTraceMessage.append(" E");
			writeThread(fTraceMessage);
			writeLocation(fTraceMessage);
			fTraceMessage.append(aMsg);
			outputTrace(fTraceMessage);
		}
	}

	/**
	 * Output a warning trace
	 * 
	 * @param aMsg
	 *            - the trace message to output
	 */
	public void traceWarning(String aMsg) {
		if (WARNING) {
			fTraceMessage.setLength(0);
			// Timestamp
			writeTimestamp(fTraceMessage);
			fTraceMessage.append(" W");
			writeThread(fTraceMessage);
			writeLocation(fTraceMessage);
			fTraceMessage.append(aMsg);
			outputTrace(fTraceMessage);
		}
	}

	/**
	 * Output an info trace
	 * 
	 * @param aMsg
	 *            - the trace message to output
	 */
	public void traceInfo(String aMsg) {
		if (INFO) {
			fTraceMessage.setLength(0);
			// Timestamp
			writeTimestamp(fTraceMessage);
			fTraceMessage.append(" I");
			writeThread(fTraceMessage);
			writeLocation(fTraceMessage);
			fTraceMessage.append(aMsg);
			outputTrace(fTraceMessage);
		}
	}

	/**
	 * Output a debug trace
	 * 
	 * @param aMsg
	 *            - the trace message to output
	 */
	public void traceDebug(String aMsg) {
		if (DEBUG) {
			fTraceMessage.setLength(0);
			// Timestamp
			writeTimestamp(fTraceMessage);
			fTraceMessage.append(" D");
			writeThread(fTraceMessage);
			writeLocation(fTraceMessage);
			fTraceMessage.append(aMsg);
			outputTrace(fTraceMessage);
		}
	}

	/**
	 * Write timestamp header
	 * 
	 * @param aSb
	 *            - the StringBuilder object (trace line) to append to
	 */
	private void writeTimestamp(StringBuilder aSb) {
		aSb.append("[" + fTimeFormat.format(new Date()) + "]");
	}

	/**
	 * Write thread header
	 * 
	 * @param aSb
	 *            - the StringBuilder object (trace line) to append to
	 */
	private static void writeThread(StringBuilder aSb) {
		aSb.append(" T=" + Thread.currentThread().getName());
	}

	/**
	 * Write location header
	 * 
	 * @param aSb
	 *            - the StringBuilder object (trace line) to append to
	 */
	private static void writeLocation(StringBuilder aSb) {
		int traceElement = DEFAULT_STACK_TRACE_ELEMENT;
		// ESCA-JAVA0067:
		final StackTraceElement e[] = Thread.currentThread().getStackTrace();
		if (e != null) {
			traceElement = traceElement >= e.length ? e.length - 1 : traceElement;
			StackTraceElement s = e[traceElement];
			if (s != null) {
				String simpleClassName = s.getClassName();
				String[] clsNameSegs = simpleClassName.split("\\.");
				if (clsNameSegs.length > 0) {
					simpleClassName = clsNameSegs[clsNameSegs.length - 1];
				}
				aSb.append(" " + simpleClassName + ":" + s.getLineNumber() + " (" + s.getMethodName() + "): ");
			}
		}
	}

	/**
	 * Write trace to console/file
	 * 
	 * @param aSb
	 *            - the StringBuilder object (trace line) to output
	 */
	private void outputTrace(StringBuilder aSb) {

		if (fLogToConsole) {
			// ESCA-JAVA0266:
			System.out.println(aSb);
		}

		if (fTraceFile != null) {
			try {
				fTraceFile.write(aSb.toString());
				fTraceFile.newLine();
				fTraceFile.flush();
			} catch (IOException e) {
				// ESCA-JAVA0265:
				e.printStackTrace();
			}
		}
	}

}
