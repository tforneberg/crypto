package org.jcryptool.visual.sigVerification;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the main view)
 * 
 * @author Grebe/Wilfing
 */
public class Messages {
    // Name of the files that contain the string values
    private static final String BUNDLE_NAME = "org.jcryptool.visual.sigVerification.messages"; //$NON-NLS-1$
    // Create strings for all the values
    public static String SigComposite_grpSignatureGeneration;
    public static String SigComposite_grpSignedDoc;
    public static String SigComposite_lblHash;
    public static String SigComposite_lblSignature;
    public static String SigComposite_lblProgress;
    public static String SigComposite_tbtmNewItem_0;
    public static String SigComposite_tbtmNewItem_1;
    public static String SigComposite_tbtmNewItem_2;
    public static String SigComposite_tbtmNewItem_3;
    public static String SigComposite_txtDescriptionOfStep1;
    public static String SigComposite_txtDescriptionOfStep2;
    public static String SigComposite_txtDescriptionOfStep3;
    public static String SigComposite_txtDescriptionOfStep4;
    public static String SigComposite_btnSignature;
    public static String SigComposite_btnHash;
    public static String SigComposite_btnOpenInEditor;
    public static String SigComposite_btnReset;
    public static String SigComposite_btnReturn;
    public static String SigComposite_description;
    public static String SigComposite_btnChooseInput;
    public static String SigComposite_lblHeader;
    public static String SigComposite_MessageTitleReturn;
    public static String SigComposite_MessageTextReturn;
    public static String SigComposite_txtDescriptionOfStep4_Success;
    public static String SigComposite_menu;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}