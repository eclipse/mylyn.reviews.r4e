/**
 * Copyright (c) 2010, 2012 Ericsson
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

import java.util.List;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EMeetingData;
import org.eclipse.mylyn.reviews.r4e.core.model.RModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>R4E Meeting Data</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getId <em>Id</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getSubject <em>Subject</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getLocation <em>Location</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getStartTime <em>Start Time</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getDuration <em>Duration</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getSentCount <em>Sent Count</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getSender <em>Sender</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getReceivers <em>Receivers</em>}</li>
 * <li>{@link org.eclipse.mylyn.reviews.r4e.core.model.impl.R4EMeetingDataImpl#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class R4EMeetingDataImpl extends EObjectImpl implements R4EMeetingData {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getSubject() <em>Subject</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSubject()
	 * @generated
	 * @ordered
	 */
	protected static final String SUBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSubject() <em>Subject</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSubject()
	 * @generated
	 * @ordered
	 */
	protected String subject = SUBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected String location = LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getStartTime()
	 * @generated
	 * @ordered
	 */
	protected static final long START_TIME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getStartTime()
	 * @generated
	 * @ordered
	 */
	protected long startTime = START_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSentCount() <em>Sent Count</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSentCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SENT_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSentCount() <em>Sent Count</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSentCount()
	 * @generated
	 * @ordered
	 */
	protected int sentCount = SENT_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSender() <em>Sender</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSender()
	 * @generated
	 * @ordered
	 */
	protected static final String SENDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSender() <em>Sender</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getSender()
	 * @generated
	 * @ordered
	 */
	protected String sender = SENDER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReceivers() <em>Receivers</em>}' attribute list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getReceivers()
	 * @generated
	 * @ordered
	 */
	protected EList<String> receivers;

	/**
	 * The default value of the '{@link #getBody() <em>Body</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getBody()
	 * @generated
	 * @ordered
	 */
	protected static final String BODY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBody() <em>Body</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getBody()
	 * @generated
	 * @ordered
	 */
	protected String body = BODY_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected R4EMeetingDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RModelPackage.Literals.R4E_MEETING_DATA;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubject(String newSubject) {
		String oldSubject = subject;
		subject = newSubject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__SUBJECT, oldSubject, subject));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(String newLocation) {
		String oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__LOCATION, oldLocation, location));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartTime(long newStartTime) {
		long oldStartTime = startTime;
		startTime = newStartTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__START_TIME, oldStartTime, startTime));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getSentCount() {
		return sentCount;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSentCount(int newSentCount) {
		int oldSentCount = sentCount;
		sentCount = newSentCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__SENT_COUNT, oldSentCount, sentCount));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSender(String newSender) {
		String oldSender = sender;
		sender = newSender;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__SENDER, oldSender, sender));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public List<String> getReceivers() {
		if (receivers == null) {
			receivers = new EDataTypeUniqueEList<String>(String.class, this, RModelPackage.R4E_MEETING_DATA__RECEIVERS);
		}
		return receivers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getBody() {
		return body;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setBody(String newBody) {
		String oldBody = body;
		body = newBody;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RModelPackage.R4E_MEETING_DATA__BODY, oldBody, body));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RModelPackage.R4E_MEETING_DATA__ID:
				return getId();
			case RModelPackage.R4E_MEETING_DATA__SUBJECT:
				return getSubject();
			case RModelPackage.R4E_MEETING_DATA__LOCATION:
				return getLocation();
			case RModelPackage.R4E_MEETING_DATA__START_TIME:
				return getStartTime();
			case RModelPackage.R4E_MEETING_DATA__DURATION:
				return getDuration();
			case RModelPackage.R4E_MEETING_DATA__SENT_COUNT:
				return getSentCount();
			case RModelPackage.R4E_MEETING_DATA__SENDER:
				return getSender();
			case RModelPackage.R4E_MEETING_DATA__RECEIVERS:
				return getReceivers();
			case RModelPackage.R4E_MEETING_DATA__BODY:
				return getBody();
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
			case RModelPackage.R4E_MEETING_DATA__ID:
				setId((String)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__SUBJECT:
				setSubject((String)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__LOCATION:
				setLocation((String)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__START_TIME:
				setStartTime((Long)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__DURATION:
				setDuration((Integer)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__SENT_COUNT:
				setSentCount((Integer)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__SENDER:
				setSender((String)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__RECEIVERS:
				getReceivers().clear();
				getReceivers().addAll((Collection<? extends String>)newValue);
				return;
			case RModelPackage.R4E_MEETING_DATA__BODY:
				setBody((String)newValue);
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
			case RModelPackage.R4E_MEETING_DATA__ID:
				setId(ID_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__SUBJECT:
				setSubject(SUBJECT_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__START_TIME:
				setStartTime(START_TIME_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__SENT_COUNT:
				setSentCount(SENT_COUNT_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__SENDER:
				setSender(SENDER_EDEFAULT);
				return;
			case RModelPackage.R4E_MEETING_DATA__RECEIVERS:
				getReceivers().clear();
				return;
			case RModelPackage.R4E_MEETING_DATA__BODY:
				setBody(BODY_EDEFAULT);
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
			case RModelPackage.R4E_MEETING_DATA__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case RModelPackage.R4E_MEETING_DATA__SUBJECT:
				return SUBJECT_EDEFAULT == null ? subject != null : !SUBJECT_EDEFAULT.equals(subject);
			case RModelPackage.R4E_MEETING_DATA__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case RModelPackage.R4E_MEETING_DATA__START_TIME:
				return startTime != START_TIME_EDEFAULT;
			case RModelPackage.R4E_MEETING_DATA__DURATION:
				return duration != DURATION_EDEFAULT;
			case RModelPackage.R4E_MEETING_DATA__SENT_COUNT:
				return sentCount != SENT_COUNT_EDEFAULT;
			case RModelPackage.R4E_MEETING_DATA__SENDER:
				return SENDER_EDEFAULT == null ? sender != null : !SENDER_EDEFAULT.equals(sender);
			case RModelPackage.R4E_MEETING_DATA__RECEIVERS:
				return receivers != null && !receivers.isEmpty();
			case RModelPackage.R4E_MEETING_DATA__BODY:
				return BODY_EDEFAULT == null ? body != null : !BODY_EDEFAULT.equals(body);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", subject: ");
		result.append(subject);
		result.append(", location: ");
		result.append(location);
		result.append(", startTime: ");
		result.append(startTime);
		result.append(", duration: ");
		result.append(duration);
		result.append(", sentCount: ");
		result.append(sentCount);
		result.append(", sender: ");
		result.append(sender);
		result.append(", receivers: ");
		result.append(receivers);
		result.append(", body: ");
		result.append(body);
		result.append(')');
		return result.toString();
	}

} //R4EMeetingDataImpl
