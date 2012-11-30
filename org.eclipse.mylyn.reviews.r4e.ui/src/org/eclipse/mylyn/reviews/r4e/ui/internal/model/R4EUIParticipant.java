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
 * This class implements the Participant element of the UI model
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.eclipse.mylyn.reviews.core.model.IReviewComponent;
import org.eclipse.mylyn.reviews.core.model.ITopic;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EParticipant;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewComponent;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewState;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewType;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EUserRole;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.OutOfSyncException;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.ResourceHandlingException;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.preferences.PreferenceConstants;
import org.eclipse.mylyn.reviews.r4e.ui.internal.properties.general.ParticipantProperties;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.R4EUIConstants;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.UIUtils;
import org.eclipse.mylyn.reviews.userSearch.query.IQueryUser;
import org.eclipse.mylyn.reviews.userSearch.query.QueryUserFactory;
import org.eclipse.mylyn.reviews.userSearch.userInfo.IUserInfo;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class R4EUIParticipant extends R4EUIModelElement {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field PARTICIPANT_ICON_FILE. (value is ""icons/obj16/part_obj.png"")
	 */
	public static final String PARTICIPANT_ICON_FILE = "icons/obj16/part_obj.png";

	/**
	 * Field PARTICIPANT_REVIEWER_ICON_FILE. (value is ""icons/obj16/partrevr_obj.png"")
	 */
	public static final String PARTICIPANT_REVIEWER_ICON_FILE = "icons/obj16/partrevr_obj.png";

	/**
	 * Field PARTICIPANT_LEAD_ICON_FILE. (value is ""icons/obj16/partlead_obj.png"")
	 */
	public static final String PARTICIPANT_LEAD_ICON_FILE = "icons/obj16/partlead_obj.png";

	/**
	 * Field PARTICIPANT_AUTHOR_ICON_FILE. (value is ""icons/obj16/partauthr_obj.png"")
	 */
	public static final String PARTICIPANT_AUTHOR_ICON_FILE = "icons/obj16/partauthr_obj.png";

	/**
	 * Field PARTICIPANT_ORGANIZER_ICON_FILE. (value is ""icons/obj16/partorg_obj.png"")
	 */
	public static final String PARTICIPANT_ORGANIZER_ICON_FILE = "icons/obj16/partorg_obj.png";

	/**
	 * Field REMOVE_ELEMENT_ACTION_NAME. (value is ""Disable Participant"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_NAME = "Disable Participant";

	/**
	 * Field REMOVE_ELEMENT_ACTION_TOOLTIP. (value is ""Disable this Participant"")
	 */
	private static final String REMOVE_ELEMENT_COMMAND_TOOLTIP = "Disable this Participant";

	/**
	 * Field RESTORE_ELEMENT_COMMAND_NAME. (value is ""Restore Participant"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_NAME = "Restore Participant";

	/**
	 * Field RESTORE_ELEMENT_ACTION_TOOLTIP. (value is ""Restore this disabled Participant"")
	 */
	private static final String RESTORE_ELEMENT_COMMAND_TOOLTIP = "Restore this disabled Participant";

	// ------------------------------------------------------------------------
	// Member variables
	// ------------------------------------------------------------------------

	/**
	 * Field fParticipant.
	 */
	private final R4EParticipant fParticipant;

	/**
	 * Field fParticipantDetails.
	 */
	private String fParticipantDetails = null;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Constructor for R4EUIParticipant.
	 * 
	 * @param aParent
	 *            IR4EUIModelElement
	 * @param aParticipant
	 *            R4EParticipant
	 * @param aType
	 *            R4EReviewType
	 */
	public R4EUIParticipant(IR4EUIModelElement aParent, R4EParticipant aParticipant, R4EReviewType aType) {
		super(aParent, aParticipant.getId());
		fReadOnly = aParent.isReadOnly();
		fParticipant = aParticipant;
		return;
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
		return getRoleIconPath(((R4EUIReviewBasic) getParent().getParent()).getReview().getType());
	}

	/**
	 * Method getToolTip.
	 * 
	 * @return String
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return fParticipant.getEmail();
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
			return new ParticipantProperties(this);
		}
		return null;
	}

	/**
	 * Method getParticipant.
	 * 
	 * @return R4EParticipant
	 */
	public R4EParticipant getParticipant() {
		return fParticipant;
	}

	/**
	 * Method getRoleIcon. Get participant icon path based on most significant role
	 * 
	 * @param aType
	 *            R4EReviewType
	 * @return String
	 */
	public String getRoleIconPath(R4EReviewType aType) {
		if (aType.equals(R4EReviewType.BASIC)) {
			return PARTICIPANT_ICON_FILE;
		} else {
			final List<R4EUserRole> roles = fParticipant.getRoles();
			//First check for Lead
			for (R4EUserRole role : roles) {
				if (role.equals(R4EUserRole.LEAD)) {
					return PARTICIPANT_LEAD_ICON_FILE;
				}
			}
			//Next Organizer
			for (R4EUserRole role : roles) {
				if (role.equals(R4EUserRole.ORGANIZER)) {
					return PARTICIPANT_ORGANIZER_ICON_FILE;
				}
			}
			//Next Author
			for (R4EUserRole role : roles) {
				if (role.equals(R4EUserRole.AUTHOR)) {
					return PARTICIPANT_AUTHOR_ICON_FILE;
				}
			}
			//Finally Reviewer
			for (R4EUserRole role : roles) {
				if (role.equals(R4EUserRole.REVIEWER)) {
					return PARTICIPANT_REVIEWER_ICON_FILE;
				}
			}
			//If no role, set default icon
			return PARTICIPANT_ICON_FILE;
		}
	}

	/**
	 * Method getRoles.
	 * 
	 * @param aRoles
	 *            List<R4EUserRole>
	 * @return String[]
	 */
	public String[] getRoles(List<R4EUserRole> aRoles) {
		final List<String> roles = new ArrayList<String>();
		for (R4EUserRole role : aRoles) {
			if (role.getValue() == R4EUserRole.ORGANIZER_VALUE) {
				roles.add(R4EUIConstants.USER_ROLE_ORGANIZER);
			} else if (role.getValue() == R4EUserRole.LEAD_VALUE) {
				roles.add(R4EUIConstants.USER_ROLE_LEAD);
			} else if (role.getValue() == R4EUserRole.AUTHOR_VALUE) {
				roles.add(R4EUIConstants.USER_ROLE_AUTHOR);
			} else if (role.getValue() == R4EUserRole.REVIEWER_VALUE) {
				roles.add(R4EUIConstants.USER_ROLE_REVIEWER);
			}
		}
		return roles.toArray(new String[roles.size()]);
	}

	/**
	 * Method mapStringToRole.
	 * 
	 * @param aRoleStr
	 *            String
	 * @return R4EUserRole
	 */
	public static R4EUserRole mapStringToRole(String aRoleStr) {
		if (aRoleStr.equals(R4EUIConstants.USER_ROLE_ORGANIZER)) {
			return R4EUserRole.ORGANIZER;
		} else if (aRoleStr.equals(R4EUIConstants.USER_ROLE_LEAD)) {
			return R4EUserRole.LEAD;
		}
		if (aRoleStr.equals(R4EUIConstants.USER_ROLE_AUTHOR)) {
			return R4EUserRole.AUTHOR;
		} else if (aRoleStr.equals(R4EUIConstants.USER_ROLE_REVIEWER)) {
			return R4EUserRole.REVIEWER;
		}
		return null;
	}

	/**
	 * Method mapRoleToString.
	 * 
	 * @param aRole
	 *            R4EUserRole
	 * @return String
	 */
	public static String mapRoleToString(R4EUserRole aRole) {
		if (aRole.equals(R4EUserRole.ORGANIZER)) {
			return R4EUIConstants.USER_ROLE_ORGANIZER;
		} else if (aRole.equals(R4EUserRole.LEAD)) {
			return R4EUIConstants.USER_ROLE_LEAD;
		}
		if (aRole.equals(R4EUserRole.AUTHOR)) {
			return R4EUIConstants.USER_ROLE_AUTHOR;
		} else if (aRole.equals(R4EUserRole.REVIEWER)) {
			return R4EUIConstants.USER_ROLE_REVIEWER;
		}
		return null;
	}

	/**
	 * Method isEnabled.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return fParticipant.isEnabled();
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
		final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fParticipant,
				R4EUIModelController.getReviewer());
		fParticipant.setEnabled(true);
		R4EUIModelController.FResourceUpdater.checkIn(bookNum);
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
		final Long bookNum = R4EUIModelController.FResourceUpdater.checkOut(fParticipant,
				R4EUIModelController.getReviewer());
		fParticipant.setId(((R4EParticipant) aModelComponent).getId());
		fParticipant.setEmail(((R4EParticipant) aModelComponent).getEmail());
		fParticipant.getRoles().addAll(((R4EParticipant) aModelComponent).getRoles());
		fParticipant.setFocusArea(((R4EParticipant) aModelComponent).getFocusArea());
		R4EUIModelController.FResourceUpdater.checkIn(bookNum);
	}

	/**
	 * Method getParticipantDetails.
	 * 
	 * @return String
	 */
	public String getParticipantDetails() {
		return fParticipantDetails;
	}

	/**
	 * Method setParticipantDetails.
	 */
	public void setParticipantDetails() {
		if (fParticipant.getEmail() == null || fParticipant.getEmail().equals("")) {

			if (fParticipant.getId().equals(
					R4EUIPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.P_USER_ID))) {
				//If this is the default user, get its email
				String email = R4EUIPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_USER_EMAIL);
				fParticipant.setEmail(email);
			}

			if (R4EUIModelController.isUserQueryAvailable()) {
				try {
					//Get detailed info from DB if available
					final IQueryUser query = new QueryUserFactory().getInstance();
					final List<IUserInfo> info = query.searchByUserId(fParticipant.getId());
					if (info.size() > 0) {
						final IUserInfo userInfo = info.get(0);
						fParticipantDetails = UIUtils.buildUserDetailsString(userInfo);
						if (null == fParticipant.getEmail() || fParticipant.getEmail().length() < 1) {
							fParticipant.setEmail(userInfo.getEmail());
						}
					}
				} catch (NamingException e) {
					R4EUIPlugin.Ftracer.traceWarning("Exception: " + e.toString() + " (" + e.getMessage() + ")");
				} catch (IOException e) {
					R4EUIPlugin.Ftracer.traceWarning("Exception: " + e.toString() + " (" + e.getMessage() + ")");
				}
			}

			//Safety if the LDAP query is not resolved properly
			if (fParticipant.getEmail() == null) {
				fParticipant.setEmail(""); //$NON-NLS-1$
			}
		}

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
				&& !isAssigned(fParticipant.getId(), true)
				&& !isAnomalyCreator()
				&& !isReadOnly()
				&& !(((R4EReviewState) R4EUIModelController.getActiveReview().getReview().getState()).getState().equals(R4EReviewPhase.COMPLETED))) {

			IR4EUIModelElement parentReview = this.getParent().getParent();
			if (parentReview instanceof R4EUIReviewBasic) {
				//Don't allow to remove the element if this is the last review lead in the current Review
				if (fParticipant.getRoles().contains(R4EUserRole.LEAD)) {
					if (reviewerRoleSize((R4EUIReviewBasic) parentReview, R4EUserRole.LEAD) < 2) {
						//This is the only review lead left, it shall not be disabled
						return false;
					}
				}

				if (fParticipant.getRoles().contains(R4EUserRole.ORGANIZER)) {
					if (reviewerRoleSize((R4EUIReviewBasic) parentReview, R4EUserRole.ORGANIZER) < 2) {
						//This is the only organiser left, it shall not be disabled
						return false;
					}
				}

				//All conditions passed, element can be disabled
				return true;
			}
		}
		return false;
	}

	/**
	 * Count the number of participants with the given role on the given review
	 * 
	 * @param parentReview
	 */
	private int reviewerRoleSize(R4EUIReviewBasic aReview, R4EUserRole aRole) {
		List<R4EParticipant> participants = aReview.getParticipants(false);
		if (participants == null) {
			return 0;
		}

		//Resolve the current number of review participants with the given role
		int roleCount = 0;
		for (R4EParticipant participant : participants) {
			//Don't consider disabled participants within the count
			List<R4EUserRole> roles = participant.getRoles();
			for (R4EUserRole role : roles) {
				if (role.equals(aRole)) {
					roleCount++;
					break;
				}
			}
		}

		return roleCount;
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
		return ((R4EUIReviewBasic) getParent().getParent()).isAssigned(aUsername, aCheckChildren);
	}

	/**
	 * Method isAnomalyCreator.
	 * 
	 * @return boolean
	 */
	private boolean isAnomalyCreator() {
		final List<ITopic> anomalies = ((R4EUIReviewBasic) getParent().getParent()).getReview().getTopics();
		for (ITopic anomaly : anomalies) {
			if (anomaly.getAuthor().equals(fParticipant)) {
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
	 * Method isRestoreElementCmd.
	 * 
	 * @return boolean
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIModelElement#iisRestoreElementCmd()
	 */
	@Override
	public boolean isRestoreElementCmd() {
		if (!(getParent().getParent().isEnabled())) {
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
}
