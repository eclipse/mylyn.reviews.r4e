/*******************************************************************************
 * Copyright (c) 2013 Ericsson
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Francois Chouinard - Initial implementation
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e_gerrit.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.internal.gerrit.core.GerritQueryResultSchema;
import org.eclipse.mylyn.internal.gerrit.core.GerritTaskSchema;
import org.eclipse.mylyn.internal.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * An R4E Gerrit review task
 *  
 * @author Francois Chouinard
 * @version 0.1
 */
@SuppressWarnings("restriction")
public class R4EGerritTask extends AbstractTask {

    //-------------------------------------------------------------------------
    // Constants
    //-------------------------------------------------------------------------

    /**
     * Mylyn Task ID
     */
    public static final String TASK_ID = "r4e.mylyn.task.id"; 

    /**
     * Gerrit Review shortened Change-Id
     */
    public static final String SHORT_CHANGE_ID = TaskAttribute.TASK_KEY;

    /**
     * Gerrit Review Change-Id
     */
    public static final String CHANGE_ID = GerritQueryResultSchema.getDefault().CHANGE_ID.getKey();

    /**
     * Gerrit Review subject
     */
    public static final String SUBJECT = TaskAttribute.SUMMARY;

    /**
     * Gerrit Review owner
     */
    public static final String OWNER = GerritTaskSchema.getDefault().OWNER.getKey();

    /**
     * Gerrit Review project
     */
    public static final String PROJECT = TaskAttribute.PRODUCT; 

    /**
     * Gerrit Review branch
     */
    public static final String BRANCH = GerritTaskSchema.getDefault().BRANCH.getKey();

    /**
     * Gerrit Review creation date
     */
    public static final String DATE_CREATION = TaskAttribute.DATE_CREATION;

    /**
     * Gerrit Review last modification date
     */
    public static final String DATE_MODIFICATION = TaskAttribute.DATE_MODIFICATION;

    /**
     * Gerrit Review completion date
     */
    public static final String DATE_COMPLETION = TaskAttribute.DATE_COMPLETION;

    /**
     * Gerrit Review flags
     */
    public static final String IS_STARRED    = GerritTaskSchema.getDefault().IS_STARRED.getKey();
    public static final String REVIEW_STATE  = GerritTaskSchema.getDefault().REVIEW_STATE.getKey();
    public static final String VERIFY_STATE  = GerritTaskSchema.getDefault().VERIFY_STATE.getKey();

    /**
     * Date format
     */
    private static SimpleDateFormat FORMAT_HOUR  = new SimpleDateFormat("h:mm a");
    private static SimpleDateFormat FORMAT_MONTH = new SimpleDateFormat("MMM d");
    private static SimpleDateFormat FORMAT_FULL  = new SimpleDateFormat("MMM d, yyyy");

    //-------------------------------------------------------------------------
    // Attributes
    //-------------------------------------------------------------------------

    // The connector kind
    private final String fConnectorKind;
    
    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    /**
     * Construct an R4EGerritReviewSumamry from a Gerrit query result. Some
     * fields may be missing from the task data.
     * 
     * @param taskData the Gerrit task data
     */
    public R4EGerritTask(final TaskData taskData) {
    	super(taskData.getRepositoryUrl(), taskData.getTaskId(),
    			taskData.getRoot().getAttribute(TaskAttribute.SUMMARY).getValue() + 
    	 " [" + taskData.getRoot().getAttribute(TaskAttribute.TASK_KEY).getValue() + "]");

    	fConnectorKind = taskData.getConnectorKind();

    	TaskAttribute root = taskData.getRoot();
    	Map<String, TaskAttribute> attributes = root.getAttributes();

    	setAttribute(TASK_ID,           taskData.getTaskId());
        setAttribute(SHORT_CHANGE_ID,   getValue(attributes.get(SHORT_CHANGE_ID)));
        setAttribute(CHANGE_ID,         getValue(attributes.get(CHANGE_ID)));
        setAttribute(SUBJECT,           getValue(attributes.get(SUBJECT)));

        setAttribute(OWNER,             getValue(attributes.get(OWNER)));
        setAttribute(PROJECT,           getValue(attributes.get(PROJECT)));
        setAttribute(BRANCH,            getValue(attributes.get(BRANCH)));

        setAttribute(DATE_CREATION,     getValue(attributes.get(DATE_CREATION)));
        setAttribute(DATE_MODIFICATION, getValue(attributes.get(DATE_MODIFICATION)));
        setAttribute(DATE_COMPLETION,   getValue(attributes.get(DATE_COMPLETION)));

        setAttribute(IS_STARRED,        getValue(attributes.get(IS_STARRED)));
        setAttribute(REVIEW_STATE,      getValue(attributes.get(REVIEW_STATE)));
        setAttribute(VERIFY_STATE,      getValue(attributes.get(VERIFY_STATE)));
    }

    /*
     * Extract the first value from the specified task attributes list.
     * 
     * @param taskAttribute
     * @return the first value in the list (if any)
     */
    private String getValue(TaskAttribute taskAttribute) {
        if (taskAttribute != null) {
            List<String> values = taskAttribute.getValues();
            if (values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }

    //-------------------------------------------------------------------------
    // Getters and Setters
    //-------------------------------------------------------------------------

    /**
     * Format the requested Gerrit Review attribute as a date string.
     * As in the Gerrit web UI, the output format depends on the date
     * relation with 'today':
     * 
     * Same day:                 'hh:mm am/pm'
     * Same year, different day: 'Mon DD'
     * Different year:           'Mon DD, YYYY' (not implemented)
     *         
     * @param key one of { DATE_CREATION, DATE_MODIFICATION, DATE_COMPLETION }
     *
     * @return
     */
    public String getAttributeAsDate(String key) {
        // Validate the supplied key
        if (!key.equals(DATE_CREATION) && !key.equals(DATE_MODIFICATION) && !key.equals(DATE_COMPLETION)) {
            return "";
        }

        // Retrieve the date
        String rawDate = getAttribute(key);
        if (rawDate == null) {
            return "";
        }

        // Format the date
        Date date = new Date(Long.parseLong(rawDate));
        if (isToday(date)) {
            return FORMAT_HOUR.format(date);
        }
        if (isThisYear(date)) {
            return FORMAT_MONTH.format(date);
        }
        return FORMAT_FULL.format(date);
    }

    /**
     * Indicates if a date is 'today' 
     * 
     * @param date the date to check against 'today'
     * @return true if 'today'
     */
    private boolean isToday(Date date) {
        Calendar cal1 = Calendar.getInstance(); // today
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        boolean sameDay =
            (cal1.get(Calendar.YEAR)        == cal2.get(Calendar.YEAR) &&
            (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)));

        return sameDay;
    }

    /**
     * Indicates if a date is 'this year' 
     * 
     * @param date the date to check
     * @return true if same year as today
     */
    private boolean isThisYear(Date date) {
        Calendar cal1 = Calendar.getInstance(); // today
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    //-------------------------------------------------------------------------
    // AbstractTask
    //-------------------------------------------------------------------------

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public String getConnectorKind() {
		return fConnectorKind;
	}

    //-------------------------------------------------------------------------
    // Object
    //-------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TaskID  = ").append(getAttribute(R4EGerritTask.TASK_ID)).append("\n");
        buffer.append("ShortID = ").append(getAttribute(R4EGerritTask.SHORT_CHANGE_ID)).append("\n");
        buffer.append("ChangeID= ").append(getAttribute(R4EGerritTask.CHANGE_ID)).append("\n");
        buffer.append("Subject = ").append(getAttribute(R4EGerritTask.SUBJECT)).append("\n");
        buffer.append("Owner   = ").append(getAttribute(R4EGerritTask.OWNER)).append("\n");
        buffer.append("Project = ").append(getAttribute(R4EGerritTask.PROJECT)).append("\n");
        buffer.append("Branch  = ").append(getAttribute(R4EGerritTask.BRANCH)).append("\n");
        buffer.append("Updated = ").append(getAttributeAsDate(R4EGerritTask.DATE_MODIFICATION)).append("\n");
        buffer.append("STAR = ").append(getAttribute(R4EGerritTask.IS_STARRED))
              .append(", CRVW = ").append(getAttribute(R4EGerritTask.REVIEW_STATE))
              .append(", VRIF = ").append(getAttribute(R4EGerritTask.VERIFY_STATE)).append("\n");
        return buffer.toString();
    }

}
