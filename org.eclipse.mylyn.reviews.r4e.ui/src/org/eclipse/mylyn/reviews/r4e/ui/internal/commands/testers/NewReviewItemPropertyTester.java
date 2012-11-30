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
 * This class implements a property tester that is used to see if a review item can
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
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4ECompareEditorInput;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4EFileRevisionEditorInput;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4EFileRevisionTypedElement;
import org.eclipse.mylyn.reviews.r4e.ui.internal.editors.R4EFileTypedElement;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIModelController;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewBasic;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class NewReviewItemPropertyTester extends PropertyTester {

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
		final R4EReviewPhase phase = ((R4EReviewState) activeReview.getReview().getState()).getState();
		if (phase.equals(R4EReviewPhase.COMPLETED)) {
			return false;
		}

		//Check if command is enabled based on input
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
				//Single editor
				if (editorInput instanceof R4EFileRevisionEditorInput) {
					if (null == ((R4EFileRevisionEditorInput) editorInput).getFileVersion().getRepositoryPath()) {
						return false; //No valid target
					}
					//Compare editor
				} else if (editorInput instanceof R4ECompareEditorInput) {
					final ITypedElement targetElement = ((R4ECompareEditorInput) editorInput).getLeftElement();
					if (null == targetElement) {
						return false;
					}
					//R4EItem parentItem = null;
					final ITypedElement element = ((R4ECompareEditorInput) editorInput).getLeftElement();

					//NOTE: For now we only support adding new review items from an R4E Compare Editor.
					//		Eventually this should be enhanced to include any compare editor input
					if (!(element instanceof R4EFileRevisionTypedElement) && !(element instanceof R4EFileTypedElement)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}