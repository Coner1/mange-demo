package com.yzqc.support.security.rsa;

public class RSAKeySize {
	/**
	 * 密钥长度；
	 */
	private final int keySize;

	/**
	 * 能够进行加密的数据的最大长度；值为 keySize / 8 - 11 ；
	 */
	private final int maxEncryptingDataSize;

	/**
	 * 加密后的数据的长度；值为 keySize / 8；
	 */
	private final int encryptedDataSize;
	
	public RSAKeySize(int keySize) {
		this.keySize = keySize;

		// 根据密钥长度计算出能够加密的数据片段的最大长度；
		if (!is2Power(keySize)) {
			throw new IllegalArgumentException(
					"keySize 必须是 2 的 N 次方(N 大于等于 0)！");
		}
		if (keySize % 8 != 0) {
			throw new IllegalArgumentException("keySize 必须是 8 的整数倍！");
		}
		this.encryptedDataSize = keySize / 8;
		this.maxEncryptingDataSize = keySize / 8 - 11;
		if (this.maxEncryptingDataSize <= 0) {
			throw new IllegalArgumentException(
					"无效的 keySize 值！必须满足 keySize / 8 - 11 的结果大于 0 ！");
		}

	}
	

	/**
	 * 判断指定的值是否是 2 的 N 次方；(N 大于等于 0)
	 * 
	 * @param value
	 * @return
	 */
	private boolean is2Power(int value) {
		if (value < 1) {
			return false;
		}
		return (value & (value - 1)) == 0;
	}
	
	
	/**
	 * 能够进行加密的数据的最大长度；值为 keySize / 8 - 11 ；
	 * 
	 * @return
	 */
	public int getMaxEncryptingDataSize() {
		return maxEncryptingDataSize;
	}

	/**
	 * 加密后的数据的长度；值为 keySize / 8；
	 * 
	 * @return
	 */
	public int getEncryptedDataSize() {
		return encryptedDataSize;
	}

	/**
	 * 密钥长度；
	 * 
	 * @return
	 */
	public int getKeySize() {
		return keySize;
	}
}
