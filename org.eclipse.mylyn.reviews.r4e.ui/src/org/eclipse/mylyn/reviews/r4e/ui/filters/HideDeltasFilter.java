// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString, com.instantiations.assist.eclipse.analysis.deserializeabilitySecurity, com.instantiations.assist.eclipse.analysis.enforceCloneableUsageSecurity
/*******************************************************************************
 * Copyright (c) 2011 Ericsson Research Canada
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements the Navigator View filter used to hide the
 * Delta elements
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.mylyn.reviews.r4e.ui.model.R4EUIDelta;
import org.eclipse.mylyn.reviews.r4e.ui.model.R4EUIDeltaContainer;

/**
 * @author lmcdubo
 * @version $Revision: 1.0 $
 */
public class HideDeltasFilter extends ViewerFilter {

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method select.
	 * 
	 * @param viewer
	 *            Viewer
	 * @param parentElement
	 *            Object
	 * @param element
	 *            Object
	 * @return boolean
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof R4EUIDeltaContainer || element instanceof R4EUIDelta) {
			return false;
		}
		return true;
	}
}
