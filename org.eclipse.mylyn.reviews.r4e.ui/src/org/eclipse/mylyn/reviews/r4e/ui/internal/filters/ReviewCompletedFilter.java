/*******************************************************************************
 * Copyright (c) 2011, 2012 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements the Navigator View filter used to display only
 * uncompleted reviews in the Review Navigator tree
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReview;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewGroup;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIRule;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIRuleArea;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIRuleSet;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIRuleViolation;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class ReviewCompletedFilter extends ViewerFilter {

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

		if (element instanceof R4EUIReviewGroup || element instanceof R4EUIRuleSet || element instanceof R4EUIRuleArea
				|| element instanceof R4EUIRuleViolation || element instanceof R4EUIRule) {
			return true;
		}

		if (isParentReviewInProgress((IR4EUIModelElement) element)) {
			return true;
		}
		return isChildrenReviewInProgress((IR4EUIModelElement) element);
	}

	/**
	 * Checks if the children is an In Progress review
	 * 
	 * @param aCurrentElement
	 *            - the element to filter on
	 * @return true/false
	 */
	private boolean isChildrenReviewInProgress(IR4EUIModelElement aCurrentElement) {
		final int length = aCurrentElement.getChildren().length;
		IR4EUIModelElement element = null;
		for (int i = 0; i < length; i++) {
			element = aCurrentElement.getChildren()[i];
			if (!(element instanceof R4EUIReview)) {
				return false;
			}
			if (!(((R4EReviewState) ((R4EUIReview) element).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))
					|| ((R4EUIReview) element).isOpen()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the parent element cis an In Progress review
	 * 
	 * @param aCurrentElement
	 *            - the element to filter on
	 * @return true/false
	 */
	private boolean isParentReviewInProgress(IR4EUIModelElement aCurrentElement) {

		//Get Review parent
		IR4EUIModelElement reviewParentElement = aCurrentElement;
		while (!(reviewParentElement instanceof R4EUIReview)) {
			reviewParentElement = reviewParentElement.getParent();
			if (null == reviewParentElement) {
				return false;
			}
		}
		if (!(((R4EReviewState) ((R4EUIReview) reviewParentElement).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))
				|| ((R4EUIReview) reviewParentElement).isOpen()) {
			return true;
		}
		return false;
	}
}
