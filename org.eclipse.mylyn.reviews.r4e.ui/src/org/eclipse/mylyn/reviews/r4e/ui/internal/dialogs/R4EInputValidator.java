// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString, com.instantiations.assist.eclipse.analysis.deserializeabilitySecurity, com.instantiations.assist.eclipse.analysis.enforceCloneableUsageSecurity
/*******************************************************************************
 * Copyright (c) 2010, 2012 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements the dialog used to validate that a String received
 * corresponds to an existing folder
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.mylyn.reviews.frame.core.model.Review;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReview;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewGroup;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.WildcardFileFilter;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class R4EInputValidator implements IInputValidator {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field FOLDER_VALIDATION_ERROR_MESSAGE. (value is ""Folder does not exist"")
	 */
	private static final String FOLDER_VALIDATION_ERROR_MESSAGE = "Folder does not exist";

	/**
	 * Field EMPTY_VALIDATION_ERROR_MESSAGE. (value is ""No input given"")
	 */
	private static final String EMPTY_VALIDATION_ERROR_MESSAGE = "No Input given";

	/**
	 * Field GROUP_WILDCARD_NAME. (value is ""*_group_root.xrer"")
	 */
	private static final String GROUP_WILDCARD_NAME = "*_group_root.xrer";

	/**
	 * Field FOLDER_GROUP_EXISTS_VALIDATION_ERROR_MESSAGE. (value is ""Folder already contains a group file"")
	 */
	private static final String FOLDER_GROUP_EXISTS_VALIDATION_ERROR_MESSAGE = "Folder already contains a Group File";

	/**
	 * Field FILE_EXISTS_VALIDATION_ERROR_MESSAGE. (value is ""File already exists"")
	 */
	private static final String FILE_EXISTS_VALIDATION_ERROR_MESSAGE = "File already exists";

	/**
	 * Field REVIEW_EXISTS_VALIDATION_ERROR_MESSAGE. (value is ""A Review with the same name already exists within this
	 * Review Group"")
	 */
	private static final String REVIEW_EXISTS_VALIDATION_ERROR_MESSAGE = "A Review with the same name already exists within this Review Group";

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method isValid.
	 * 
	 * @param newText
	 *            String
	 * @return String
	 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(String)
	 */
	public String isFolderValid(String newText) { // $codepro.audit.disable booleanMethodNamingConvention
		if (null == newText || 0 == newText.length()) {
			return FOLDER_VALIDATION_ERROR_MESSAGE;
		}

		final File folder = new File(newText);
		if (folder.exists()) {
			return null;
		}
		return FOLDER_VALIDATION_ERROR_MESSAGE;
	}

	/**
	 * Method isFolderEmpty.
	 * 
	 * @param newText
	 *            String
	 * @return String
	 */
	public String isFolderEmpty(String newText) { // $codepro.audit.disable booleanMethodNamingConvention
		final File dir = new File(newText);
		final File[] files = dir.listFiles(new WildcardFileFilter(GROUP_WILDCARD_NAME));

		if (files.length > 0) {
			return FOLDER_GROUP_EXISTS_VALIDATION_ERROR_MESSAGE;
		}
		return null;
	}

	/**
	 * Method isFileExists.
	 * 
	 * @param newText
	 *            String
	 * @return String
	 */
	public String isFileExists(String newText) { // $codepro.audit.disable booleanMethodNamingConvention
		final File file = new File(newText);
		if (file.exists()) {
			return FILE_EXISTS_VALIDATION_ERROR_MESSAGE;
		}
		return null;
	}

	/**
	 * Method isEmpty.
	 * 
	 * @param newText
	 *            String
	 * @return String
	 */
	public String isValid(String newText) { // $codepro.audit.disable booleanMethodNamingConvention
		if (null == newText || 0 == newText.length()) {
			return EMPTY_VALIDATION_ERROR_MESSAGE;
		}
		return null;
	}

	/**
	 * Method isReviewExists.
	 * 
	 * @param aReviewName
	 *            String
	 * @param aParentGroup
	 *            R4EUIReviewGroup
	 * @return String
	 */
	public String isReviewExists(String aReviewName, R4EUIReviewGroup aParentGroup) { // $codepro.audit.disable booleanMethodNamingConvention	
		for (Review review : aParentGroup.getReviewGroup().getReviews()) {
			if (((R4EReview) review).getName().equalsIgnoreCase(aReviewName)) {
				return REVIEW_EXISTS_VALIDATION_ERROR_MESSAGE;
			}
		}
		return null;
	}
}
