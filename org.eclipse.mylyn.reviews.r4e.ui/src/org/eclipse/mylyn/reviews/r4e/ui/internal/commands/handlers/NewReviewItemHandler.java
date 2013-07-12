// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString, com.instantiations.assist.eclipse.analysis.deserializeabilitySecurity, com.instantiations.assist.eclipse.analysis.enforceCloneableUsageSecurity
/*******************************************************************************
 * Copyright (c) 2010, 2013 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class implements the context-sensitive command to add a review item 
 * to a review
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.commands.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EFileVersion;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EFormalReview;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReview;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewComponent;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewPhase;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewType;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.OutOfSyncException;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.ResourceHandlingException;
import org.eclipse.mylyn.reviews.r4e.core.rfs.spi.ReviewsFileStorageException;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.IR4EUIPosition;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIContent;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIContentsContainer;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIFileContext;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIModelController;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewBasic;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIReviewItem;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUISelection;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUISelectionContainer;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUITextPosition;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.CommandUtils;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.MailServicesProxy;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.R4EUIConstants;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.UIUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class NewReviewItemHandler extends AbstractHandler {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field COMMAND_MESSAGE. (value is ""Adding New Review Items..."")
	 */
	private static final String COMMAND_MESSAGE = "Adding New Review Items...";

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method execute.
	 * 
	 * @param event
	 *            ExecutionEvent
	 * @return Object
	 * @see org.eclipse.core.commands.IHandler#execute(ExecutionEvent)
	 */
	public Object execute(final ExecutionEvent event) {

		final IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.getActiveEditor(); // $codepro.audit.disable methodChainLength

		final IEditorInput input;
		if (null != editorPart) {
			input = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage()
					.getActiveEditor()
					.getEditorInput(); // $codepro.audit.disable methodChainLength
		} else {
			input = null;
		}

		final Job job = new Job(COMMAND_MESSAGE) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				R4EUIModelController.setJobInProgress(true);
				final ISelection selection = HandlerUtil.getCurrentSelection(event);

				//Act differently depending on the type of selection we get
				if (selection instanceof ITextSelection) {
					monitor.beginTask(COMMAND_MESSAGE, IProgressMonitor.UNKNOWN);
					addReviewItemFromText((ITextSelection) selection, input);

				} else if (selection instanceof IStructuredSelection) {

					//First remove any editor selection (if open) if we execute the command from the review navigator view
					if (null != editorPart && editorPart instanceof ITextEditor) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								((ITextEditor) editorPart).getSelectionProvider().setSelection(null);
							}
						});
					}

					//Then iterate through all selections
					monitor.beginTask(COMMAND_MESSAGE, ((IStructuredSelection) selection).size());
					for (final Iterator<?> iterator = ((IStructuredSelection) selection).iterator(); iterator.hasNext();) {
						addReviewItemFromTree(iterator.next(), monitor);
						if (monitor.isCanceled()) {
							R4EUIModelController.setJobInProgress(false);
							return Status.CANCEL_STATUS;
						}
					}
				}
				R4EUIModelController.setJobInProgress(false);
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		return null;
	}

	/**
	 * Method addReviewItemFromText.
	 * 
	 * @param aSelection
	 *            ITextSelection
	 * @param aInput
	 *            - IEditorInput
	 */
	private void addReviewItemFromText(ITextSelection aSelection, IEditorInput aInput) {
		//This is a text selection in a text editor, we need to get the file path and
		//the position of the selection within the file
		try {
			final IR4EUIPosition position = CommandUtils.getPosition(aSelection);
			final R4EFileVersion baseVersion = CommandUtils.getBaseFileData(aInput);
			final R4EFileVersion targetVersion = CommandUtils.getTargetFileData(aInput);

			//Add selection to model
			if (null != targetVersion) {
				addReviewItem(baseVersion, targetVersion, position);
			} else {
				R4EUIPlugin.Ftracer.traceWarning("Trying to add review item to base file");
				final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_ERROR,
						"Add Review Item Error", new Status(IStatus.ERROR, R4EUIPlugin.PLUGIN_ID, 0,
								"No Target File present to Add Review Item", null), IStatus.ERROR);
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						dialog.open();
					}
				});
			}

		} catch (CoreException e) {
			UIUtils.displayCoreErrorDialog(e);
		} catch (ReviewsFileStorageException e) {
			UIUtils.displayReviewsFileStorageErrorDialog(e);
		}
	}

	/**
	 * Method addReviewItemFromTree.
	 * 
	 * @param aSelection
	 *            ITreeSelection
	 * @param aMonitor
	 *            IProgressMonitor
	 */
	private void addReviewItemFromTree(Object aSelection, IProgressMonitor aMonitor) {

		//This is a selection from the tree view (e.g. Review Navigator, Package Explorer etc...)
		//We will need to get the parent file path and the position of the element in a text editor
		//If the selection is on the File itself, then the selection will include all the lines
		//in the file.  Otherwise it will include all the lines corresponding to the currently 
		//selected element	
		try {

			IR4EUIPosition position = null;
			IFile workspaceFile = null;

			//Next find out what kind of selection we are dealing with
			if (aSelection instanceof IFile) {
				position = CommandUtils.getPosition((IFile) aSelection);
				workspaceFile = (IFile) aSelection;
			} else if (R4EUIPlugin.isJDTAvailable() && aSelection instanceof ISourceReference) {
				//NOTE:  This is always true because all elements that implement ISourceReference
				//       also implement IJavaElement.  The resource is always an IFile
				workspaceFile = (IFile) ((IJavaElement) aSelection).getResource();
				//TODO is that the right file to get the position???
				position = CommandUtils.getPosition((ISourceReference) aSelection, workspaceFile);
			} else if (R4EUIPlugin.isCDTAvailable()
					&& aSelection instanceof org.eclipse.cdt.core.model.ISourceReference) {
				//NOTE:  This is always true because all elements that implement ISourceReference
				//       also implement ICElement.  The resource is always an IFile
				//TO Fix this causes an error java.lang.ClassCastException: org.eclipse.core.internal.resources.Project cannot be cast to org.eclipse.core.resources.IFile
				org.eclipse.cdt.core.model.ICElement cdtElement = (org.eclipse.cdt.core.model.ICElement) aSelection;
				while (null != cdtElement) {
					if (cdtElement.getResource() instanceof IFile) {
						workspaceFile = (IFile) cdtElement.getResource();
						break;
					}
					cdtElement = cdtElement.getParent();
				}
				if (null == workspaceFile) {
					//This should never happen
					return;
				}
				//NOTE:  When selecting whole CDT File, the position is always set to line 0.  This is what is returned by CDT
				position = CommandUtils.getPosition((org.eclipse.cdt.core.model.ISourceReference) aSelection,
						workspaceFile);
			} else {
				//This should never happen
				R4EUIPlugin.Ftracer.traceWarning("Invalid selection " + aSelection.getClass().toString()
						+ ".  Ignoring");
				return;
			}

			//Add selection to model
			final R4EFileVersion baseVersion = CommandUtils.updateBaseFile(workspaceFile);
			final R4EFileVersion targetVersion = CommandUtils.updateTargetFile(workspaceFile);

			//Add selection to model
			if (null != targetVersion) {
				aMonitor.subTask("Adding " + targetVersion.getName());
				addReviewItem(baseVersion, targetVersion, position);
				aMonitor.worked(1);
			} else {
				R4EUIPlugin.Ftracer.traceWarning("Trying to add review item to base file");
				final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_ERROR,
						"Add Review Item Error", new Status(IStatus.ERROR, R4EUIPlugin.PLUGIN_ID, 0,
								"No Target File present to Add Review Item", null), IStatus.ERROR);
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						dialog.open();
					}
				});
			}
		} catch (CoreException e) {
			UIUtils.displayCoreErrorDialog(e);
		} catch (ReviewsFileStorageException e) {
			UIUtils.displayReviewsFileStorageErrorDialog(e);
		}
	}

	/**
	 * // $codepro.audit.disable blockDepth Method AddReviewItem. Adds a review item to the model based on user input
	 * 
	 * @param aUIPosition
	 *            IR4EUIPosition
	 * @param aBaseFileVersion
	 *            R4EFileVersion
	 * @param aTargetFileVersion
	 *            R4EFileVersion
	 */
	private void addReviewItem(R4EFileVersion aBaseFileVersion, R4EFileVersion aTargetFileVersion,
			IR4EUIPosition aUIPosition) {

		try {

			//Check if the file element and/or selection already exist
			//If file exists, add selection element to it
			//if selection element already exist, ignore command
			//for all other cases, create the parent elements as needed as well.	
			final List<R4EUIReviewItem> reviewItems = R4EUIModelController.getActiveReview().getReviewItems();

			boolean newSelection = true;
			for (R4EUIReviewItem reviewItem : reviewItems) {
				R4EUIFileContext[] files = (R4EUIFileContext[]) reviewItem.getChildren();
				for (R4EUIFileContext file : files) {
					if (null != file.getFileContext().getTarget()
							&& aTargetFileVersion.getLocalVersionID().equals(
									file.getFileContext().getTarget().getLocalVersionID())) {
						if ((null == file.getFileContext().getBase() && (null == aBaseFileVersion || "".equals(aBaseFileVersion.getVersionID())))
								|| (null != file.getFileContext().getBase() && null != aBaseFileVersion && aBaseFileVersion.getLocalVersionID()
										.equals(file.getFileContext().getBase().getLocalVersionID()))) {
							//File already exists, check if selection also exists
							R4EUIContentsContainer contentContainer = file.getContentsContainerElement();
							if (!(contentContainer instanceof R4EUISelectionContainer)) {
								//If this is a Commit element, we skip it
								continue;
							}
							R4EUIContent[] contentElements = (R4EUIContent[]) contentContainer.getChildren();
							for (R4EUIContent contentElement : contentElements) {
								if (contentElement.getPosition().isSameAs(aUIPosition)) {
									newSelection = false;
								}
							}
							if (newSelection) {
								addReviewItemToExistingFileContext((R4EUISelectionContainer) contentContainer,
										aUIPosition);
								R4EUIPlugin.Ftracer.traceInfo("Added review item: Target = "
										+ file.getFileContext().getTarget().getName()
										+ ((null != file.getFileContext().getBase()) ? "Base = "
												+ file.getFileContext().getBase().getName() : "") + " Position = "
										+ aUIPosition.toString());
							} else {
								//The selection already exists so ignore command
								R4EUIPlugin.Ftracer.traceWarning("Review Item already exists.  Ignoring");
								final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_WARNING,
										"Cannot add Review Item", new Status(IStatus.WARNING, R4EUIPlugin.PLUGIN_ID, 0,
												"Review Item already exists", null), IStatus.WARNING);
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										dialog.open();
									}
								});
							}
							return; //We found the file so we are done here	
						}
					}
				}
			}

			//This is a new file create it (and its parent reviewItem) and all its children
			addReviewItemToNewFileContext(aBaseFileVersion, aTargetFileVersion, aUIPosition);
			R4EUIPlugin.Ftracer.traceInfo("Added Review Item: Target = "
					+ aTargetFileVersion.getName()
					+ "_"
					+ aTargetFileVersion.getVersionID()
					+ ((null != aBaseFileVersion) ? "Base = " + aBaseFileVersion.getName() + "_"
							+ aBaseFileVersion.getVersionID() : "") + " Position = " + aUIPosition.toString());
		} catch (ResourceHandlingException e) {
			UIUtils.displayResourceErrorDialog(e);

		} catch (OutOfSyncException e) {
			UIUtils.displaySyncErrorDialog(e);

		} catch (CoreException e) {
			UIUtils.displayCoreErrorDialog(e);
		}
	}

	/**
	 * Method addReviewItemToExistingFileContext.
	 * 
	 * @param aContainer
	 *            R4EUISelectionContainer
	 * @param aUIPosition
	 *            IR4EUIPosition
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 */
	private void addReviewItemToExistingFileContext(R4EUISelectionContainer aContainer, IR4EUIPosition aUIPosition)
			throws ResourceHandlingException, OutOfSyncException {

		final R4EUISelection uiSelection = aContainer.createSelection((R4EUITextPosition) aUIPosition);
		R4EUIModelController.setJobInProgress(false);
		UIUtils.setNavigatorViewFocus(uiSelection, AbstractTreeViewer.ALL_LEVELS);
	}

	/**
	 * Method addReviewItemToNewFileContext.
	 * 
	 * @param aBaseFileVersion
	 *            R4EFileVersion
	 * @param aTargetFileVersion
	 *            R4EFileVersion
	 * @param aUIPosition
	 *            IR4EUIPosition
	 * @throws ResourceHandlingException
	 * @throws OutOfSyncException
	 * @throws CoreException
	 */
	private void addReviewItemToNewFileContext(R4EFileVersion aBaseFileVersion, R4EFileVersion aTargetFileVersion,
			IR4EUIPosition aUIPosition) throws ResourceHandlingException, OutOfSyncException, CoreException {

		final R4EUIReviewBasic uiReview = R4EUIModelController.getActiveReview();
		final R4EUIReviewItem uiReviewItem = uiReview.createResourceReviewItem(aTargetFileVersion.getName());
		if (null == uiReviewItem) {
			return;
		}

		//NOTE: Resource Review Items always only have a target file
		final R4EUIFileContext uiFileContext = uiReviewItem.createFileContext(aBaseFileVersion, aTargetFileVersion,
				null);
		if (null == uiFileContext) {
			uiReview.removeChildren(uiReviewItem, false);
			return;
		}

		final R4EUISelectionContainer uiSelectionContainer = (R4EUISelectionContainer) uiFileContext.getContentsContainerElement();
		uiSelectionContainer.createSelection((R4EUITextPosition) aUIPosition);
		R4EUIModelController.setJobInProgress(false);
		UIUtils.setNavigatorViewFocus(uiReviewItem, 1);

		//Send email notification if needed
		final List<R4EReviewComponent> addedItems = new ArrayList<R4EReviewComponent>();
		addedItems.add(uiReviewItem.getItem());
		final R4EReview review = uiReview.getReview();
		if (review.getType().equals(R4EReviewType.R4E_REVIEW_TYPE_FORMAL)) {
			if (((R4EFormalReview) review).getCurrent().getType().equals(R4EReviewPhase.R4E_REVIEW_PHASE_PREPARATION)) {
				MailServicesProxy.sendItemsAddedNotification(addedItems);
			}
		}
	}
}
