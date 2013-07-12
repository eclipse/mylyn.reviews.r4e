/**
/**
 * Copyright (c) 2010, 2013 Ericsson
 *  
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * Contributors:
 * Alvaro Sanchez-Leon  - Initial API and implementation
 * 
 */
package org.eclipse.mylyn.reviews.r4e.core.model.impl;

import java.util.Collection;

import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EID;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EParticipant;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EUserRole;
import org.eclipse.mylyn.reviews.r4e.core.model.RModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>R4E Participant</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EParticipantImpl#getRoles <em>Roles</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EParticipantImpl#getFocusArea <em>Focus Area</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EParticipantImpl#isIsPartOfDecision <em>Is Part Of
 * Decision</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EParticipantImpl#getReviewedContent <em>Reviewed Content
 * </em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EParticipantImpl#getTimeLog <em>Time Log</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class R4EParticipantImpl extends R4EUserImpl implements R4EParticipant {
	/**
	 * The cached value of the '{@link #getRoles() <em>Roles</em>}' attribute list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getRoles()
	 * @generated
	 * @ordered
	 */
	protected EList<R4EUserRole> roles;

	/**
	 * The default value of the '{@link #getFocusArea() <em>Focus Area</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getFocusArea()
	 * @generated
	 * @ordered
	 */
	protected static final String FOCUS_AREA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFocusArea() <em>Focus Area</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getFocusArea()
	 * @generated
	 * @ordered
	 */
	protected String focusArea = FOCUS_AREA_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsPartOfDecision() <em>Is Part Of Decision</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsPartOfDecision()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_PART_OF_DECISION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsPartOfDecision() <em>Is Part Of Decision</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsPartOfDecision()
	 * @generated
	 * @ordered
	 */
	protected boolean isPartOfDecision = IS_PART_OF_DECISION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReviewedContent() <em>Reviewed Content</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReviewedContent()
	 * @generated
	 * @ordered
	 */
	protected EList<R4EID> reviewedContent;

	/**
	 * The cached value of the '{@link #getTimeLog() <em>Time Log</em>}' map. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getTimeLog()
	 * @generated
	 * @ordered
	 */
	protected EMap<Date, Integer> timeLog;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected R4EParticipantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RModelPackage.Literals.R4E_PARTICIPANT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<R4EUserRole> getRoles() {
		if (roles == null) {
			roles = new EDataTypeUniqueEList<R4EUserRole>(R4EUserRole.class, this, RModelPackage.R4E_PARTICIPANT__ROLES);
		}
		return roles;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getFocusArea() {
		return focusArea;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFocusArea(String newFocusArea) {
		String oldFocusArea = focusArea;
		focusArea = newFocusArea;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_PARTICIPANT__FOCUS_AREA, oldFocusArea, focusArea));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsPartOfDecision() {
		return isPartOfDecision;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsPartOfDecision(boolean newIsPartOfDecision) {
		boolean oldIsPartOfDecision = isPartOfDecision;
		isPartOfDecision = newIsPartOfDecision;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_PARTICIPANT__IS_PART_OF_DECISION, oldIsPartOfDecision, isPartOfDecision));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<R4EID> getReviewedContent() {
		if (reviewedContent == null) {
			reviewedContent = new EObjectResolvingEList<R4EID>(R4EID.class, this, RModelPackage.R4E_PARTICIPANT__REVIEWED_CONTENT);
		}
		return reviewedContent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<Date, Integer> getTimeLog() {
		if (timeLog == null) {
			timeLog = new EcoreEMap<Date,Integer>(RModelPackage.Literals.MAP_DATE_TO_DURATION, MapDateToDurationImpl.class, this, RModelPackage.R4E_PARTICIPANT__TIME_LOG);
		}
		return timeLog;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RModelPackage.R4E_PARTICIPANT__TIME_LOG:
				return ((InternalEList<?>)getTimeLog()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RModelPackage.R4E_PARTICIPANT__ROLES:
				return getRoles();
			case RModelPackage.R4E_PARTICIPANT__FOCUS_AREA:
				return getFocusArea();
			case RModelPackage.R4E_PARTICIPANT__IS_PART_OF_DECISION:
				return isIsPartOfDecision();
			case RModelPackage.R4E_PARTICIPANT__REVIEWED_CONTENT:
				return getReviewedContent();
			case RModelPackage.R4E_PARTICIPANT__TIME_LOG:
				if (coreType) return getTimeLog();
				else return getTimeLog().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RModelPackage.R4E_PARTICIPANT__ROLES:
				getRoles().clear();
				getRoles().addAll((Collection<? extends R4EUserRole>)newValue);
				return;
			case RModelPackage.R4E_PARTICIPANT__FOCUS_AREA:
				setFocusArea((String)newValue);
				return;
			case RModelPackage.R4E_PARTICIPANT__IS_PART_OF_DECISION:
				setIsPartOfDecision((Boolean)newValue);
				return;
			case RModelPackage.R4E_PARTICIPANT__REVIEWED_CONTENT:
				getReviewedContent().clear();
				getReviewedContent().addAll((Collection<? extends R4EID>)newValue);
				return;
			case RModelPackage.R4E_PARTICIPANT__TIME_LOG:
				((EStructuralFeature.Setting)getTimeLog()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case RModelPackage.R4E_PARTICIPANT__ROLES:
				getRoles().clear();
				return;
			case RModelPackage.R4E_PARTICIPANT__FOCUS_AREA:
				setFocusArea(FOCUS_AREA_EDEFAULT);
				return;
			case RModelPackage.R4E_PARTICIPANT__IS_PART_OF_DECISION:
				setIsPartOfDecision(IS_PART_OF_DECISION_EDEFAULT);
				return;
			case RModelPackage.R4E_PARTICIPANT__REVIEWED_CONTENT:
				getReviewedContent().clear();
				return;
			case RModelPackage.R4E_PARTICIPANT__TIME_LOG:
				getTimeLog().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case RModelPackage.R4E_PARTICIPANT__ROLES:
				return roles != null && !roles.isEmpty();
			case RModelPackage.R4E_PARTICIPANT__FOCUS_AREA:
				return FOCUS_AREA_EDEFAULT == null ? focusArea != null : !FOCUS_AREA_EDEFAULT.equals(focusArea);
			case RModelPackage.R4E_PARTICIPANT__IS_PART_OF_DECISION:
				return isPartOfDecision != IS_PART_OF_DECISION_EDEFAULT;
			case RModelPackage.R4E_PARTICIPANT__REVIEWED_CONTENT:
				return reviewedContent != null && !reviewedContent.isEmpty();
			case RModelPackage.R4E_PARTICIPANT__TIME_LOG:
				return timeLog != null && !timeLog.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (roles: ");
		result.append(roles);
		result.append(", focusArea: ");
		result.append(focusArea);
		result.append(", isPartOfDecision: ");
		result.append(isPartOfDecision);
		result.append(')');
		return result.toString();
	}

} //R4EParticipantImpl
