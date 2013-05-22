// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString, com.instantiations.assist.eclipse.analysis.deserializeabilitySecurity, com.instantiations.assist.eclipse.analysis.disallowReturnMutable, com.instantiations.assist.eclipse.analysis.enforceCloneableUsageSecurity, com.instantiations.assist.eclipse.analysis.mutabilityOfArrays
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
 * This class implements the Review Item element of the UI model
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 *******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.model;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EContextType;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EDelta;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EFileContext;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EFileVersion;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EItem;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EParticipant;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.CompatibilityException;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.OutOfSyncException;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.ResourceHandlingException;
import org.eclipse.mylyn.reviews.r4e.core.rfs.spi.IRFSRegistry;
import org.eclipse.mylyn.reviews.r4e.core.rfs.spi.RFSRegistryFactory;
import org.eclipse.mylyn.reviews.r4e.core.rfs.spi.ReviewsFileStorageException;
import org.eclipse.mylyn.reviews.r4e.core.utils.ResourceUtils;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.properties.general.ReviewItemProperties;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.CommandUtils;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.UIUtils;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class R4EUIReviewItem extends R4EUIFileContainer {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field fReviewItemFile. (value is ""icons/obj16/revitm_obj.gif"")
	 */
	public static final String REVIEW_ITEM_ICON_FILE = "icons/obj16/revitm_obj.gif";

	/**
	 * Field REMOVE_ELEMENT_ACTION_NAME. (value is ""Delete Review Item"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_NAME = "Disable Review Item";

	/**
	 * Field REMOVE_ELEMENT_ACTION_TOOLTIP. (value is ""Remove this review item from its parent review"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_TOOLTIP = "Remove this Review " + "Item from its Parent Review";

	/**
	 * Field RESTORE_ELEMENT_COMMAND_NAME. (value is ""Restore Review Item"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_NAME = "Restore Review Item";

	/**
	 * Field RESTORE_ELEMENT_ACTION_TOOLTIP. (value is ""Restore this disabled Review Item"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_TOOLTIP = "Restore this disabled Review Item";

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Constructor for R4EUIReviewItem.
	 * 
	 * @param aParent
	 *            IR4EUIModelElement
	 * @param aItem
	 *            R4EItem
	 * @param aName
	 *            String
	 * @param aItemType
	 *            - int
	 */
	public R4EUIReviewItem(IR4EUIModelElement aParent, R4EItem aItem, String aName, int aItemType) {
		super(aParent, aItem, aName, aItemType);
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
		return REVIEW_ITEM_ICON_FILE;
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
			return new ReviewItemProperties(this);
		}
		return null;
	}

	//Attributes

	/**
	 * Method setReviewed.
	 * 
	 * @param aReviewed
	 *            boolean
	 * @param aSetChildren
	 *            boolean
	 * @param aUpdateModel
	 *            boolean
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#setUserReviewed(boolean, boolean,
	 *      boolean)
	 */
	@Override
	public void setUserReviewed(boolean aReviewed, boolean aSetChildren, boolean aUpdateModel)
			throws ResourceHandlingException, OutOfSyncException {
		fUserReviewed = aReviewed;
		if (fUserReviewed) {
			//Check to see if we should mark the parent reviewed as well
			getParent().checkToSetUserReviewed(aUpdateModel);
		} else {
			//Remove check on parent, since at least one children is not set anymore
			getParent().setUserReviewed(fUserReviewed, false, aUpdateModel);
		}

		if (aSetChildren) {
			//Also set the children
			final int length = fFileContexts.size();
			for (int i = 0; i < length; i++) {
				fFileContexts.get(i).setChildUserReviewed(aReviewed, aUpdateModel);
			}
		}
	}

	/**
	 * Method checkToSetReviewed.
	 * 
	 * @param aUpdateModel
	 *            - flag that is used to see whether we should also update the serialization model
	 * @throws OutOfSyncException
	 * @throws ResourceHandlingException
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#checkToSetUserReviewed(boolean)
	 */
	@Override
	public void checkToSetUserReviewed(boolean aUpdateModel) throws ResourceHandlingException, OutOfSyncException {
		boolean allChildrenReviewed = true;
		final int length = fFileContexts.size();
		for (int i = 0; i < length; i++) {
			if (!(fFileContexts.get(i).isUserReviewed())) {
				allChildrenReviewed = false;
			}
		}
		//If all children are reviewed, mark the parent as reviewed as well
		if (allChildrenReviewed) {
			fUserReviewed = true;
			getParent().checkToSetUserReviewed(aUpdateModel);
		}
	}

	/**
	 * Method addAssignees.
	 * 
	 * @param aParticipants
	 *            - List<R4EParticipant> aParticipants
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#addAssignees(List<R4EParticipant>,
	 *      boolean)
	 */
	@Override
	public void addAssignees(List<R4EParticipant> aParticipants) {
		try {
			//assign participants
			final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fItem,
					R4EUIModelController.getReviewer());
			final EList<String> assignedParticipants = fItem.getAssignedTo();
			for (R4EParticipant participant : aParticipants) {
				assignedParticipants.add(participant.getId());
				((R4EUIReviewBasic) getParent()).getParticipant(participant.getId(), true);
			}
			R4EUIModelController.FResourceUpdater.checkIn(bookNum);
		} catch (ResourceHandlingException e1) {
			UIUtils.displayResourceErrorDialog(e1);
		} catch (OutOfSyncException e1) {
			UIUtils.displaySyncErrorDialog(e1);
		}

		//Also assign children
		for (R4EUIFileContext file : fFileContexts) {
			file.addAssignees(aParticipants);
		}
	}

	/**
	 * Method removeAssignees.
	 * 
	 * @param aParticipants
	 *            - List<R4EParticipant> aParticipants
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#removeAssignees(List<R4EParticipant>)
	 */
	@Override
	public void removeAssignees(List<R4EParticipant> aParticipants) {
		try {
			//unassign participants
			final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fItem,
					R4EUIModelController.getReviewer());
			final EList<String> assignedParticipants = fItem.getAssignedTo();
			for (R4EParticipant participant : aParticipants) {
				assignedParticipants.remove(participant.getId());
			}
			R4EUIModelController.FResourceUpdater.checkIn(bookNum);
		} catch (ResourceHandlingException e1) {
			UIUtils.displayResourceErrorDialog(e1);
		} catch (OutOfSyncException e1) {
			UIUtils.displaySyncErrorDialog(e1);
		}

		//Also unassign children
		for (R4EUIFileContext file : fFileContexts) {
			file.removeAssignees(aParticipants);
		}
	}

	/**
	 * Method getNumChanges.
	 * 
	 * @return int
	 */
	public int getNumChanges() {
		int numChanges = 0;
		for (R4EUIFileContext file : fFileContexts) {
			numChanges += file.getNumChanges();
		}
		return numChanges;
	}

	/**
	 * Method getNumReviewedChanges.
	 * 
	 * @return int
	 */
	public int getNumReviewedChanges() {
		int numReviewedChanges = 0;
		for (R4EUIFileContext file : fFileContexts) {
			numReviewedChanges += file.getNumReviewedChanges();
		}
		return numReviewedChanges;
	}

	/**
	 * Method getNumAnomalies.
	 * 
	 * @return int
	 */
	public int getNumAnomalies() {
		int numAnomalies = 0;
		for (R4EUIFileContext file : fFileContexts) {
			numAnomalies += file.getNumAnomalies();
		}
		return numAnomalies;
	}

	//Hierarchy

	/**
	 * Method createReviewItem
	 * 
	 * @param aBaseTempFileVersion
	 *            R4EFileVersion
	 * @param aTargetTempFileVersion
	 *            R4EFileVersion
	 * @param aType
	 *            R4EContextType
	 * @return R4EUIFileContext
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 */
	public R4EUIFileContext createFileContext(R4EFileVersion aBaseTempFileVersion,
			R4EFileVersion aTargetTempFileVersion, R4EContextType aType) throws ResourceHandlingException,
			OutOfSyncException {

		IRFSRegistry revRegistry = null;
		try {
			revRegistry = RFSRegistryFactory.getRegistry(((R4EUIReviewBasic) this.getParent()).getReview());
		} catch (ReviewsFileStorageException e1) {
			R4EUIPlugin.Ftracer.traceInfo("Exception: " + e1.toString() + " (" + e1.getMessage() + ")");
		}

		//Book serialization changes to review item level which traverses all the way to affected containment children
		Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fItem, R4EUIModelController.getReviewer());

		final R4EFileContext fileContext = R4EUIModelController.FModelExt.createR4EFileContext(fItem);
		fileContext.setType(aType);

		//Get Base version from Version control system and set core model data
		if (null != aBaseTempFileVersion) {
			final R4EFileVersion rfileBaseVersion = R4EUIModelController.FModelExt.createR4EBaseFileVersion(fileContext);
			CommandUtils.copyFileVersionData(rfileBaseVersion, aBaseTempFileVersion);

			//Add IFileRevision info
			if (null != revRegistry) {
				try {
					final IFileRevision fileRev = revRegistry.getIFileRevision(null, rfileBaseVersion);
					rfileBaseVersion.setFileRevision(fileRev);
				} catch (ReviewsFileStorageException e) {
					R4EUIPlugin.Ftracer.traceInfo("Exception: " + e.toString() + " (" + e.getMessage() + ")");
				}
			}

			//Add ProjectURI to the review item
			//TODO:  temporary solution.  We need to have an Iproject member variable in R4EFileVersion
			if (null != rfileBaseVersion.getResource()) {
				final String projPlatformURI = ResourceUtils.toPlatformURIStr(rfileBaseVersion.getResource()
						.getProject());
				if (!fItem.getProjectURIs().contains(projPlatformURI)) {
					fItem.getProjectURIs().add(projPlatformURI);
				}
			}
		}

		//Get Target version from Version control system and set core model data
		if (null != aTargetTempFileVersion) {
			final R4EFileVersion rfileTargetVersion = R4EUIModelController.FModelExt.createR4ETargetFileVersion(fileContext);
			CommandUtils.copyFileVersionData(rfileTargetVersion, aTargetTempFileVersion);

			//Add IFileRevision info
			if (null != revRegistry) {
				try {
					final IFileRevision fileRev = revRegistry.getIFileRevision(null, rfileTargetVersion);
					rfileTargetVersion.setFileRevision(fileRev);
				} catch (ReviewsFileStorageException e) {
					R4EUIPlugin.Ftracer.traceInfo("Exception: " + e.toString() + " (" + e.getMessage() + ")");
				}
			}

			//Add ProjectURI to the review item
			if (null != rfileTargetVersion.getResource()) {
				final String projPlatformURI = ResourceUtils.toPlatformURIStr(rfileTargetVersion.getResource()
						.getProject());
				if (!fItem.getProjectURIs().contains(projPlatformURI)) {
					fItem.getProjectURIs().add(projPlatformURI);
				}
			}
		}

		//Check-in to save to disk
		R4EUIModelController.FResourceUpdater.checkIn(bookNum);

		final R4EUIFileContext uiFile = new R4EUIFileContext(this, fileContext, fType);
		addChildren(uiFile);
		return uiFile;
	}

	/**
	 * Method restore.
	 * 
	 * @throws CompatibilityException
	 * @throws OutOfSyncException
	 * @throws ResourceHandlingException
	 */
	@Override
	public void restore() throws ResourceHandlingException, OutOfSyncException, CompatibilityException {
		super.restore();

		//Also restore any participant assigned to this element and its children
		for (String participant : fItem.getAssignedTo()) {
			R4EUIModelController.getActiveReview().getParticipant(participant, true);
		}
		for (R4EFileContext file : fItem.getFileContextList()) {
			for (String participant : file.getAssignedTo()) {
				R4EUIModelController.getActiveReview().getParticipant(participant, true);
			}
			for (R4EDelta content : file.getDeltas()) {
				for (String participant : content.getAssignedTo()) {
					R4EUIModelController.getActiveReview().getParticipant(participant, true);
				}
			}
		}
	}

	//Commands

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
				&& !(((R4EReviewState) ((R4EUIReviewBasic) getParent()).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the corresponding model element is assigned to a user
	 * 
	 * @param aUserName
	 *            - the user name
	 * @param aCheckChildren
	 *            - a flag that determines whether we will also check the child elements
	 * @return true/false
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isAssigned(String, boolean)
	 */
	@Override
	public boolean isAssigned(String aUsername, boolean aCheckChildren) {
		if (fItem.isEnabled()) {
			if (fItem.getAssignedTo().contains(aUsername)) {
				return true;
			} else {
				if (aCheckChildren) {
					for (R4EUIFileContext file : fFileContexts) {
						if (file.isAssigned(aUsername, aCheckChildren)) {
							return true;
						}
					}
				}
			}
		}
		return false;
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
				|| ((R4EReviewState) ((R4EUIReviewBasic) getParent()).getReview().getState()).getState().equals(
						R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED)) {
			return false;
		}
		return true;
	}

	/**
	 * Method isChangeReviewStateCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isChangeUserReviewStateCmd()
	 */
	@Override
	public boolean isChangeUserReviewStateCmd() {
		if (isEnabled()
				&& !isReadOnly()
				&& !(((R4EReviewState) ((R4EUIReviewBasic) getParent()).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))) {
			return true;
		}
		return false;
	}

	/**
	 * Method isAssignToCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isAssignToCmd()
	 */
	@Override
	public boolean isAssignToCmd() {
		if (isEnabled()
				&& !isReadOnly()
				&& !(((R4EReviewState) ((R4EUIReviewBasic) getParent()).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))) {
			return true;
		}
		return false;
	}

	/**
	 * Method isUnassignToCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isAssignToCmd()
	 */
	@Override
	public boolean isUnassignToCmd() {
		if (isEnabled()
				&& !isReadOnly()
				&& !(((R4EReviewState) ((R4EUIReviewBasic) getParent()).getReview().getState()).getState().equals(R4EReviewPhase.R4E_REVIEW_PHASE_COMPLETED))
				&& fItem.getAssignedTo().size() > 0) {
			return true;
		}
		//If at least on children has participants assigned, enable the command
		for (IR4EUIModelElement file : getChildren()) {
			if (file.isUnassignToCmd()) {
				return true;
			}
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
