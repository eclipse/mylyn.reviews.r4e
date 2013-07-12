/*******************************************************************************
 * Copyright (c) 2012, 2013 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements a property tester that is used to see if an anomaly can
 * be added
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.commands.testers;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Iterator;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.mylyn.reviews.frame.core.model.Topic;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4ECompareEditorInput;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4EFileRevisionEditorInput;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIModelController;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewBasic;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.UIUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class CloneAnomalyPropertyTester extends PropertyTester {

	/**
	 * Method test.
	 * 
	 * @param receiver
	 *            Object
	 * @param property
	 *            String
	 * @param args
	 *            Object[]
	 * @param expectedValue
	 *            Object
	 * @return boolean
	 * @see org.eclipse.core.expressions.IPropertyTester#test(Object, String, Object[], Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		//Command is disabled if there is no active review
		final R4EUIReviewBasic activeReview = R4EUIModelController.getActiveReview();
		if (null == activeReview) {
			return false;
		}

		//Command is disabled if active review is Read-Only
		if (activeReview.isReadOnly()) {
			return false;
		}

		//Command is disabled if the active review is completed
		if (((R4EReviewState) activeReview.getReview().getState()).getState().equals(
				R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED)) {
			return false;
		}

		//Command is disabled if there is no anomaly available for cloning
		EList<Topic> anomalies = activeReview.getReview().getTopics();
		if (anomalies.isEmpty()) {
			return false;
		}
		boolean isLocalAnomalies = false;
		int anomaliesSize = anomalies.size();
		for (int i = 0; i < anomaliesSize; i++) {
			if (!anomalies.get(i).getLocation().isEmpty()) {
				isLocalAnomalies = true;
				break;
			}
		}
		if (!isLocalAnomalies) {
			return false;
		}

		//If the file opened in the current editor is an R4E File that does not have a valid target version, the command is disabled
		if (receiver instanceof AbstractSet) {
			final Iterator<?> iterator = ((AbstractSet<?>) receiver).iterator();
			if (iterator.next() instanceof TextSelection) {
				if (!(isR4EEditorInputAvailable())) {
					return false;
				}
			}
		}

		if (receiver instanceof AbstractList) {
			//This happens when the command is selected from the outline view on an external or workspace file
			final Iterator<?> iterator = ((AbstractList<?>) receiver).iterator();
			if (!iterator.hasNext()) {
				if (!(isR4EEditorInputAvailable())) {
					return false;
				}
			} else {
				final Object obj = iterator.next();
				if ((R4EUIPlugin.isJDTAvailable() && obj instanceof ISourceReference)
						|| (R4EUIPlugin.isCDTAvailable() && obj instanceof org.eclipse.cdt.core.model.ISourceReference)) {
					if (!(isR4EEditorInputAvailable())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method isR4EEditorInputAvailable.
	 * 
	 * @return boolean
	 */
	private boolean isR4EEditorInputAvailable() {
		IEditorInput editorInput = null;
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (null != window) {
			final IWorkbenchPage page = window.getActivePage();
			if (null != page && null != page.getActiveEditor()) {
				editorInput = page.getActiveEditor().getEditorInput();
				if (editorInput instanceof R4EFileRevisionEditorInput) {
					if (null == ((R4EFileRevisionEditorInput) editorInput).getFileVersion().getRepositoryPath()) {
						return false;
					}
					//Compare editor
				} else if (editorInput instanceof R4ECompareEditorInput) {

					if (!UIUtils.isLeftCompareEditorSelected(page.getActiveEditor())) {
						return false;
					}

					final ITypedElement targetElement = ((R4ECompareEditorInput) editorInput).getCurrentDiffNode()
							.getTargetTypedElement();
					if (null == targetElement) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
