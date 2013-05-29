/*******************************************************************************
 * Copyright (c) 2013 Ericsson
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 	This class implements the implementation of the R4E-Gerrit UI utility.
 * 
 * Contributors:
 *   Jacques Bouthillier - Initial Implementation of the plug-in utility
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e_gerrit.ui.internal.utils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.mylyn.reviews.r4e_gerrit.ui.R4EGerritUi;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Jacques Bouthillier
 * @version $Revision: 1.0 $
 *
 */

public class UIUtils {

	/**
	 * Method displaySyncErrorDialog.
	 * 
	 * @param e
	 *            OutOfSyncException
	 */
	public static void notInplementedDialog(String aSt) {
		String msg = "Not Implemented yet !";
		R4EGerritUi.Ftracer.traceWarning("Not Implemented yet");
		final ErrorDialog dialog = new ErrorDialog(
				null,
				"R4E-Information",
				"This method [ " + aSt + " ] is not ready yet" ,
				new Status(IStatus.INFO, R4EGerritUi.PLUGIN_ID, 0, "Not Implemented yet", null), IStatus.INFO);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				dialog.open();
			}
		});
		// TODO later we will want to do this automatically
	}

	/**
	 * Creates view preference frame and return the child composite.
	 * 
	 * @param aParent
	 *            the parent composite.
	 * @return the child composite.
	 */
	public static Composite createsGeneralComposite(Composite aParent, int style) {
		Composite child = new Composite(aParent, style );
		FormLayout layout = new FormLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 1;
		// child.setSize(WIDTH, HEIGHT);
		child.setLayout(layout);
		// child.minHeight = 100;
		return child;
	}

}
