/*******************************************************************************
 * Copyright (c) 2013 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements the "Add ..." a new Gerrit 
 * project locations.
 * 
 * Contributors:
 *   Jacques Bouthillier - Initial implementation of the handler
 *   
 ******************************************************************************/
package org.eclipse.mylyn.reviews.r4e_gerrit.ui.internal.commands.all;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.mylyn.reviews.r4e_gerrit.ui.R4EGerritUi;
import org.eclipse.mylyn.reviews.r4e_gerrit.ui.internal.utils.UIUtils;


/**
 * @author Jacques Bouthillier
 * @version $Revision: 1.0 $
 */

public class AllOpenReviewsHandler extends AbstractHandler {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field COMMAND_MESSAGE. (value is ""Add a Gerrit location ..."")
	 */
	private static final String COMMAND_MESSAGE = "Add all Open Gerrit reviews ...";

	// ------------------------------------------------------------------------
	// Variables
	// ------------------------------------------------------------------------

	
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method execute.
	 * 
	 * @param aEvent
	 *            ExecutionEvent
	 * @return Object
	 * @see org.eclipse.core.commands.IHandler#execute(ExecutionEvent)
	 */
	public Object execute(final ExecutionEvent aEvent) {

		R4EGerritUi.Ftracer.traceInfo("Search the Gerrit reviews for All Open  " ); //$NON-NLS-1$

		R4EGerritUi.Ftracer.traceInfo("Execute:   "  ); //$NON-NLS-1$
		try {
			UIUtils.notInplementedDialog(aEvent.getCommand().getName());
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		String menuItemText = "";
//		fServerUtil = new R4EGerritServerUtility();
//		Object obj = aEvent.getTrigger();
//		Map<String, String> param = aEvent.getParameters();
//		
//		if (obj instanceof Event) {
//			Event ev = (Event) obj;
//			Widget objWidget = ev.widget;
//		}
//
//		final Job job = new Job(COMMAND_MESSAGE) {
//
//			public String familyName = R4EUIConstants.R4E_UI_JOB_FAMILY;
//
//			@Override
//			public boolean belongsTo(Object aFamily) {
//				return familyName.equals(aFamily);
//			}
//
//			@Override
//			public IStatus run(final IProgressMonitor aMonitor) {
//				aMonitor.beginTask(COMMAND_MESSAGE, IProgressMonitor.UNKNOWN);
//						
//	
//
//				aMonitor.done();
//				return Status.OK_STATUS;
//			}
//		};
//		job.setUser(true);
//		job.schedule();
		return null;	

	}

}
