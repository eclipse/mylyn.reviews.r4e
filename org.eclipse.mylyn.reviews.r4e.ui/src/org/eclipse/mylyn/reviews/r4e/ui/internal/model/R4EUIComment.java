// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString, com.instantiations.assist.eclipse.analysis.deserializeabilitySecurity, com.instantiations.assist.eclipse.analysis.disallowReturnMutable, com.instantiations.assist.eclipse.analysis.enforceCloneableUsageSecurity, com.instantiations.assist.eclipse.analysis.mutabilityOfArrays, explicitThisUsage
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
 * This class implements the Comment element of the UI model
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.model;

import org.eclipse.mylyn.reviews.core.model.IComment;
import org.eclipse.mylyn.reviews.core.model.IReviewComponent;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EComment;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewComponent;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.OutOfSyncException;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.ResourceHandlingException;
import org.eclipse.mylyn.reviews.r4e.ui.internal.properties.general.CommentProperties;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.R4EUIConstants;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class R4EUIComment extends R4EUIModelElement {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field COMMENT_ICON_FILE. (value is ""icons/obj16/cmmnt_obj.gif"")
	 */
	private static final String COMMENT_ICON_FILE = "icons/obj16/cmmnt_obj.gif";

	/**
	 * Field COMMENT_DISABLED_ICON_FILE. (value is ""icons/obj16/cmmntdsbl_obj.png"")
	 */
	private static final String COMMENT_DISABLED_ICON_FILE = "icons/obj16/cmmntdsbl_obj.png";

	/**
	 * Field COPY_ELEMENT_COMMAND_NAME. (value is ""Copy Comments"")
	 */
	private static final String COPY_ELEMENT_COMMAND_NAME = "Copy Comments";

	/**
	 * Field COPY_ELEMENT_COMMAND_TOOLTIP. (value is ""Copy Comments to Clipboard"")
	 */
	private static final String COPY_ELEMENT_COMMAND_TOOLTIP = "Copy Comments to Clipboard";

	/**
	 * Field REMOVE_ELEMENT_ACTION_NAME. (value is ""Delete Comment"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_NAME = "Disable Comment";

	/**
	 * Field REMOVE_ELEMENT_ACTION_TOOLTIP. (value is ""Remove this comment from its parent anomaly"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_TOOLTIP = "Remove this Comment " + "from its parent anomaly";

	/**
	 * Field RESTORE_ELEMENT_COMMAND_NAME. (value is ""Restore Comment"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_NAME = "Restore Comment";

	/**
	 * Field RESTORE_ELEMENT_ACTION_TOOLTIP. (value is ""Restore this disabled Comment"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_TOOLTIP = "Restore this disabled Comment";

	/**
	 * Field COMMENT_LABEL_LENGTH. (value is 25)
	 */
	private static final int COMMENT_LABEL_LENGTH = 25;

	// ------------------------------------------------------------------------
	// Member variables
	// ------------------------------------------------------------------------

	/**
	 * Field fComment.
	 */
	private final R4EComment fComment;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Constructor for R4EUIComment.
	 * 
	 * @param aParent
	 *            IR4EUIModelElement
	 * @param aComment
	 *            R4EComment
	 */
	public R4EUIComment(IR4EUIModelElement aParent, R4EComment aComment) {
		super(aParent, buildCommentName(aComment.getDescription()));
		fReadOnly = aParent.isReadOnly();
		fComment = aComment;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method getImageLocation.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getImageLocation()
	 */
	public String getImageLocation() {
		if (isEnabled()) {
			return COMMENT_ICON_FILE;
		}
		return COMMENT_DISABLED_ICON_FILE;
	}

	/**
	 * Method getToolTip.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return fComment.getAuthor().getId() + ": " + fComment.getDescription();
	}

	/**
	 * Method getAdapter.
	 * 
	 * @param adapter
	 *            Class
	 * @return Object
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes")
	Class adapter) {
		if (IR4EUIModelElement.class.equals(adapter)) {
			return this;
		}
		if (IPropertySource.class.equals(adapter)) {
			return new CommentProperties(this);
		}
		return null;
	}

	/**
	 * Method buildCommentName.
	 * 
	 * @param aDescription
	 *            String
	 * @return String
	 */
	private static String buildCommentName(String aDescription) {
		//Only consider first line
		if (null != aDescription) {
			final String[] lines = aDescription.split(R4EUIConstants.LINE_FEED);
			if (lines[0].length() > COMMENT_LABEL_LENGTH) {
				return lines[0].substring(0, COMMENT_LABEL_LENGTH) + R4EUIConstants.ELLIPSIS_STR;
			} else {
				if (lines.length > 1) {
					return lines[0] + R4EUIConstants.ELLIPSIS_STR;
				} else {
					return lines[0];
				}
			}
		}
		return aDescription;
	}

	//Attributes

	/**
	 * Method getComment.
	 * 
	 * @return R4EComment
	 */
	public R4EComment getComment() {
		return fComment;
	}

	/**
	 * Set serialization model data by copying it from the passed-in object
	 * 
	 * @param aModelComponent
	 *            - a serialization model element to copy information from
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#setModelData(R4EReviewComponent)
	 */
	@Override
	public void setModelData(IReviewComponent aModelComponent) throws ResourceHandlingException, OutOfSyncException {
		//Set data in model element
		final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fComment,
				R4EUIModelController.getReviewer());
		fComment.setDescription(((IComment) aModelComponent).getDescription());
		R4EUIModelController.FResourceUpdater.checkIn(bookNum);
	}

	/**
	 * Method setEnabled.
	 * 
	 * @param aEnabled
	 *            boolean
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean aEnabled) throws ResourceHandlingException, OutOfSyncException {
		final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fComment,
				R4EUIModelController.getReviewer());
		fComment.setEnabled(true);
		R4EUIModelController.FResourceUpdater.checkIn(bookNum);
	}

	/**
	 * Method isEnabled.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return fComment.isEnabled();
	}

	//Commands

	/**
	 * Method isOpenEditorCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isOpenEditorCmd()
	 */
	@Override
	public boolean isOpenEditorCmd() {
		final IR4EUIModelElement ancestorElement = getParent().getParent().getParent();
		if (!(ancestorElement instanceof R4EUIFileContext)) {
			return false;
		}
		if (isEnabled() && null != R4EUIModelController.getActiveReview()
				&& null != ((R4EUIFileContext) ancestorElement).getTargetFileVersion()) {
			return true;
		}
		return false;
	}

	/**
	 * Method isCopyElementCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isCopyElementCmd()
	 */
	@Override
	public boolean isCopyElementCmd() {
		if (isEnabled()
				&& !isReadOnly()
				&& !(((R4EReviewState) R4EUIModelController.getActiveReview().getReview().getState()).getState().equals(R4EReviewPhase.COMPLETED))) {
			return true;
		}
		return false;
	}

	/**
	 * Method getCopyElementCmdName.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getCopyElementCmdName()
	 */
	@Override
	public String getCopyElementCmdName() {
		return COPY_ELEMENT_COMMAND_NAME;
	}

	/**
	 * Method getCopyElementCmdTooltip.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getCopyElementCmdTooltip()
	 */
	@Override
	public String getCopyElementCmdTooltip() {
		return COPY_ELEMENT_COMMAND_TOOLTIP;
	}

	/**
	 * Method isRemoveElementCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isRemoveElementCmd()
	 */
	@Override
	public boolean isRemoveElementCmd() {
		if (isEnabled()
				&& !isReadOnly()
				&& !(((R4EReviewState) R4EUIModelController.getActiveReview().getReview().getState()).getState().equals(R4EReviewPhase.COMPLETED))) {
			return true;
		}
		return false;
	}

	/**
	 * Method getRemoveElementCmdName.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getRemoveElementCmdName()
	 */
	@Override
	public String getRemoveElementCmdName() {
		return REMOVE_ELEMENT_COMMAND_NAME;
	}

	/**
	 * Method getRemoveElementCmdTooltip.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getRemoveElementCmdTooltip()
	 */
	@Override
	public String getRemoveElementCmdTooltip() {
		return REMOVE_ELEMENT_COMMAND_TOOLTIP;
	}

	/**
	 * Method isRestoreElementCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#iisRestoreElementCmd()
	 */
	@Override
	public boolean isRestoreElementCmd() {
		if (!(getParent().isEnabled())) {
			return false;
		}
		if (isEnabled()
				|| isReadOnly()
				|| ((R4EReviewState) R4EUIModelController.getActiveReview().getReview().getState()).getState().equals(
						R4EReviewPhase.COMPLETED)) {
			return false;
		}
		return true;
	}

	/**
	 * Method getRestoreElementCmdName.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getRestoreElementCmdName()
	 */
	@Override
	public String getRestoreElementCmdName() {
		return RESTORE_ELEMENT_COMMAND_NAME;
	}

	/**
	 * Method getRestoreElementCmdTooltip.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getRestoreElementCmdTooltip()
	 */
	@Override
	public String getRestoreElementCmdTooltip() {
		return RESTORE_ELEMENT_COMMAND_TOOLTIP;
	}

	/**
	 * Method isSendEmailCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isSendEmailCmd()
	 */
	@Override
	public boolean isSendEmailCmd() {
		if (isEnabled() && null != R4EUIModelController.getActiveReview()) {
			return true;
		}
		return false;
	}
}
