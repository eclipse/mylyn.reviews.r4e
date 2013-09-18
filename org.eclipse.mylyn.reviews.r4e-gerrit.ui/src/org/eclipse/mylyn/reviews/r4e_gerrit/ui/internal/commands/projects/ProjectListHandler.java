/*******************************************************************************
 * Copyright (c) 2013 Ericsson
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 	This class implements the implementation of the R4E-Gerrit UI project list reviews handler.
 * 
 * Contributors:
 *   Jacques Bouthillier - Initial Implementation of the plug-in handler
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e_gerrit.ui.internal.commands.projects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.mylyn.internal.gerrit.core.GerritQuery;
import org.eclipse.mylyn.reviews.r4e_gerrit.ui.R4EGerritUi;
import org.eclipse.mylyn.reviews.r4e_gerrit.ui.internal.utils.UIUtils;
import org.eclipse.mylyn.reviews.r4egerrit.ui.views.R4EGerritTableView;

/**
 * @author Jacques Bouthillier
 * @version $Revision: 1.0 $
 *
 */

public class ProjectListHandler extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent aEvent) throws ExecutionException {
		R4EGerritUi.Ftracer.traceInfo("Search the Gerrit reviews for Projects List  " ); //$NON-NLS-1$

		R4EGerritUi.Ftracer.traceInfo("Execute:  ProjectList() "  ); //$NON-NLS-1$
//		try {
//			UIUtils.notInplementedDialog(aEvent.getCommand().getName());
//		} catch (NotDefinedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		R4EGerritTableView reviewTableView = R4EGerritTableView
				.getActiveView();

		// see http://gerrit-documentation.googlecode.com/svn/Documentation/2.5.2/user-search.html
		//for Project list
		reviewTableView.processCommands(GerritQuery.OPEN_CHANGES_BY_PROJECT);
	 
		return null;

	}

}
