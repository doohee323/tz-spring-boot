package com.tz.basis.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 설    명 : Seed 알고리즘에 따라 ECB (Electronic Cookbook), CBC(Cipher-Block Chaining)방식의 암호화를 지원한다.
 *        ECB에서는 인크립션 프로세스가 데이터 블럭마다 동일하게 적용되며 반복되는 패턴들은 동일한 방식으로 암호화된다.
 *        CBC에서는 이전 블럭의 인크립션을 토대로 그 다음 블럭을 암호화한다.
 *
 *        Base 64 : 바이트 배열을 아스키 문자로 표현
 *        MD5 : 해시함수의 알고리즘이며 결과물은 메시지 무결성을 체크
 *        [암호화 알고리즘]
 *        http://math88.com.ne.kr/crypto2.htm
 *        DES : 64비트 블록암호알고리즘 (동일한 키를 생성하여 암호화와 복호화가 이루워지는 대칭키 방식)
 *        SEED : 한국정보보호센터에서 128비트 블록암호알고리즘(SEED)
 *        AES : 블록암호알고리즘, DES 알고리즘의 후속버젼(?)으로 128, 192, 256비트 키를 사용
 *        RSA : 암호키의 안전한 분배 및 관리문제를 해결, 증권사 등에 활용
 * ---------------------------------------------------------------
 * </pre>
 * 
 * @version 1.0
 */
public class SecurityUtil extends SeedBase {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 ECB 암호화.
	 * </pre>
	 * 
	 * @param data
	 *          암호화시킬 데이타
	 * @param len
	 *          암호화시킬길이
	 * @param key
	 *          암호화시킬 키
	 * @return byte[] 암호화된 데이타.
	 */
	public byte[] ECBEncrypt(byte[] data, int len, byte[] key) {
		int i, block;
		byte[] dup_key = new byte[SEED_BLOCK_SIZE];
		byte[] m = new byte[SEED_BLOCK_SIZE];
		byte[] c = new byte[SEED_BLOCK_SIZE];
		byte[] pad_data;
		byte[] result;

		// 키를 복사해서 사용.
		System.arraycopy(key, 0, dup_key, 0, SEED_BLOCK_SIZE);

		if (len == 0) {
			return null;
		}

		// 데이터 패딩을 한다.
		pad_data = Padding(data, len);

		// 패딩한 데이터를 16바이트 블럭으로 계산한다.
		block = pad_data.length / SEED_BLOCK_SIZE;
		result = new byte[block * SEED_BLOCK_SIZE];

		// 키값 초기화 및 라운딩
		for (i = 0; i < EncRoundKey.length; i++) {
			EncRoundKey[i] = 0x00;
		}
		SeedEncRoundKey(dup_key);

		// 블럭단위로 암호화
		for (i = 0; i < block; i++) {
			System.arraycopy(pad_data, i * SEED_BLOCK_SIZE, m, 0, SEED_BLOCK_SIZE);
			c = SeedEncrypt(m, EncRoundKey);
			System.arraycopy(c, 0, result, i * SEED_BLOCK_SIZE, SEED_BLOCK_SIZE);
		}

		return result;
	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 ECB 복호화.
	 * </pre>
	 * 
	 * @param data
	 *          복호화시킬 데이타
	 * @param len
	 *          복호화시킬 길이
	 * @param key
	 *          복호화시킬 키
	 * @return byte[] 암호화된 데이타.
	 */
	public byte[] ECBDecrypt(byte[] data, int len, byte[] key) {
		int i, block;
		byte[] dup_key = new byte[SEED_BLOCK_SIZE];
		byte[] m = new byte[SEED_BLOCK_SIZE];
		byte[] c = new byte[SEED_BLOCK_SIZE];
		byte[] pad_data;
		byte[] result;

		// 키를 복사해서 사용.
		System.arraycopy(key, 0, dup_key, 0, SEED_BLOCK_SIZE);

		int return_len = 0;
		byte[] return_data;

		// 길이가 0이면 null을 리턴한다.
		if (len == 0) {
			return null;
		}

		// 버퍼 크기화 초기화를 한다.
		result = new byte[len];
		block = len / SEED_BLOCK_SIZE;
		pad_data = new byte[len];
		System.arraycopy(data, 0, pad_data, 0, len);

		// 키 초기화
		for (i = 0; i < EncRoundKey.length; i++) {
			EncRoundKey[i] = 0x00;
		}
		SeedEncRoundKey(dup_key);

		// 블럭단위로 복호화
		for (i = 0; i < block; i++) {
			System.arraycopy(pad_data, i * SEED_BLOCK_SIZE, c, 0, SEED_BLOCK_SIZE);
			m = SeedDecrypt(c, EncRoundKey);
			System.arraycopy(m, 0, result, i * SEED_BLOCK_SIZE, SEED_BLOCK_SIZE);
		}

		return_len = UnPadding(result, len);

		// 필요한 부분만 생성하여 리턴한다.
		return_data = new byte[return_len];
		System.arraycopy(result, 0, return_data, 0, return_len);

		return return_data;
	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 CBC 암호화한 다음 마지막 블럭을 돌려준다.
	 * </pre>
	 * 
	 * @param data
	 *          암호화시킬 데이타
	 * @param len
	 *          암호화시킬 길이
	 * @param key
	 *          암호화시킬 키
	 * @return byte[] MAC 데이타.
	 */
	public byte[] CBCMAC(byte[] data, int len, byte[] key) {
		int i, block;
		byte[] dup_key = new byte[SEED_BLOCK_SIZE];
		byte[] m = new byte[SEED_BLOCK_SIZE];
		byte[] c = new byte[SEED_BLOCK_SIZE];
		byte[] iv = new byte[SEED_BLOCK_SIZE];
		byte[] pad_data;

		// 키를 복사해서 사용.
		System.arraycopy(key, 0, dup_key, 0, SEED_BLOCK_SIZE);

		if (len == 0) {
			return null;
		}

		// 초기화..
		for (i = 0; i < SEED_BLOCK_SIZE; i++) {
			iv[i] = 0x00;
		}

		// 키 초기화...
		for (i = 0; i < EncRoundKey.length; i++) {
			EncRoundKey[i] = 0x00;
		}
		SeedEncRoundKey(dup_key);

		pad_data = Padding(data, len);
		block = pad_data.length / SEED_BLOCK_SIZE;

		for (i = 0; i < block; i++) {
			System.arraycopy(pad_data, i * SEED_BLOCK_SIZE, m, 0, SEED_BLOCK_SIZE);

			mem_xor(m, iv, SEED_BLOCK_SIZE, 0);

			c = SeedEncrypt(m, EncRoundKey);

			System.arraycopy(c, 0, iv, 0, SEED_BLOCK_SIZE);
		}

		return c;
	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 CBC 암호화.
	 * </pre>
	 * 
	 * @param data
	 *          암호화시킬 데이타
	 * @return byte[] 암호화된 데이타
	 */
	public byte[] CBCEncrypt(byte[] data) {
		byte[] byteRetKey = { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD,
				(byte) 0xEF, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0X9A, (byte) 0XBC, (byte) 0XDE,
				(byte) 0XF0 };
		return CBCEncrypt(data, data.length, byteRetKey, byteRetKey);
	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 CBC 암호화.
	 * </pre>
	 * 
	 * @param data
	 *          암호화시킬 데이타
	 * @param len
	 *          암호화시킬길이
	 * @param key
	 *          암호화시킬 키
	 * @param iv
	 *          초기값
	 * @return byte[] 암호화된 데이타
	 */
	public byte[] CBCEncrypt(byte[] data, int len, byte[] key, byte[] iv) {
		int i, block;
		byte[] dup_key = new byte[SEED_BLOCK_SIZE];
		byte[] dup_iv = new byte[SEED_BLOCK_SIZE];
		byte[] m = new byte[SEED_BLOCK_SIZE];
		byte[] c = new byte[SEED_BLOCK_SIZE];
		byte[] pad_data;
		byte[] result;

		// 키를 복사해서 사용.
		System.arraycopy(key, 0, dup_key, 0, SEED_BLOCK_SIZE);
		System.arraycopy(iv, 0, dup_iv, 0, SEED_BLOCK_SIZE);

		if (len == 0) {
			return null;
		}

		// 데이터 패딩을 한다.
		pad_data = Padding(data, len);

		// 패딩한 데이터를 16바이트 블럭으로 계산한다.
		block = pad_data.length / SEED_BLOCK_SIZE;
		result = new byte[block * SEED_BLOCK_SIZE];

		// 키값 초기화 및 라운딩
		for (i = 0; i < EncRoundKey.length; i++) {
			EncRoundKey[i] = 0x00;
		}
		SeedEncRoundKey(dup_key);

		// 블럭단위로 암호화
		for (i = 0; i < block; i++) {
			System.arraycopy(pad_data, i * SEED_BLOCK_SIZE, m, 0, SEED_BLOCK_SIZE);

			mem_xor(m, dup_iv, SEED_BLOCK_SIZE, 0);

			c = SeedEncrypt(m, EncRoundKey);

			System.arraycopy(c, 0, result, i * SEED_BLOCK_SIZE, SEED_BLOCK_SIZE);

			System.arraycopy(c, 0, dup_iv, 0, SEED_BLOCK_SIZE);
		}

		return result;
	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 복호화.
	 * </pre>
	 * 
	 * @param data
	 *          복호화시킬 데이타
	 * @return byte[] 복화화된 데이타
	 */
	public byte[] CBCDecrypt(byte[] data) {
		// UserFunction uf = new UserFunction();
		byte[] byteRetKey = { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD,
				(byte) 0xEF, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0X9A, (byte) 0XBC, (byte) 0XDE,
				(byte) 0XF0 };
		// logger.debug("Decrypt Data : "+uf.ascii2hexstr(data));
		// logger.debug("Before Decrypt : "+uf.ascii2hexstr(byteRetKey));

		return CBCDecrypt(data, data.length, byteRetKey, byteRetKey);

	}

	/**
	 * <pre>
	 * Data를 len 길이만큼 Key로 복호화.
	 * </pre>
	 * 
	 * @param data
	 *          복호화시킬 데이타
	 * @param len
	 *          복호화시킬길이
	 * @param key
	 *          복호화시킬 키
	 * @param iv
	 *          초기값
	 * @return byte[] 복화화된 데이타
	 */
	public byte[] CBCDecrypt(byte[] data, int len, byte[] key, byte[] iv) {
		int i, block;
		byte[] dup_key = new byte[SEED_BLOCK_SIZE];
		byte[] dup_iv = new byte[SEED_BLOCK_SIZE];
		byte[] m = new byte[SEED_BLOCK_SIZE];
		byte[] c = new byte[SEED_BLOCK_SIZE];
		byte[] pad_data;
		byte[] result;
		// UserFunction uf = new UserFunction();

		// 키를 복사해서 사용.
		// logger.debug("before copy Decrypt : "+uf.ascii2hexstr(key));
		System.arraycopy(key, 0, dup_key, 0, SEED_BLOCK_SIZE);
		System.arraycopy(iv, 0, dup_iv, 0, SEED_BLOCK_SIZE);
		// logger.debug("after copy Decrypt : "+uf.ascii2hexstr(key));

		int return_len = 0;
		byte[] return_data;

		// 길이가 0이면 null을 리턴한다.
		if (len == 0) {
			return null;
		}

		// 버퍼 크기화 초기화를 한다.
		result = new byte[len];
		block = len / SEED_BLOCK_SIZE;
		pad_data = new byte[len];
		System.arraycopy(data, 0, pad_data, 0, len);

		// 키 초기화
		for (i = 0; i < EncRoundKey.length; i++) {
			EncRoundKey[i] = 0x00;
		}
		SeedEncRoundKey(dup_key);

		// 블럭단위로 복호화
		for (i = 0; i < block; i++) {
			System.arraycopy(pad_data, i * SEED_BLOCK_SIZE, c, 0, SEED_BLOCK_SIZE);
			m = SeedDecrypt(c, EncRoundKey);
			mem_xor(m, dup_iv, SEED_BLOCK_SIZE, 0);
			System.arraycopy(m, 0, result, i * SEED_BLOCK_SIZE, SEED_BLOCK_SIZE);
			// logger.debug("Decrypt for("+i+") : "+new String(result));
			System.arraycopy(c, 0, dup_iv, 0, SEED_BLOCK_SIZE);
		}

		return_len = UnPadding(result, len);

		// 필요한 부분만 생성하여 리턴한다.
		return_data = new byte[return_len];
		System.arraycopy(result, 0, return_data, 0, return_len);

		// logger.debug("Decrypt dup_key : "+uf.ascii2hexstr(dup_key));
		// logger.debug("Decrypt dup_iv : "+uf.ascii2hexstr(dup_iv));

		return return_data;
	}

	/**
	 * <pre>
	 * base64Encode
	 * </pre>
	 * 
	 * @param raw
	 * @return
	 * @throws Exception
	 */
	public byte[] base64Encode(byte[] raw) throws Exception {
		return org.apache.commons.codec.binary.Base64.encodeBase64(raw);
	}

	public static String base64Encode(String vo) {
		return new String(org.apache.commons.codec.binary.Base64
				.encodeBase64(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(vo)));
	}

	/**
	 * <pre>
	 * base64Decode
	 * </pre>
	 * 
	 * @param raw
	 * @return
	 * @throws Exception
	 */
	public byte[] base64Decode(byte[] raw) throws Exception {
		return org.apache.commons.codec.binary.Base64.decodeBase64(raw);
	}

	public static String base64Decode(String vo) {
		return new String(org.apache.commons.codec.binary.Base64.decodeBase64(vo));
	}

	/**
	 * <pre>
	 * base64로 변환한 데이터 반환
	 * </pre>
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String input) {
		return encrypt(input, null);
	}

	/**
	 * <pre>
	 * encrypt
	 * </pre>
	 * 
	 * @param input
	 * @param charSet
	 * @return
	 */
	public static String encrypt(String input, String charSet) {
		try {
			SecurityUtil su = new SecurityUtil();
			byte[] encResult;
			if (charSet == null) {
				encResult = su.CBCEncrypt(input.getBytes());
			} else {
				encResult = su.CBCEncrypt(input.getBytes(charSet));
			}
			byte[] base64enc = su.base64Encode(encResult);
			return new String(base64enc);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * <pre>
	 * decrypt
	 * </pre>
	 * 
	 * @param input
	 * @return
	 */
	public static String decrypt(String input) {
		return decrypt(input, null);
	}

	/**
	 * <pre>
	 * decrypt
	 * </pre>
	 * 
	 * @param input
	 * @param charSet
	 * @return
	 */
	public static String decrypt(String input, String charSet) {
		try {
			SecurityUtil su = new SecurityUtil();
			byte[] base64dec;
			if (charSet == null) {
				base64dec = su.base64Decode(input.getBytes());
			} else {
				base64dec = su.base64Decode(input.getBytes(charSet));
			}
			byte[] decResult = su.CBCDecrypt(base64dec);
			return new String(decResult);
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * <pre>
	 * aType : MD5, SHA1
	 * </pre>
	 * 
	 * @param aType
	 * @param args
	 * @return
	 */
	public static String getMessageDigest(String aType, String args) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest sha1 = MessageDigest.getInstance(aType);
			sha1.update(args.getBytes());
			byte[] digest = sha1.digest();
			for (byte b : digest) {
				sb.append(Integer.toHexString(b & 0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * main
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SecurityUtil iseed = new SecurityUtil();
			String aa = "sitedev1";

			logger.debug("makeUniqueKey String : " + SecurityUtil.getMessageDigest("SHA", "sitedev1"));

			byte[] enc_data = iseed.CBCEncrypt(aa.getBytes());
			logger.debug("ENC String : " + new String(enc_data));

			byte[] base = org.apache.commons.codec.binary.Base64.encodeBase64(enc_data);
			logger.debug("BASE64 : " + new String(base));

			byte[] result = org.apache.commons.codec.binary.Base64.decodeBase64(base);
			logger.debug("BASE64 Decode : " + new String(result));

			byte[] dec_data = iseed.CBCDecrypt(result);
			logger.debug("Dec String : " + new String(dec_data));
			
			logger.debug("DB pass encryption : " + SecurityUtil.encrypt("passwd123"));
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
