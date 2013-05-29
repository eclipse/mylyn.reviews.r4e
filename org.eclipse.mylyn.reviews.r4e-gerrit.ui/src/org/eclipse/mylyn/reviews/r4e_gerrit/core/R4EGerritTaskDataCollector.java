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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * A minimalist implementation of TaskDataCollector for R4E Gerrit queries.
 *  
 * @author Francois Chouinard
 * @version 0.1
 */
public class R4EGerritTaskDataCollector extends TaskDataCollector {

    //-------------------------------------------------------------------------
    // Attributes
    //-------------------------------------------------------------------------

    private Map<String, IStatus> fFailureByTaskId;

    private List<TaskData> fResults;

    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    /**
     * Default constructor
     */
    public R4EGerritTaskDataCollector() {
        fResults = new ArrayList<TaskData>();
        fFailureByTaskId = new HashMap<String, IStatus>();
    }

    //-------------------------------------------------------------------------
    // Getters
    //-------------------------------------------------------------------------

    /**
     * @return the query failures
     */
    public Map<String, IStatus> getFailureByTaskId() {
        return fFailureByTaskId;
    }

    /**
     * @return the query results
     */
    public List<TaskData> getResults() {
        return fResults;
    }

    //-------------------------------------------------------------------------
    // TaskDataCollector
    //-------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see org.eclipse.mylyn.tasks.core.data.TaskDataCollector#accept(org.eclipse.mylyn.tasks.core.data.TaskData)
     */
    @Override
    public void accept(TaskData taskData) {
        fResults.add(taskData);
    }

    /* (non-Javadoc)
     * @see org.eclipse.mylyn.tasks.core.data.TaskDataCollector#failed(java.lang.String, org.eclipse.core.runtime.IStatus)
     */
    @Override
    public void failed(String taskId, IStatus status) {
        fFailureByTaskId.put(taskId, status);
    }

}
