/*******************************************************************************
 * Copyright (c) 2013 Ericsson
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 	This class implements the implementation of the R4E-Gerrit.
 * 
 * Contributors:
 *   Jacques Bouthillier - Initial Implementation of the plug-in
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e_gerrit;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.reviews.r4e_gerrit.trace.Tracer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

//import utils.Tracer;

/**
 * @author Jacques Bouthillier
 * @version $Revision: 1.0 $
 *
 */

/**
 * The activator class controls the plug-in life cycle
 */
public class R4EGerritPlugin extends Plugin {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field PLUGIN_ID. (value is ""org.eclipse.mylyn.reviews.r4e-gerrit"")
	 */
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.mylyn.reviews.r4e-gerrit"; //$NON-NLS-1$

	/**
	 * Field R4E_VERSION_QUALIFIER. (value is ""qualifier"")
	 */
	private static final String R4E_VERSION_QUALIFIER = "qualifier"; //$NON-NLS-1$

	// ------------------------------------------------------------------------
	// Member variables
	// ------------------------------------------------------------------------

	// The shared instance
	private static R4EGerritPlugin Fplugin;
	
	/**
	 * Field Tracer.
	 */
	public static Tracer Ftracer;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * The constructor
	 */
	public R4EGerritPlugin() {
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method start.
	 * 
	 * @param aContext
	 *            BundleContext
	 * @throws Exception
	 * @see org.osgi.framework.BundleActivator#start(BundleContext)
	 */
	@Override
	public void start(BundleContext aContext) throws Exception {
		super.start(aContext);
		Fplugin = this;
		Ftracer =  new Tracer();
		Ftracer.init(PLUGIN_ID);
		Ftracer.traceDebug("plugin started");
		verifyVersion (PLUGIN_ID);
	}

	/**
	 * Verify if we should consider the availability for the REPORT option based on the features level
	 */
	private void verifyVersion(String aBundleStr) {

		//Testing for the eclipse runtime here
		final Bundle bdleCurrent = Platform.getBundle(aBundleStr);
		if (bdleCurrent != null) {
			Version ver = bdleCurrent.getVersion();
			if (ver.getQualifier().equals(R4E_VERSION_QUALIFIER)) {
				//We are in a runtime environment, so enable it
				Ftracer.traceDebug("In a runtime environment for " + aBundleStr + 
						" Version: " + ver.toString() ); //$NON-NLS-1$
				return;
			}
		}

		//Testing for the binary execution
		IBundleGroupProvider[] grpprovider = Platform.getBundleGroupProviders();
		for (IBundleGroupProvider element : grpprovider) {
			IBundleGroup[] bdlgrp = element.getBundleGroups();
			Ftracer.traceDebug("bundle group count: " + bdlgrp.length); //$NON-NLS-1$
			for (int j = 0; j < bdlgrp.length; j++) {
				if (bdlgrp[j].getIdentifier().contains(aBundleStr)) {
					Ftracer.traceDebug("\t bdlgrp[" + j + "] : " + bdlgrp[j].getName() + "\t : " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ bdlgrp[j].getProviderName() + "\t version: " + bdlgrp[j].getVersion() + "\t : " //$NON-NLS-1$//$NON-NLS-2$
							+ bdlgrp[j].getIdentifier());
					break;

				}
			}

		}

	}

	/**
	 * Method stop.
	 * 
	 * @param aContext
	 *            BundleContext
	 * @throws Exception
	 * @see org.osgi.framework.BundleActivator#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext aContext) throws Exception {
		Fplugin = null;
		super.stop(aContext);
		Ftracer.traceDebug("plugin stopped");
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static R4EGerritPlugin getDefault() {
		return Fplugin;
	}

	/**
	 * Method logError.
	 * 
	 * @param aMsg
	 *            String
	 * @param ae
	 *            Exception
	 */
	public void logError(String aMsg, Exception ae) {
		getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, aMsg, ae));
	}

	/**
	 * Method logWarning.
	 * 
	 * @param aMsg
	 *            String
	 * @param ae
	 *            Exception
	 */
	public void logWarning(String aMsg, Exception ae) {
		getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, IStatus.OK, aMsg, ae));
	}

	/**
	 * Method logInfo.
	 * 
	 * @param aMsg
	 *            String
	 * @param ae
	 *            Exception
	 */
	public void logInfo(String aMsg, Exception ae) {
		getLog().log(new Status(IStatus.INFO, PLUGIN_ID, IStatus.OK, aMsg, ae));
	}

}
