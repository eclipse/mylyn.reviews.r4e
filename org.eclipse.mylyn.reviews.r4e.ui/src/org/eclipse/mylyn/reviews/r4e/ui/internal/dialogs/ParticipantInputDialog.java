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
 * This class implements the dialog used to fill-in the Participant element details.
 *  This is a modal dialog
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EParticipant;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EReviewType;
import org.eclipse.mylyn.reviews.r4e.core.model.R4EUserRole;
import org.eclipse.mylyn.reviews.r4e.core.model.RModelFactory;
import org.eclipse.mylyn.reviews.r4e.core.model.serial.impl.ResourceHandlingException;
import org.eclipse.mylyn.reviews.r4e.ui.R4EUIPlugin;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIModelController;
import org.eclipse.mylyn.reviews.r4e.ui.internal.model.R4EUIParticipant;
import org.eclipse.mylyn.reviews.r4e.ui.internal.preferences.R4EPreferencePage;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.CommandUtils;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.IEditableListListener;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.R4EUIConstants;
import org.eclipse.mylyn.reviews.r4e.ui.internal.utils.UIUtils;
import org.eclipse.mylyn.reviews.userSearch.query.IQueryUser;
import org.eclipse.mylyn.reviews.userSearch.query.QueryUserFactory;
import org.eclipse.mylyn.reviews.userSearch.userInfo.IUserInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class ParticipantInputDialog extends FormDialog implements IParticipantInputDialog, IEditableListListener {

	// ------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------

	/**
	 * Field ADD_PARTICIPANT_DIALOG_TITLE. (value is ""Enter Participants details"")
	 */
	private static final String ADD_PARTICIPANT_DIALOG_TITLE = "Enter Participants Details";

	/**
	 * Field PARTICIPANTS_GROUP_LABEL. (value is ""Participants"")
	 */
	private static final String PARTICIPANTS_GROUP_LABEL = "Participants";

	/**
	 * Field PARTICIPANT_LABEL. (value is ""participant"")
	 */
	private static final String PARTICIPANT_LABEL = "participant";

	/**
	 * Field ADD_BUTTON_LABEL. (value is ""Add"")
	 */
	private static final String ADD_BUTTON_LABEL = "Add";

	/**
	 * Field REMOVE_BUTTON_LABEL. (value is ""Erase"")
	 */
	private static final String REMOVE_BUTTON_LABEL = "Erase";

	/**
	 * Field FIND_BUTTON_LABEL. (value is ""Find..."")
	 */
	private static final String FIND_BUTTON_LABEL = "Find...";

	/**
	 * Field CLEAR_BUTTON_LABEL. (value is ""Clear"")
	 */
	private static final String CLEAR_BUTTON_LABEL = "Clear";

	/**
	 * Field LDAP_NOT_CONFIGURED. (value is ""LDAP Server not present or not configured"")
	 */
	private static final String LDAP_NOT_CONFIGURED = "LDAP Server not present or not configured";

	/**
	 * Field LDAP_NOT_CONFIGURED_DETAILED. (value is ""No LDAP Server is currently configured to accept queries. " +
	 * "You can add the Server configuration in the R4E LDAP Preferences"")
	 */
	private static final String LDAP_NOT_CONFIGURED_DETAILED = "No LDAP Server is currently configured to accept queries.  "
			+ "You can add the Server configuration in the R4E LDAP Preferences";

	// ------------------------------------------------------------------------
	// Member variables
	// ------------------------------------------------------------------------

	/**
	 * Field fParticipantIdInputTextField.
	 */
	protected Text fParticipantIdInputTextField;

	/**
	 * Field fParticipantEmailInputTextField.
	 */
	protected Text fParticipantEmailInputTextField;

	/**
	 * Field fParticipantDetailsInputTextField.
	 */
	protected Text fParticipantDetailsInputTextField;

	/**
	 * Field fParticipantsDetailsValue.
	 */
	private final List<String> fParticipantsDetailsValues = new ArrayList<String>();

	/**
	 * The input validator, or <code>null</code> if none.
	 */
	private final IInputValidator fValidator;

	/**
	 * Field fAddedParticipantsTable.
	 */
	private Table fAddedParticipantsTable;

	/**
	 * Field fUserToAddCombo.
	 */
	private Scrollable fUserToAddCombo;

	/**
	 * Field fParticipants.
	 */
	private List<R4EParticipant> fParticipants = new ArrayList<R4EParticipant>();

	/**
	 * Field fSelectedParticipantIndex.
	 */
	protected int fSelectedParticipantIndex = R4EUIConstants.INVALID_VALUE;

	/**
	 * Field fAddUserButton.
	 */
	private Button fAddUserButton;

	/**
	 * Field fRemoveParticipantsButton.
	 */
	private Button fRemoveUserButton;

	/**
	 * Field fClearParticipantsButton.
	 */
	private Button fFindUserButton;

	/**
	 * Field fClearParticipantsButton.
	 */
	private Button fClearParticipantsButton;

	/**
	 * Field fReviewSource.
	 */
	private final boolean fReviewSource;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Constructor for ParticipantInputDialog. <br>
	 * Using a ShellProvider to Defer the resolution of the shell to the opening of the dialog i.e. when running in the
	 * UI thread
	 * 
	 * @param aShellProvider
	 *            Shell
	 * @param aReviewSource
	 *            boolean
	 */
	public ParticipantInputDialog(IShellProvider aShellProvider, boolean aReviewSource) {
		super(aShellProvider);
		setBlockOnOpen(true);
		fValidator = new R4EInputValidator();
		fReviewSource = aReviewSource;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * Method buttonPressed.
	 * 
	 * @param buttonId
	 *            int
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			final List<R4EParticipant> validatedParticipants = new ArrayList<R4EParticipant>();

			for (R4EParticipant newParticipant : fParticipants) {
				//Validate Participant Id
				String validateResult = validateEmptyInput(newParticipant.getId());
				if (null != validateResult) {
					//Validation of input failed
					final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_ERROR,
							"No input given for Participant Id", new Status(IStatus.ERROR, R4EUIPlugin.PLUGIN_ID, 0,
									validateResult, null), IStatus.ERROR);
					dialog.open();
					return;
				}

				//Validate Participant Email
				validateResult = validateEmptyInput(newParticipant.getEmail());
				if (null != validateResult) {
					//Validation of input failed
					final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_ERROR,
							"No Email given for Participant " + newParticipant.getId(), new Status(IStatus.ERROR,
									R4EUIPlugin.PLUGIN_ID, 0, validateResult, null), IStatus.ERROR);
					dialog.open();
					return;
				}
				if (!CommandUtils.isEmailValid(newParticipant.getEmail())) {
					return;
				}

				//Check if participant already exists (if so ignore but continue)
				R4EParticipant currentParticipant = null;
				if (null != R4EUIModelController.getActiveReview()) { //do not do this for participants lists
					try {
						currentParticipant = R4EUIModelController.getActiveReview().getParticipant(
								newParticipant.getId(), false);
					} catch (ResourceHandlingException e) {
						// ignore
					}
					if (fReviewSource && R4EUIModelController.getActiveReview().isParticipant(newParticipant.getId())
							&& null != currentParticipant && currentParticipant.isEnabled()) {
						final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_ERROR,
								"Cannot Add Participant " + newParticipant.getId(), new Status(IStatus.ERROR,
										R4EUIPlugin.PLUGIN_ID, 0, "Participant already part of this Review", null),
								IStatus.ERROR);
						dialog.open();
						continue;
					}

					//Validate Roles (optional)
					if (0 == newParticipant.getRoles().size()) {
						//If there is no roles defined, put one as default depending on the review type
						if (R4EUIModelController.getActiveReview()
								.getReview()
								.getType()
								.equals(R4EReviewType.BASIC)) {
							newParticipant.getRoles().add(R4EUserRole.LEAD);
						} else {
							newParticipant.getRoles().add(R4EUserRole.REVIEWER);
						}
					}
				}
				validatedParticipants.add(newParticipant);
			}
			//Set the participant list to include only the validated participants
			fParticipants = validatedParticipants;
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Method configureShell.
	 * 
	 * @param shell
	 *            Shell
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ADD_PARTICIPANT_DIALOG_TITLE);
		shell.setMinimumSize(R4EUIConstants.DIALOG_DEFAULT_WIDTH, R4EUIConstants.DIALOG_DEFAULT_HEIGHT);
	}

	/**
	 * Method open.
	 * 
	 * @see org.eclipse.ui.forms.FormDialog#open()
	 */
	@Override
	public void create() {
		fParticipantsDetailsValues.clear();
		fParticipants.clear();
		super.create();
	}

	/**
	 * Method open.
	 * 
	 * @return int
	 * @see org.eclipse.ui.forms.FormDialog#open()
	 */
	@Override
	public int open() {
		return super.open();
	}

	/**
	 * Method close.
	 * 
	 * @return int
	 * @see org.eclipse.ui.forms.FormDialog#close()
	 */
	@Override
	public boolean close() {
		return super.close();
	}

	/**
	 * Configures the dialog form and creates form content. Clients should override this method.
	 * 
	 * @param mform
	 *            the dialog form
	 */
	@Override
	protected void createFormContent(final IManagedForm mform) {

		final FormToolkit toolkit = mform.getToolkit();
		final ScrolledForm sform = mform.getForm();
		sform.setExpandVertical(true);
		final Composite composite = sform.getBody();
		final GridLayout layout = new GridLayout(4, false);
		composite.setLayout(layout);

		createParticipantsGroup(toolkit, composite);
		createBasicParameters(toolkit, composite);

		//Set default focus
		fUserToAddCombo.setFocus();
	}

	/**
	 * method createParticipantsGroup
	 * 
	 * @param aToolkit
	 * @param aComposite
	 */
	private void createParticipantsGroup(FormToolkit aToolkit, Composite aComposite) {
		//Users to Add
		final Group usersToAddGroup = new Group(aComposite, SWT.NONE);
		usersToAddGroup.setText(PARTICIPANTS_GROUP_LABEL);
		usersToAddGroup.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		usersToAddGroup.setLayout(new GridLayout(4, false));
		final GridData groupGridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		usersToAddGroup.setLayoutData(groupGridData);

		//Participants data composite
		final Composite dataComposite = aToolkit.createComposite(usersToAddGroup);
		dataComposite.setLayout(new GridLayout());
		final GridData dataCompositeData = new GridData(GridData.FILL, GridData.FILL, true, true);
		dataCompositeData.horizontalSpan = 3;
		dataComposite.setLayoutData(dataCompositeData);

		//Participants button composite
		final Composite buttonsComposite = aToolkit.createComposite(usersToAddGroup);
		buttonsComposite.setLayout(new GridLayout());
		final GridData buttonsCompositeData = new GridData(GridData.END, GridData.FILL, false, true);
		buttonsComposite.setLayoutData(buttonsCompositeData);

		final String[] participantsLists = R4EPreferencePage.getParticipantsLists();
		if (participantsLists.length > 0) {
			fUserToAddCombo = new CCombo(dataComposite, SWT.SINGLE | SWT.BORDER);
			((CCombo) fUserToAddCombo).setItems(participantsLists);
			((CCombo) fUserToAddCombo).setEditable(true);
		} else {
			fUserToAddCombo = aToolkit.createText(dataComposite, "", SWT.SINGLE | SWT.BORDER);
			((Text) fUserToAddCombo).setEditable(true);
		}
		final GridData textGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		fUserToAddCombo.setToolTipText(R4EUIConstants.PARTICIPANT_ADD_USER_TOOLTIP);
		fUserToAddCombo.setLayoutData(textGridData);
		fUserToAddCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				String widgetStr = null;
				if (fUserToAddCombo instanceof CCombo) {
					widgetStr = ((CCombo) fUserToAddCombo).getText();
				} else {
					widgetStr = ((Text) fUserToAddCombo).getText().trim();
				}
				if (widgetStr.trim().length() > 0) {
					fAddUserButton.setEnabled(true);
				} else {
					fAddUserButton.setEnabled(false);
				}
			}
		});
		//Trap \R key (Return)
		fUserToAddCombo.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.character == SWT.CR) {
					getShell().setCursor(getShell().getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

					String widgetStr = null;
					if (fUserToAddCombo instanceof CCombo) {
						widgetStr = ((CCombo) fUserToAddCombo).getText();
					} else {
						widgetStr = ((Text) fUserToAddCombo).getText();
					}

					//Tokenize the users list
					final String[] users = widgetStr.split(R4EUIConstants.LIST_SEPARATOR);
					for (String user : users) {
						addUsersToParticipantList(user);
					}
					if (fUserToAddCombo instanceof CCombo) {
						((CCombo) fUserToAddCombo).setText("");
					} else {
						((Text) fUserToAddCombo).setText("");
					}
					getShell().setCursor(getShell().getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
				}
			}
		});

		//Participants table
		final Composite tableComposite = aToolkit.createComposite(dataComposite);
		final GridData tableCompositeData = new GridData(GridData.FILL, GridData.FILL, true, true);
		fAddedParticipantsTable = aToolkit.createTable(tableComposite, SWT.FULL_SELECTION | SWT.BORDER);
		fAddedParticipantsTable.setHeaderVisible(true);
		fAddedParticipantsTable.setLinesVisible(true);
		fAddedParticipantsTable.setToolTipText(R4EUIConstants.PARTICIPANTS_ADD_TOOLTIP);
		fAddedParticipantsTable.setLinesVisible(true);
		fAddedParticipantsTable.setItemCount(0);
		final GridData tableGridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		fAddedParticipantsTable.setLayoutData(tableGridData);

		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		final TableColumn idColumn = new TableColumn(fAddedParticipantsTable, SWT.NONE, 0);
		idColumn.setText(R4EUIConstants.ID_LABEL);
		final TableColumn emailColumn = new TableColumn(fAddedParticipantsTable, SWT.NONE, 1);
		emailColumn.setText(R4EUIConstants.EMAIL_LABEL);

		tableColumnLayout.setColumnData(idColumn, new ColumnWeightData(30, idColumn.getWidth() * 2, true));
		tableColumnLayout.setColumnData(emailColumn, new ColumnWeightData(70, emailColumn.getWidth(), true));

		tableComposite.setLayout(tableColumnLayout);
		tableComposite.setLayoutData(tableCompositeData);

		fAddedParticipantsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				fSelectedParticipantIndex = fAddedParticipantsTable.getSelectionIndex();
				if (fSelectedParticipantIndex >= 0) {
					final R4EParticipant participant = fParticipants.get(fSelectedParticipantIndex);
					if (null != participant) {
						fParticipantIdInputTextField.setText(participant.getId());
						if (null != participant.getEmail()) {
							fParticipantEmailInputTextField.setText(participant.getEmail());
						} else {
							fParticipantEmailInputTextField.setText("");
						}
						if (fSelectedParticipantIndex < fParticipantsDetailsValues.size()) {
							fParticipantDetailsInputTextField.setText(fParticipantsDetailsValues.get(fSelectedParticipantIndex));
						} else {
							fParticipantDetailsInputTextField.setText("");
						}
					}

					//Make sure fields are enabled
					fParticipantIdInputTextField.setEnabled(true);
					fParticipantEmailInputTextField.setEnabled(true);
					fParticipantDetailsInputTextField.setEnabled(true);
				}
			}
		});

		//Add user button
		fAddUserButton = aToolkit.createButton(buttonsComposite, ADD_BUTTON_LABEL, SWT.NONE);
		final GridData addButtonGridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		fAddUserButton.setEnabled(false);
		fAddUserButton.setToolTipText(R4EUIConstants.PARTICIPANT_ADD_USER_TOOLTIP);
		fAddUserButton.setLayoutData(addButtonGridData);
		fAddUserButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				getShell().setCursor(getShell().getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

				String widgetStr = null;
				if (fUserToAddCombo instanceof CCombo) {
					widgetStr = ((CCombo) fUserToAddCombo).getText();
				} else {
					widgetStr = ((Text) fUserToAddCombo).getText();
				}

				//Tokenize the users list
				final String[] users = widgetStr.split(R4EUIConstants.LIST_SEPARATOR);
				for (String user : users) {
					addUsersToParticipantList(user);
				}
				getShell().setCursor(getShell().getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
			}
		});

		fFindUserButton = aToolkit.createButton(buttonsComposite, FIND_BUTTON_LABEL, SWT.NONE);
		final GridData findButtonGridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		fFindUserButton.setToolTipText(R4EUIConstants.PARTICIPANT_FIND_USER_TOOLTIP);
		fFindUserButton.setLayoutData(findButtonGridData);
		fFindUserButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (R4EUIModelController.isUserQueryAvailable()) {
					final IFindUserDialog dialog = R4EUIDialogFactory.getInstance().getFindUserDialog();
					dialog.create();
					dialog.setDialogsDefaults();
					final int result = dialog.open();
					if (result == Window.OK) {
						final List<IUserInfo> usersInfos = dialog.getUserInfos();
						for (IUserInfo user : usersInfos) {
							addUserToParticipantList(user);
						}
					}
				} else {
					R4EUIPlugin.Ftracer.traceWarning(LDAP_NOT_CONFIGURED);
					final ErrorDialog dialog = new ErrorDialog(null, R4EUIConstants.DIALOG_TITLE_WARNING,
							LDAP_NOT_CONFIGURED, new Status(IStatus.WARNING, R4EUIPlugin.PLUGIN_ID, 0,
									LDAP_NOT_CONFIGURED_DETAILED, null), IStatus.WARNING);
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							dialog.open();
						}
					});
				}
			}
		});

		fRemoveUserButton = aToolkit.createButton(buttonsComposite, REMOVE_BUTTON_LABEL, SWT.NONE);
		final GridData removeButtonGridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		fRemoveUserButton.setEnabled(false);
		fRemoveUserButton.setToolTipText(R4EUIConstants.PARTICIPANT_REMOVE_TOOLTIP);
		fRemoveUserButton.setLayoutData(removeButtonGridData);
		if (!R4EUIModelController.isUserQueryAvailable()) {
			fRemoveUserButton.setEnabled(false);
		}
		fRemoveUserButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (fSelectedParticipantIndex >= 0
						&& fSelectedParticipantIndex < fAddedParticipantsTable.getItemCount()) {
					fAddedParticipantsTable.remove(fSelectedParticipantIndex);
					fParticipants.remove(fSelectedParticipantIndex);
					fParticipantsDetailsValues.remove(fSelectedParticipantIndex);
					clearParametersFields();
				}
			}
		});

		//Clear participants list button
		fClearParticipantsButton = aToolkit.createButton(buttonsComposite, CLEAR_BUTTON_LABEL, SWT.NONE);
		final GridData clearButtonGridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		fClearParticipantsButton.setEnabled(false);
		fClearParticipantsButton.setToolTipText(R4EUIConstants.PARTICIPANTS_CLEAR_TOOLTIP);
		fClearParticipantsButton.setLayoutData(clearButtonGridData);
		fClearParticipantsButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				fParticipants.clear();
				fParticipantsDetailsValues.clear();
				fAddedParticipantsTable.removeAll();
				fSelectedParticipantIndex = R4EUIConstants.INVALID_VALUE;
				clearParametersFields();
			}
		});
	}

	/**
	 * method createBasicParameters
	 * 
	 * @param aToolkit
	 * @param aComposite
	 */
	private void createBasicParameters(FormToolkit aToolkit, Composite aComposite) {

		//Basic parameters section
		final Section basicSection = aToolkit.createSection(aComposite, Section.DESCRIPTION
				| ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		final GridData basicSectionGridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		basicSectionGridData.horizontalSpan = 4;
		basicSection.setLayoutData(basicSectionGridData);
		basicSection.setText(R4EUIConstants.BASIC_PARAMS_HEADER);
		basicSection.setDescription(R4EUIConstants.BASIC_PARAMS_HEADER_DETAILS + PARTICIPANT_LABEL);
		basicSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				getShell().setSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		final Composite basicSectionClient = aToolkit.createComposite(basicSection);
		final GridLayout layout = new GridLayout(4, false);
		basicSectionClient.setLayout(layout);
		basicSection.setClient(basicSectionClient);

		//Participant Id
		Label label = aToolkit.createLabel(basicSectionClient, R4EUIConstants.ID_LABEL);
		label.setToolTipText(R4EUIConstants.PARTICIPANT_ID_TOOLTIP);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		aToolkit.setBorderStyle(SWT.NULL);
		fParticipantIdInputTextField = aToolkit.createText(basicSectionClient, "");
		GridData textGridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		textGridData.horizontalSpan = 3;
		fParticipantIdInputTextField.setEnabled(false);
		fParticipantIdInputTextField.setEditable(false);
		fParticipantIdInputTextField.setToolTipText(R4EUIConstants.PARTICIPANT_ID_TOOLTIP);
		fParticipantIdInputTextField.setLayoutData(textGridData);

		//Participant Email
		label = aToolkit.createLabel(basicSectionClient, R4EUIConstants.EMAIL_LABEL);
		label.setToolTipText(R4EUIConstants.PARTICIPANT_EMAIL_TOOLTIP);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		aToolkit.setBorderStyle(SWT.BORDER);
		fParticipantEmailInputTextField = aToolkit.createText(basicSectionClient, "", SWT.SINGLE);
		textGridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		textGridData.horizontalSpan = 3;
		fParticipantEmailInputTextField.setToolTipText(R4EUIConstants.PARTICIPANT_EMAIL_TOOLTIP);
		fParticipantEmailInputTextField.setLayoutData(textGridData);
		fParticipantEmailInputTextField.setEnabled(false);
		fParticipantEmailInputTextField.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event event) {
				if (fSelectedParticipantIndex >= 0) {
					final R4EParticipant participant = fParticipants.get(fSelectedParticipantIndex);
					participant.setEmail(fParticipantEmailInputTextField.getText().trim());
					final TableItem item = fAddedParticipantsTable.getItem(fSelectedParticipantIndex);
					if (null != participant.getEmail()) {
						item.setFont(JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT));
						item.setText(1, participant.getEmail());
					} else {
						item.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));
					}
				}
			}
		});

		//User details
		label = aToolkit.createLabel(basicSectionClient, R4EUIConstants.USER_DETAILS_LABEL);
		label.setToolTipText(R4EUIConstants.PARTICIPANT_DETAILS_TOOLTIP);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		aToolkit.setBorderStyle(SWT.NULL);
		fParticipantDetailsInputTextField = aToolkit.createText(basicSectionClient, "", SWT.MULTI | SWT.V_SCROLL
				| SWT.READ_ONLY);
		textGridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		textGridData.horizontalSpan = 3;
		textGridData.heightHint = fParticipantDetailsInputTextField.getLineHeight() << 3;
		fParticipantDetailsInputTextField.setEnabled(false);
		fParticipantDetailsInputTextField.setEditable(false);
		fParticipantDetailsInputTextField.setToolTipText(R4EUIConstants.PARTICIPANT_DETAILS_TOOLTIP);
		fParticipantDetailsInputTextField.setLayoutData(textGridData);
	}

	/**
	 * Configures the button bar.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return Control
	 */
	@Override
	protected Control createButtonBar(Composite parent) {
		final Control bar = super.createButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return bar;
	}

	/**
	 * Method isResizable.
	 * 
	 * @return boolean
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Method validateEmptyInput.
	 * 
	 * @param aText
	 *            Text
	 * @return String
	 */
	private String validateEmptyInput(String aText) {
		return fValidator.isValid(aText);
	}

	/**
	 * Method getParticpants.
	 * 
	 * @return List<R4EParticipant>
	 */
	public List<R4EParticipant> getParticipants() {
		return fParticipants;
	}

	/**
	 * Method clearParametersFields.
	 */
	private void clearParametersFields() {
		fParticipantIdInputTextField.setText("");
		fParticipantEmailInputTextField.setText("");
		fParticipantDetailsInputTextField.setText("");
		if (0 == fParticipants.size()) {
			fParticipantDetailsInputTextField.setEnabled(false);
			fParticipantIdInputTextField.setEnabled(false);
			fParticipantEmailInputTextField.setEnabled(false);
			fClearParticipantsButton.setEnabled(false);
			fRemoveUserButton.setEnabled(false);
			if (fReviewSource) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
	}

	/**
	 * Method addUserToParticipantList.
	 * 
	 * @param aUser
	 *            - String
	 */
	private void addUsersToParticipantList(String aUser) {
		//Here we first need to resolve any group aliases to multiple users
		final List<String> resolvedUsers = R4EPreferencePage.getParticipantsFromList(aUser.trim());

		for (String user : resolvedUsers) {
			String[] userTokens = user.split(R4EUIConstants.LIST_SEPARATOR);

			//Resolve user in database (if possible)
			R4EParticipant participant = getParticipant(userTokens[0].toLowerCase());
			if (null == participant) {
				return; //Participant already in list
			}

			//Override email with address defined in users group (if any)
			if (2 == userTokens.length && !(userTokens[1].trim().equals(""))) {
				participant.setEmail(userTokens[1]);
			}

			//Override email with address defined in current review (if any)
			if (null != R4EUIModelController.getActiveReview()) {
				R4EParticipant reviewParticipant;
				try {
					reviewParticipant = R4EUIModelController.getActiveReview().getParticipant(participant.getId(),
							false);
					if (null != reviewParticipant) {
						participant.setEmail(reviewParticipant.getEmail());
					}
				} catch (ResourceHandlingException e) {
					UIUtils.displayResourceErrorDialog(e);
				}
			}
			fParticipants.add(participant);
			updateComponents(participant);
		}
	}

	/**
	 * Method addUserToParticipantList.
	 * 
	 * @param aUserInfo
	 *            - IUserInfo
	 */
	private void addUserToParticipantList(IUserInfo aUserInfo) {

		//First check if the participant already exist in the participant list
		for (R4EParticipant tmpPart : fParticipants) {
			if (aUserInfo.getUserId().equalsIgnoreCase(tmpPart.getId())) {
				return;
			}
		}

		//Add User to List
		final R4EParticipant participant = RModelFactory.eINSTANCE.createR4EParticipant();
		participant.setId(aUserInfo.getUserId());
		participant.setEmail(aUserInfo.getEmail());
		fParticipantsDetailsValues.add(UIUtils.buildUserDetailsString(aUserInfo));
		fParticipants.add(participant);

		updateComponents(participant);

	}

	/**
	 * Method updateComponents.
	 * 
	 * @param aParticipant
	 *            - R4EParticipant
	 */
	private void updateComponents(R4EParticipant aParticipant) {
		//Add item to the participants table
		final TableItem item = new TableItem(fAddedParticipantsTable, SWT.NONE);
		item.setText(0, aParticipant.getId());
		if (null != aParticipant.getEmail() && !("".equals(aParticipant.getEmail()))) {
			item.setFont(JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT));
			item.setText(1, aParticipant.getEmail());
		} else {
			//Mark table item as not complete
			item.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));
		}
		fAddedParticipantsTable.showItem(item);

		if (fParticipants.size() > 0) {
			fClearParticipantsButton.setEnabled(true);
			fRemoveUserButton.setEnabled(true);
			if (fUserToAddCombo instanceof CCombo) {
				((CCombo) fUserToAddCombo).setText("");
			} else {
				((Text) fUserToAddCombo).setText("");
			}
			getButton(IDialogConstants.OK_ID).setEnabled(true);
			getButton(IDialogConstants.OK_ID).setSelection(false);
		} else {
			if (fReviewSource) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
	}

	/**
	 * Method getParticipant.
	 * 
	 * @param aId
	 *            - String
	 * @return R4EParticipant
	 */
	protected R4EParticipant getParticipant(String aId) {
		//First check if the participant already exist in the participant list
		for (R4EParticipant tmpPart : fParticipants) {
			if (aId.equalsIgnoreCase(tmpPart.getId())) {
				return null;
			}
		}
		final R4EParticipant participant = RModelFactory.eINSTANCE.createR4EParticipant();
		if (R4EUIModelController.isUserQueryAvailable()) {
			final IQueryUser query = new QueryUserFactory().getInstance();
			try {
				final List<IUserInfo> users = query.searchByUserId(aId);

				//Fill info with first user returned
				for (IUserInfo user : users) {
					if (user.getUserId().toLowerCase().equals(aId)) {
						participant.setId(user.getUserId().toLowerCase());
						participant.setEmail(user.getEmail());
						fParticipantsDetailsValues.add(UIUtils.buildUserDetailsString(user));
						return participant;
					}
				}
			} catch (NamingException e) {
				R4EUIPlugin.Ftracer.traceError("Exception: " + e.toString() + " (" + e.getMessage() + ")");
				R4EUIPlugin.getDefault().logError("Exception: " + e.toString(), e);
			} catch (IOException e) {
				R4EUIPlugin.getDefault().logWarning("Exception: " + e.toString(), e);
			}
		}
		participant.setId(aId);
		fParticipantsDetailsValues.add("");
		return participant;
	}

	/**
	 * Method itemsUpdated.
	 * 
	 * @param aItems
	 *            Item[]
	 * @param aInstanceId
	 *            int
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.utils.IEditableListListener#itemsUpdated(Item[], int)
	 */
	public void itemsUpdated(Item[] aItems, int aInstanceId) {
		if (fSelectedParticipantIndex >= 0) {
			final R4EParticipant participant = fParticipants.get(fSelectedParticipantIndex);
			if (0 == aInstanceId) {
				//Update roles
				participant.getRoles().clear();
				for (Item item : aItems) {
					R4EUserRole role = R4EUIParticipant.mapStringToRole(item.getText());
					if (null != role) {
						participant.getRoles().add(role);
					}
				}
			}
		}
	}

	/**
	 * This is done to change the default button behavior of the OK button
	 * 
	 * @param parent
	 *            the button bar composite
	 * @see org.eclipse.jface.dialogs#createButtonsForButtonBar(Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Method itemSelected.
	 * 
	 * @param aItem
	 *            Item
	 * @param aInstanceId
	 *            int
	 * @see org.eclipse.mylyn.reviews.r4e.ui.internal.utils.IEditableListListener#itemSelected(Item, int)
	 */
	public void itemSelected(Item aItem, int aInstanceId) {
		// ignore

	}

	/**
	 * Method addParticipant.
	 * 
	 * @param aParticipant
	 *            - String
	 */
	public void addParticipant(String aParticipant) {
		addUsersToParticipantList(aParticipant);
	}
}
