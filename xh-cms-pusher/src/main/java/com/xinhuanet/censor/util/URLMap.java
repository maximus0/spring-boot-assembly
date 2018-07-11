/*
 * URLMap.java created on 2007-10-30 上午11:07:20 by Martin (Fu Chengrui)
 */

package com.xinhuanet.censor.util;

import org.apache.log4j.Logger;

/**
 * 构造和反解形如“01010000123400000123FEDA.html”的URL
 * 
 * @author		Martin (付成睿)
 */
public class URLMap {

	/**
	 * Logger instance
	 */
	private final static Logger s_logger = Logger.getLogger(URLMap.class);

	private final static char[] DIGITS =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final static int DIGITS_0 = '0';

	private final static int DIGITSA9 = 'A' - '9' - 1;

	public final static int DEFAULT_VERSION = 1;

	public final static int DEFAULT_TYPE = 1;

	/**
	 * 格式化整数为16进制字符串
	 */
	private final static void int2hex(int j, StringBuffer sb, int length) {
		char[] buf = new char[length];
		for (int i = length - 1; i >= 0; i--) {
			buf[i] = DIGITS[j & 0xF];
			j >>>= 4;
		}
		sb.append(buf);
	}

	/**
	 * 转化16进制字符串为整数
	 */
	private final static int hex2int(String s, int beginIndex, int endIndex) {
		//TODO：如果“endIndex - beginIndex”大于8，怎么办
		int iResult = 0;
		for (int i = beginIndex; i < endIndex; i++) {
			int iTemp = s.charAt(i) - DIGITS_0;
			if (iTemp > 9) iTemp = iTemp - DIGITSA9;
			iTemp &= 0xF;
			iResult <<= 4;
			iResult |= iTemp;
		}
		return iResult;
	}

	private final static boolean isSupportedVersion(int version) {
		return true;
	}

	private final static boolean isKnownType(int type) {
		return true;
	}

	private final static boolean isValidPathInfo(int version, String sPathInfo) {
		if (sPathInfo.length() < 24) {
			return false;
		}
		return true;
	}

	private final static int caculateCheckSum(int hash, int pid, int aid) {
		return (hash + (hash >> 13)) | (pid - (pid >> 12)) & (aid ^ (aid >> 11));
	}

	public final static URLMap valueOf(String sPathInfo) {

		//检验长度是否符合最短要求
		if (sPathInfo == null || sPathInfo.length() < 24) {
			return null;
		}

		//获取版本
		int version = hex2int(sPathInfo, 0, 2);
		if (!isSupportedVersion(version)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Error version:" + sPathInfo);
			}
			return null;
		}

		if (!isValidPathInfo(version, sPathInfo)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Valid PathInfo:" + sPathInfo);
			}
			return null;
		}

		//获取类型
		int type = hex2int(sPathInfo, 2, 4);
		if (!isKnownType(type)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Unknown type:" + sPathInfo);
			}
			return null;
		}

		//获取父对象的ID
		int pid = hex2int(sPathInfo, 4, 12);

		//获取对象自身的ID
		int aid = hex2int(sPathInfo, 12, 20);

		URLMap map = new URLMap();
		map.m_iVersion = DEFAULT_VERSION;
		map.m_iType = type;
		map.m_iParentId = pid;
		map.m_iId = aid;
		map.m_iCheckSum = 0;
		return map;
	}

	public final static URLMap valueOf(String sUserName, String sPathInfo) {

		//检验长度是否符合最短要求
		if (sPathInfo == null || sPathInfo.length() < 24) {
			return null;
		}

		//获取版本
		int version = hex2int(sPathInfo, 0, 2);
		if (!isSupportedVersion(version)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Error version:" + sPathInfo);
			}
			return null;
		}

		if (!isValidPathInfo(version, sPathInfo)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Valid PathInfo:" + sPathInfo);
			}
			return null;
		}

		//获取类型
		int type = hex2int(sPathInfo, 2, 4);
		if (!isKnownType(type)) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Unknown type:" + sPathInfo);
			}
			return null;
		}

		//获取用户名的hashCode
		int hash = sUserName.hashCode();

		//获取父对象的ID
		int pid = hex2int(sPathInfo, 4, 12);

		//获取对象自身的ID
		int aid = hex2int(sPathInfo, 12, 20);

		//获取校验和
		int checkSumFromInfoPath = hex2int(sPathInfo, 20, 24);

		//计算校验和
		int checkSum = caculateCheckSum(hash, pid, aid);
		checkSum = checkSum & 0xFFFF;

		//比较两个校验和
		if (checkSum != checkSumFromInfoPath) {
			if (s_logger.isDebugEnabled()) {
				s_logger.debug("Checksum not match:" + sPathInfo);
			}
			return null;
		}

		URLMap map = new URLMap();
		map.m_iVersion = DEFAULT_VERSION;
		map.m_sUserName = sUserName;
		map.m_iType = type;
		map.m_iParentId = pid;
		map.m_iId = aid;
		map.m_iCheckSum = checkSum;
		return map;
	}

	public final static URLMap valueOf(String sUserName, int iParentId, int iId) {
		return valueOf(sUserName, iParentId, iId, DEFAULT_TYPE);
	}

	public final static URLMap valueOf(String sUserName, int iParentId, int iId, int iType) {
		URLMap map = new URLMap();
		map.m_iVersion = DEFAULT_VERSION;
		map.m_sUserName = sUserName;
		map.m_iType = iType;
		map.m_iParentId = iParentId;
		map.m_iId = iId;
		map.m_iCheckSum = caculateCheckSum(sUserName.hashCode(), iParentId, iId);
		return map;
	}

	public int m_iVersion;

	public String m_sUserName;

	public int m_iType;

	public int m_iParentId;

	public int m_iId;

	public int m_iCheckSum;

	protected URLMap() {
	}

	public final int getVersion() {
		return m_iVersion;
	}

	public final String getUserName() {
		return m_sUserName;
	}

	public final int getType() {
		return m_iType;
	}

	public final int getParentId() {
		return m_iParentId;
	}

	public final int getId() {
		return m_iId;
	}

	public final int getCheckSum() {
		return m_iCheckSum;
	}

	private StringBuffer makeURL() {
		StringBuffer sb = new StringBuffer(32);
		int2hex(m_iVersion, sb, 2);
		int2hex(m_iType, sb, 2);
		int2hex(m_iParentId, sb, 8);
		int2hex(m_iId, sb, 8);
		int2hex(m_iCheckSum, sb, 4);
		return sb;
	}

	public String getPathInfo(String ext) {
		return makeURL().append(ext).toString();
	}

	@Override
	public String toString() {
		return makeURL().toString();
	}

}
