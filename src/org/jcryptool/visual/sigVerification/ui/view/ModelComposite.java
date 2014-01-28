package org.jcryptool.visual.sigVerification.ui.view;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.Messages;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.cert.CertGeneration;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Canvas;

/**
 * This class contains all the code required for the design and functionality of the verification
 * model view.
 * 
 */
public class ModelComposite extends Composite {
    private Label lblGeneralDescription;
    private Text lblHeader;
    private Text lblTitle;
    private Button btnShellM;
    private Button btnChainM;
    private Label lblRoot;
    private Label lbllevel2;
    private Label lbllevel3;
    private Text lblrootChoose;
    private Text lbllevel2Choose;
    private Text lbllevel3Choose;
    private Button btnNewResult;
    private Text lblChoose;
    private CertGeneration Certificates;
    private Label lblDate;
    private Text textValidDate;
	private String now;
	private String dateRoot;
	private String dateLevel2;
	private String dateUser;
	private Date temp;
	private Date changeTest;
	private Date changeRoot;
	private Date changeLevel2;
	private Date changeUser;
	private Canvas canvas;
	private Label lblResult;
    
    
    public ModelComposite(Composite parent, int style, SigVerView sigVerView) {
        super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        createContents(parent);
        createActions();

        // Adds reset button to the Toolbar
         IToolBarManager toolBarMenu = sigVerView.getViewSite().getActionBars().getToolBarManager();
         Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {reset(0);}}; //$NON-NLS-1$
         action.setImageDescriptor(SigVerificationPlugin.getImageDescriptor("icons/reset.gif")); //$NON-NLS-1$
         toolBarMenu.add(action);
         
    }


    /**
     * Create contents of the application window.
     * 
     * @param parent
     */
    private void createContents(Composite parent) {
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        parent.setLayout(null);
        setLayout(null);
        
        Certificates = new CertGeneration();
        try {
			Certificates.setRoot(Certificates.createCertificate(1));
			Certificates.setLevel2(Certificates.createCertificate(2));
			Certificates.setUser(Certificates.createCertificate(3));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        {
            lblGeneralDescription = new Label(this, SWT.NONE);
            lblGeneralDescription.setBounds(10, 37, 964, 78);
            lblGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
            lblGeneralDescription.setText(Messages.ModelComposite_description);

        }
        lblHeader = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblHeader.setEditable(false);
        lblHeader.setBounds(10, 10, 964, 31);
        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
        lblHeader.setText(Messages.ModelComposite_lblHeader);
        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));

        lblTitle = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblTitle.setEditable(false);
        lblTitle.setBounds(20, 121, 216, 27);
        lblTitle.setText(Messages.ModelComposite_lblTitle);
        {
            Composite border = new Composite(this, SWT.BORDER);
            border.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
            border.setBounds(10, 132, 964, 560);
            {
                btnShellM = new Button(border, SWT.NONE);
                btnShellM.setBounds(293, 10, 180, 36);
                btnShellM.setText(Messages.ModelComposite_btnShellM);
            }
            {
                btnChainM = new Button(border, SWT.NONE);
                btnChainM.setEnabled(false);
                btnChainM.setBounds(470, 10, 171, 36);
                btnChainM.setText(Messages.ModelComposite_btnChainM);

            }
            {
                lblRoot = new Label(border, SWT.NONE);
                lblRoot.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lblRoot.setBounds(64, 133, 380, 67);
                lblRoot.setText(Messages.ModelComposite_lblroot);
            }
            {
                lbllevel2 = new Label(border, SWT.NONE);
                lbllevel2.setText(Messages.ModelComposite_lbllevel2);
                lbllevel2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lbllevel2.setBounds(103, 219, 341, 67);
            }
            {
                lbllevel3 = new Label(border, SWT.NONE);
                lbllevel3.setText(Messages.ModelComposite_lbllevel3);
                lbllevel3.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lbllevel3.setBounds(145, 304, 299, 67);
            }
            temp=Certificates.getRoot().getNotAfter();
        	dateRoot=setFormat(temp);
            {
                lblrootChoose = new Text(border, SWT.BORDER);
                lblrootChoose.setEditable(true);
                lblrootChoose.setText(dateRoot);
                lblrootChoose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lblrootChoose.setBounds(773, 133, 146, 67);
            }
//            lblrootChoose.addModifyListener(new ModifyListener() {
//                @Override
//                public void modifyText(ModifyEvent e) {
//                    if (lblrootChoose.getText().length() > 0) {
//                    	String temp=new String(lblrootChoose.getText());
//                    	changeRoot=toDate(temp);
//                    	for(int i=0;i<temp.length();i++)
//                    			btnNewResult.setEnabled(true);    
//                    }
//                }
//            });}
            temp=Certificates.getLevel2().getNotAfter();
        	dateLevel2=setFormat(temp);
            {
                lbllevel2Choose = new Text(border, SWT.BORDER);
                lbllevel2Choose.setEditable(true);
                lbllevel2Choose.setText(dateLevel2);
                lbllevel2Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lbllevel2Choose.setBounds(773, 219, 146, 67);
            }
            temp=Certificates.getUser().getNotAfter();
        	dateUser=setFormat(temp);
            {
                lbllevel3Choose = new Text(border, SWT.BORDER);
                lbllevel3Choose.setEditable(true);
                lbllevel3Choose.setText(dateUser);
                lbllevel3Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                lbllevel3Choose.setBounds(773, 304, 146, 67);
            }
            {
                btnNewResult = new Button(border, SWT.NONE);
                btnNewResult.setEnabled(false);
                btnNewResult.setBounds(293, 515, 322, 31);
                btnNewResult.setText(Messages.ModelComposite_btnNewResult);
            }

            Button btnReset = new Button(border, SWT.NONE);
            btnReset.setLocation(829, 515);
            btnReset.setSize(90, 30);
            btnReset.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                	
                }
            });
            btnReset.setText(Messages.SigVerComposite_btnReset);
            {
                lblChoose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                lblChoose.setEditable(false);
                lblChoose.setText(Messages.ModelComposite_Choose);
                lblChoose.setBounds(719, 70, 200, 50);
            }
            temp=Certificates.getNow();
        	now=setFormat(temp);
            {
            	textValidDate = new Text(border, SWT.BORDER);
            	textValidDate.setEditable(true);
            	textValidDate.setText(now);
            	textValidDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                textValidDate.setBounds(402, 453, 146, 36);
            }
            {
            	lblResult = new Label(border, SWT.NONE);
            	lblResult.setBounds(275, 395, 121, 107);
            }
//            boolean verify=Certificates.verify(Certificates.getNow());
//            System.out.println(verify);
        	}
        }

            	
            
        
    
    @SuppressWarnings("deprecation")
	private Date toDate(String string){
    	Date date=new Date();
    	char[] temp=string.toCharArray();
    	int i=1;
    	int value;
    	
    	for(;i<string.length();i++){
    		if(i<=1){//day
    			value=((temp[i-1]-48)*10)+(temp[i]-48);
    			date.setDate(value);
    		}else if(i==4){//month
    			value=(((temp[i-1]-48)*10)+(temp[i]-48)-1);
    			date.setMonth(value);
    		}else if(i==9){//year
    			value=(((temp[i-3]-48)*1000)+((temp[i-2]-48)*100)+((temp[i-1]-48)*10)+(temp[i]-48)-1900);
    			date.setYear(value);
    		}else if(i==12){//hours
    			value=((temp[i-1]-48)*10)+(temp[i]-48);
    			date.setHours(value);
    		}else if(i==15){//minutes
    			value=((temp[i-1]-48)*10)+(temp[i]-48);
    			date.setMinutes(value);
    		}
    	}
    	
    	return date;
    }

    private void createActions() {
    	 textValidDate.addModifyListener(new ModifyListener() {
             public void modifyText(ModifyEvent e) {
                 if (textValidDate.getText().length() > 0) {
                 	String temp=new String(textValidDate.getText());
                 	changeTest=toDate(temp);
                 	for(int i=0;i<temp.length();i++)
                 			btnNewResult.setEnabled(true);    
                 }
             }
         });
    	 
    	 lblrootChoose.addModifyListener(new ModifyListener() {
             public void modifyText(ModifyEvent e) {
                 if (lblrootChoose.getText().length() > 0) {
                 	String temp=new String(lblrootChoose.getText());
                 	changeRoot=toDate(temp);
                 	for(int i=0;i<temp.length();i++)
                 			btnNewResult.setEnabled(true);    
                 }
             }
         });
    	 
    	 lbllevel2Choose.addModifyListener(new ModifyListener() {
             public void modifyText(ModifyEvent e) {
                 if (lbllevel2Choose.getText().length() > 0) {
                 	String temp=new String(lbllevel2Choose.getText());
                 	changeLevel2=toDate(temp);
                 	for(int i=0;i<temp.length();i++)
                 			btnNewResult.setEnabled(true);    
                 }
             }
         });
    	 
    	 lbllevel3Choose.addModifyListener(new ModifyListener() {
             public void modifyText(ModifyEvent e) {
                 if (lbllevel3Choose.getText().length() > 0) {
                 	String temp=new String(lbllevel3Choose.getText());
                 	changeUser=toDate(temp);
                 	for(int i=0;i<temp.length();i++)
                 			btnNewResult.setEnabled(true);    
                 }
             }
         });
    	 
    	 	
     	btnNewResult.addSelectionListener(new SelectionAdapter(){
     		public void widgetSelected(SelectionEvent e) {
     			
                 try {
                 	boolean verify = false;
                      reset(0);
                      
                      if(changeTest!=null){
                     	 verify=Certificates.verify(changeTest);
                      }else if(changeRoot!=null){
                      	 verify=Certificates.verify(changeTest);
                       }else if(changeLevel2!=null){
                       	 verify=Certificates.verify(changeTest);
                       }else if(changeUser!=null){
                       	 verify=Certificates.verify(changeTest);
                       }
                     
                      
                      if(verify==true){
                      	lblResult.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/gruenerHacken.png"));          	
                      }else{
                      	lblResult.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/rotesKreuz.png"));  
                      }
                         
                 } catch (Exception ex) {
                     LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                 }
        
             }
         });
    }
     
    

    private void reset(int step) {
        // If the user already finished other steps, reset everything to this
        // step (keep the chosen algorithms)
        switch (step) {
        case 0:
             btnNewResult.setEnabled(false);
        case 1:
            // btnDecrypt.setEnabled(false);
        case 2:
            // btnResult.setEnabled(false);
            break;
        default:
            break;
        }
    }
    
    @SuppressWarnings("deprecation")
	public String setFormat(Date date){
    	String result;
    	
    	if(date.getDate()<=9){
    		result="0"+date.getDate()+".";
    	}else{
    		result=date.getDate()+".";
    	}
    	if(date.getMonth()<=9){
    		result+="0"+(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
    	}else{
    		result+=(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
    	}
    	if(date.getHours()<=9){
    		result+="0"+date.getHours()+":";
    	}else{
    		result+=date.getHours()+":";
    	}
    	if(date.getMinutes()<=9){
    		result+="0"+date.getMinutes();
    	}else{
    		result+=date.getMinutes()+"";
    	}
    	
  	  return result;
    }

    /**
     * Create the menu manager.
     * 
     * @return the menu manager
     */
    /*
     * @Override protected MenuManager createMenuManager() { MenuManager menuManager = new
     * MenuManager("menu"); return menuManager; }
     */

    /**
     * Create the status line manager.
     * 
     * @return the status line manager
     */
    /*
     * @Override protected StatusLineManager createStatusLineManager() { StatusLineManager
     * statusLineManager = new StatusLineManager(); return statusLineManager; } /** Return the
     * initial size of the window.
     */
    /*
     * @Override protected Point getInitialSize() { return new Point(1270, 750); }
     */

}
