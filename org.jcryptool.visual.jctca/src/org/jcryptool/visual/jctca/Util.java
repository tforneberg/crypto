package org.jcryptool.visual.jctca;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateViews.Messages;

import codec.x509.GeneralName;

public class Util {
	
	public static X509Certificate certificateForKeyPair(CSR csr, BigInteger serialNumber, X509Certificate caCert, Date expiryDate, Date startDate, PrivateKey caKey){
		KeyStoreManager mng = KeyStoreManager.getInstance();
		PublicKey pub = mng.getPublicKey(csr.getPubAlias()).getPublicKey(); 
        PrivateKey priv;
		try {
			priv = mng.getPrivateKey(csr.getPrivAlias(), mng.getDefaultKeyPassword());
			return Util.certificateForKeyPair(csr.getFirst() + " " + csr.getLast(), csr.getCountry(), csr.getStreet()+"/"+ csr.getTown(),"" ,"" , csr.getMail() ,pub, priv, serialNumber, caCert, expiryDate, startDate, caKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static X509Certificate certificateForKeyPair(String principal, String country, String street, String unit, String organisation, String mail, PublicKey pub, PrivateKey priv, BigInteger serialNumber, X509Certificate caCert, Date expiryDate, Date startDate, PrivateKey caKey){
		try {
			KeyPair keyPair = new KeyPair(pub, 
					                      priv);              // public/private key pair that we are creating certificate for
	
			X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
			X509Name subjectName = new X509Name("CN="+principal + ", " +//commonname
															"ST="+street+", "+ //street
															"C="+country + ", " +  //land
															"OU="+unit+", "+ //organisationseinheit
															"O="+organisation+", "+ //organisation
															"EMAILADDRESS=" + mail); //evtl SERIALNUMBER 
			//organisation
			//organisationseinheit
			//ort
			//land
			//mail
			
			certGen.setSerialNumber(serialNumber);
			if(caCert!=null){
				certGen.setIssuerDN(caCert.getSubjectX500Principal());
				certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false,new AuthorityKeyIdentifierStructure(caCert));
				certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false,new SubjectKeyIdentifierStructure(keyPair.getPublic()));
			}
			else{
				certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
				certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature| KeyUsage.keyEncipherment));
				certGen.addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));
			}
			certGen.setNotBefore(startDate);
			certGen.setNotAfter(expiryDate);
			certGen.setSubjectDN(subjectName);
			certGen.setPublicKey(keyPair.getPublic());
			certGen.setSignatureAlgorithm("SHA512withRSA");
			
			X509Certificate cert = certGen.generate(caKey, "BC"); 
			return cert;
		} catch (CertificateParsingException | InvalidKeyException | CertificateEncodingException | IllegalStateException | NoSuchProviderException | NoSuchAlgorithmException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		  // note: private key of CA
	}

	public static KeyPair asymmetricKeyPairToNormalKeyPair(AsymmetricCipherKeyPair keypair){
		RSAKeyParameters publicKey = (RSAKeyParameters) keypair.getPublic();
		RSAPrivateCrtKeyParameters privateKey = (RSAPrivateCrtKeyParameters) keypair.getPrivate();
		RSAPublicKey pkStruct = new RSAPublicKey(
				publicKey.getModulus(), publicKey.getExponent());
		// JCE format needed for the certificate - because
		// getEncoded() is necessary...
		PublicKey pubKey;
		try {
			pubKey = KeyFactory
					.getInstance("RSA")
					.generatePublic(
							new RSAPublicKeySpec(
									publicKey.getModulus(), publicKey
											.getExponent()));
			PrivateKey privKey = KeyFactory
					.getInstance(
							"RSA")
					.generatePrivate(
							new RSAPrivateCrtKeySpec(publicKey
									.getModulus(), publicKey
									.getExponent(), privateKey
									.getExponent(), privateKey.getP(),
									privateKey.getQ(), privateKey
											.getDP(), privateKey
											.getDQ(), privateKey
											.getQInv()));
			
			return new KeyPair(pubKey, privKey);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// and this one for the KeyStore
		return null;
	}
	public static void createRootNodes(Tree tree){
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText(Messages.ShowReq_CertReqs);
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText(Messages.ShowReq_RevReqs);

		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);
	}
	/**	
	 * Find all RSA and DSA public keys in a given keystore ksm and return them
	 * in an array of KeyStoreAlias
	 * 
	 * @param ksm
	 *            - KeyStoreManager from where to get the keys
	 * @return ArrayList of all KeyStoreAlias containing either RSA or DSA public key pairs
	 */
	public static ArrayList<KeyStoreAlias> getAllRSAAndDSAPublicKeys(
			KeyStoreManager ksm) {
		ArrayList<KeyStoreAlias> RSAAndDSAPublicKeys = new ArrayList<KeyStoreAlias>();
		for (KeyStoreAlias ksAlias : ksm.getAllPublicKeys()) {
			if (ksAlias.getOperation().contains("RSA") && (ksAlias.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY)) {
				RSAAndDSAPublicKeys.add(ksAlias);
			} else if (ksAlias.getOperation().contains("DSA") && (ksAlias.getKeyStoreEntryType() == KeyType.KEYPAIR_PUBLIC_KEY)) {
				RSAAndDSAPublicKeys.add(ksAlias);
			}
		}
		return RSAAndDSAPublicKeys;
	}

	public static void showMessageBox(String title, String text, int type) {
		MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(),
				type);
		box.setText(title);
		box.setMessage(text);
		box.open();
	}

}
