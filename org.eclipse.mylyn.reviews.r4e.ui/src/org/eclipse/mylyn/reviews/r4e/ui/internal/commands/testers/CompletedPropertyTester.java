/*******************************************************************************
 * Copyright (c) 2011, 2013 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements a property testers that looks for completed elements
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.commands.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIModelController;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class CompletedPropertyTester extends PropertyTester {

	/**
	 * Executes the property test determined by the parameter <code>property</code>.
	 * 
	 * @param receiver
	 *            the receiver of the property test
	 * @param property
	 *            the property to test
	 * @param args
	 *            additional arguments to evaluate the property. If no arguments are specified in the <code>test</code>
	 *            expression an array of length 0 is passed
	 * @param expectedValue
	 *            the expected value of the property. The value is either of type <code>java.lang.String</code> or a
	 *            boxed base type. If no value was specified in the <code>test</code> expressions then <code>null</code>
	 *            is passed
	 * @return returns <code>true</code> if the property is equal to the expected value; otherwise <code>false</code> is
	 *         returned
	 * @see org.eclipse.core.expressions.IPropertyTester#test(Object receiver, String property, Object[] args, Object
	 *      expectedValue)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (null == R4EUIModelController.getActiveReview()) {
			return false;
		}
		final R4EReviewPhase phase = ((R4EReviewState) R4EUIModelController.getActiveReview().getReview().getState()).getState();
		return !phase.equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED);
	}
}
