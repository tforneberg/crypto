package org.jcryptool.visual.merkletree.algorithm;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class OTSHashAddress extends Address {
	byte layerAddress;
	byte[] treeAddress = new byte[5];
	byte otsBit;
	byte[] otsAddress = new byte[3];
	byte[] chainAddress = new byte[2];
	byte hashAddress;
	byte keyBit;

	@Override
	public void setHashAdress(int i) {
		hashAddress = (byte)i;
	}

	@Override
	public void setKeyBit(int i) {
		if(i == 0) {
			keyBit -= 1; //set keyBit (bit nr. 0) to 0
		} else {
			keyBit += 1; //set keyBit (bit nr. 0) to 1
		}
	}

	@Override
	public void setChainAddress(int i) {
		chainAddress[0] = (byte)(i >> 8);
		chainAddress[1] = (byte)(i);
	}

	@Override
	public void setBlockBit(int i) {
		// do nothing, not needed

	}

	@Override
	public void setTreeHeight(int i) {
		// do nothing, not needed

	}

	@Override
	public void setTreeIndex(int i) {
		// do nothing, not needed

	}

	@Override
	public int getTreeHeight() {
		// do nothing, not needed
		return 0;
	}

	@Override
	public void setOTSBit(int i) {
		otsBit = (byte)i;

	}

	@Override
	public void setOTSAddress(int i) {
		otsAddress[0] = (byte)(i >> 24);
		otsAddress[1] = (byte)(i >> 16);
		otsAddress[2] = (byte)(i >> 8);
		otsAddress[3] = (byte)(i);
	}

	@Override
	public void setLTreeBit(int i) {
		// do nothing, not needed

	}

	@Override
	public void setLTreeAddress(int i) {
		
	}

	@Override
	public void setLayerAddress(int i) {
		layerAddress = (byte) i;

	}

	@Override
	public void setTreeAddress(int i) {
		// TODO Auto-generated method stub

	}
	
	@Override
	/**
	 * @return Address construct as in the rfc
	 */
	public byte[] getAddress(){
		byte[] temp = ByteUtils.concatenate(layerAddress, treeAddress);
		ByteUtils.concatenate(temp, otsBit);
		ByteUtils.concatenate(temp, otsAddress);
		ByteUtils.concatenate(temp, chainAddress);
		ByteUtils.concatenate(temp, hashAddress);
		ByteUtils.concatenate(temp, keyBit);
		return temp;
		
	}

}
