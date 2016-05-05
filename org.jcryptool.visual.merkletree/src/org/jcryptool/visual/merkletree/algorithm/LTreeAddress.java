package org.jcryptool.visual.merkletree.algorithm;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class LTreeAddress extends Address {
	byte layerAddress;
	byte[] treeAddress = new byte[5];
	byte otsBit;
	byte lTreeBit;
	byte[] lTreeAddress = new byte[3];
	byte treeHeight;
	byte[] treeIndex = new byte[3];
	byte blockKeyBit;

	@Override
	public void setHashAdress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setKeyBit(int i) {
		if(i == 0) {
			blockKeyBit -= 1; //set keyBit (bit nr. 0) to 0
		} else {
			blockKeyBit += 1; //set keyBit (bit nr. 0) to 1
		}

	}

	@Override
	public void setChainAddress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setBlockBit(int i) {
		if(i == 0) {
			blockKeyBit -= 2; //set blockBit (bit nr. 1) to 0
		} else {
			blockKeyBit += 2; //set blockBit (bit nr. 1) to 1
		}

	}

	@Override
	public void setTreeHeight(int i) {
		treeHeight = (byte) i;

	}

	@Override
	public void setTreeIndex(int i) {
		treeIndex[0] = (byte)(i >> 24);
		treeIndex[1] = (byte)(i >> 16);
		treeIndex[2] = (byte)(i >> 8);
		treeIndex[3] = (byte)(i);

	}

	@Override
	public int getTreeHeight() {
		return treeHeight;
	}

	@Override
	public void setOTSBit(int i) {
		otsBit = (byte)i;
	}

	@Override
	public void setOTSAddress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setLTreeBit(int i) {
		if(i == 0) {
			lTreeBit -= 1; //set lTreeBit (bit nr. 0) to 0
		} else {
			lTreeBit += 1; //set lTreeBit (bit nr. 0) to 1
		}

	}

	@Override
	public void setLTreeAddress(int i) {
		lTreeAddress[0] = (byte)(i >> 24);
		lTreeAddress[1] = (byte)(i >> 16);
		lTreeAddress[2] = (byte)(i >> 8);
		lTreeAddress[3] = (byte)(i);
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
		ByteUtils.concatenate(temp, lTreeBit);
		ByteUtils.concatenate(temp, lTreeAddress);
		ByteUtils.concatenate(temp, treeHeight);
		ByteUtils.concatenate(temp, treeIndex);
		ByteUtils.concatenate(temp, blockKeyBit);
		return temp;
		
	}

}
