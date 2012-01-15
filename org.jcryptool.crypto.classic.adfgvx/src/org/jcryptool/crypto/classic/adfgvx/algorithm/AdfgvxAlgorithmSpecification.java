//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * 
 * 
 * @author Simon L
 */
public class AdfgvxAlgorithmSpecification extends ClassicAlgorithmSpecification {
	
	public static final String RESULT_TYPE_DOUBLET_IN_KEY = "DOUBLET";

	private static final AbstractAlphabet adfgvxAlphabet = AlphabetsManager.getInstance().getAlphabetByName("ADFGVX Alphabet");
	
	private KeyVerificator NO_DOUBLETS = new KeyVerificator() {
		@Override
		protected boolean verifyKeyInput(String key, AbstractAlphabet alphabet) {
			Set<Character> occurence = new HashSet<Character>();
			for(int i=0; i<key.length(); i++) {
				if(occurence.contains(key.charAt(i))) return false; else {
					occurence.add(key.charAt(i));
				}
			}
			return true;
		}
		@Override
		protected InputVerificationResult getFailResult(String key,
				AbstractAlphabet alphabet) {
			return new InputVerificationResult() {
				public boolean isValid() {
					return false;
				}
				public boolean isStandaloneMessage() {
					return false;
				}
				public MessageType getMessageType() {
					return InputVerificationResult.MessageType.WARNING;
				}
				public String getMessage() {
					return org.jcryptool.crypto.classic.adfgvx.ui.Messages.AdfgvxWizardPage_onlyoccuronce;
				}
				public String getResultType() {
					return RESULT_TYPE_DOUBLET_IN_KEY; //$NON-NLS-1$
				}
			};
		}
	};
	
	@Override
	public boolean isValidPlainTextAlphabet(AbstractAlphabet alpha) {
		return alpha.equals(adfgvxAlphabet);
	}

	
	
	
	@Override
	public List<KeyVerificator> getKeyVerificators() {
		LogUtil.logWarning("This method shouldnt be used; use the specific methods getKeyVerificatorsSubstitutionKey and getKeyVerificatorsTranspositionKey.");
		return getKeyVerificatorsSubstitutionKey();
	}




	public List<KeyVerificator> getKeyVerificatorsSubstitutionKey() {
		List<KeyVerificator> verificators = new LinkedList<KeyVerificator>();
		verificators.add(KEY_IN_ALPHABET);
		verificators.add(NOKEY(org.jcryptool.crypto.classic.adfgvx.ui.Messages.AdfgvxWizardPage_substitutionHint));
		verificators.add(NO_DOUBLETS);
		
		return verificators;
	}

	public List<KeyVerificator> getKeyVerificatorsTranspositionKey() {
		List<KeyVerificator> verificators = new LinkedList<KeyVerificator>();
		verificators.add(KEY_IN_ALPHABET);
		verificators.add(NOKEY(org.jcryptool.crypto.classic.adfgvx.ui.Messages.AdfgvxWizardPage_transpositionHint));
		
		return verificators;
	}
	
	
	public char[] transpositionKeyInputStringToDataobjectFormat(String keyInput) {
		return keyInput.toCharArray();
	}
	
	public char[] substitutionKeyInputStringToDataobjectFormat(String keyInput) {
		char[] key = keyInput.equals("") ? adfgvxAlphabet.getCharacterSet() : keyInput.toUpperCase().toCharArray();
		char[] keyStep1 = new AdfgvxFactory().getCipherAlphabet(adfgvxAlphabet, key);
		List<Character> charlistStep1 = new LinkedList<Character>();
		for(char c: keyStep1) charlistStep1.add(c);
		String keyStep2 = substKeyFromMatrixAlph(charlistStep1);

		char[] result = keyStep2.toCharArray();
		
		return result;
	}
	
	private String substKeyFromMatrixAlph(List<Character> characters) {
		String substitutionKey = ""; //$NON-NLS-1$
		int y = characters.size();
		if (characters.size() != 36) {
			throw new IllegalArgumentException("Alphabet is not ADFGVX-compatible (must be 36 characters)"); //$NON-NLS-1$
		}

		for(int counter = 0; counter < 36; counter++) {
			if(counter < y) substitutionKey = substitutionKey.concat(String.valueOf(characters.get(counter)));
		}

		return substitutionKey;
	}
	
}
