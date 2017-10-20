// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2014 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.crt.ChineseRemainderTheoremPlugin;
import org.jcryptool.visual.crt.export.FileExporter;

/**
 * @author Oryal Inel
 * @author Holger Friedrich (support for Commands)
 * @version 1.0.1
 */
public class ChineseRemainderTheoremView extends ViewPart implements Constants {
	public ChineseRemainderTheoremView() {
	}

	private final String exportToLatexCommandId = "org.jcryptool.visual.crt.commands.exportToLatex";
	private final String exportToCSVCommandId = "org.jcryptool.visual.crt.commands.exportToCsv";
	private final String exportToPdfCommandId = "org.jcryptool.visual.crt.commands.exportToPdf";
	
    private AbstractHandler exportToLatexHandler;
    private AbstractHandler exportToCSVHandler;
    private AbstractHandler exportToPdfHandler;

    private StackLayout layout;
    private CRTGroup CRTGroup;
    private Composite parent;
    private boolean reset = false;
    
    private ICommandService commandService;
    private Category autogeneratedCategory;
    private IServiceLocator serviceLocator;

    /**
     * Create contents of the view part
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;

        layout = new StackLayout();
        parent.setLayout(layout);

        CRTGroup = new CRTGroup(parent, SWT.NONE, this);
        layout.topControl = CRTGroup;

        if (!reset) {
            defineAllCommands();
            initializeMenu();
        }

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent.getShell(), ChineseRemainderTheoremPlugin.PLUGIN_ID + ".view");
    }

    private void defineCommand(final String commandId, final String name, AbstractHandler handler) {
    	Command command = commandService.getCommand(commandId);
    	command.define(name,  null, autogeneratedCategory);
    	command.setHandler(handler);
    }
    
    /**
     * Define the Commands
     */
    private void defineAllCommands() {
    	serviceLocator = PlatformUI.getWorkbench();
        commandService = (ICommandService)serviceLocator.getService(ICommandService.class);
        autogeneratedCategory = commandService.getCategory(CommandManager.AUTOGENERATED_CATEGORY_ID);

        defineCommand(exportToPdfCommandId, MESSAGE_EXPORT_PDF, null);	// command initially disabled
        exportToPdfHandler = new AbstractHandler() {
            @Override
            public Object execute(ExecutionEvent event) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterExtensions(new String[] {IConstants.PDF_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.PDF_FILTER_NAME});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);
                String filename = dialog.open();

                if (filename != null) {
                    FileExporter pdfExport = new FileExporter(CRTGroup.getCrt(), filename);
                    pdfExport.exportToPDF();
                }
                return(null);
            }
        };

        defineCommand(exportToCSVCommandId, MESSAGE_EXPORT_CSV, null);	// command initially disabled
        exportToCSVHandler = new AbstractHandler() {
            @Override
            public Object execute(ExecutionEvent event) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterExtensions(new String[] {IConstants.CSV_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.CSV_FILTER_NAME});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);
                String filename = dialog.open();

                if (filename != null) {
                    FileExporter csvExport = new FileExporter(CRTGroup.getCrt(), filename);
                    csvExport.exportToCSV();
                }
                return(null);
            }
        };

        defineCommand(exportToLatexCommandId, MESSAGE_EXPORT_LATEX, null);	// command initially disabled
        exportToLatexHandler = new AbstractHandler() {
            @Override
            public Object execute(ExecutionEvent event) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterExtensions(new String[] {IConstants.TEX_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.TEX_FILTER_NAME});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);
                String filename = dialog.open();

                if (filename != null) {
                    FileExporter latexExport = new FileExporter(CRTGroup.getCrt(), filename);
                    latexExport.exportToLatex();
                }
                return(null);
            }
        };
        // Define the Commands
    }

    private void addContributionItem(IContributionManager manager, final String commandId,
        	final ImageDescriptor icon, final String tooltip)
        {
        	CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator,
        		null, commandId, SWT.PUSH);
        	if(icon != null)
        		param.icon = icon;
        	if(tooltip != null && !tooltip.equals(""))
        		param.tooltip = tooltip;
        	CommandContributionItem item = new CommandContributionItem(param);
        	manager.add(item);
        }
        
    /**
     * Initialize the menu
     */
    private void initializeMenu() {
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();

        addContributionItem(menuManager, exportToPdfCommandId, null, null);
        addContributionItem(menuManager, exportToCSVCommandId, null, null);
        addContributionItem(menuManager, exportToLatexCommandId, null, null);
    }

    public void enableMenu(boolean enable) {
    	Command exportToPdfCommand = commandService.getCommand(exportToPdfCommandId);
    	Command exportToLatexCommand = commandService.getCommand(exportToLatexCommandId);
    	Command exportToCSVCommand = commandService.getCommand(exportToCSVCommandId);
    	if(enable) {
    		exportToPdfCommand.setHandler(exportToPdfHandler);
    		exportToLatexCommand.setHandler(exportToLatexHandler);
    		exportToCSVCommand.setHandler(exportToCSVHandler);
    	} else {
    		exportToPdfCommand.setHandler(null);
    		exportToLatexCommand.setHandler(null);
    		exportToCSVCommand.setHandler(null);
    	}
    }

    public void reset() {
        layout = null;
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
        reset = true;
    }

    @Override
    public void setFocus() {
    }

}
