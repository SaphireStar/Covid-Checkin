package au.usyd.elec5619.service.security;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * Provide security for password using RSA and md5
 * @author  Yiqing Yang yyan8151
 *
 */
@Service(value = "encryptionManager")
public class EncryptionManager {

	// private key
		static String privatekeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALyuWEd1e6Egdx3z\r\n" + 
				"gtvDQTZX8bm06lUDW+lxvQe0YSR3kMogE/8wg00TY/v2TsmAhLkyZHQB03klBLgI\r\n" + 
				"A0EcMVCmdFkBsPw5JjB7tpOEv1NlLduFqBDN8TDpfqZQJyBrOUEfmTZUQxcKiWNc\r\n" + 
				"zrrsI9o8O7qCl81V2PzixzMrmrcNAgMBAAECgYAz+vgZH7WiizYlyztm3eDuXsYa\r\n" + 
				"ekwAhb9hjdMc90W37TS3aqT3rd7OciQeexnzi3OFLevcxdAD1P360UJMQr8W1rjr\r\n" + 
				"CVYVGnTxzDvQoWAgv1jp17kJsTUqwhMh3gOabo4I2mmx5wInxAsDMubzEOfKplBe\r\n" + 
				"gOnDDx2k3Qv6+xwmRQJBAOZ+B+BlvbQPpv0c3neXS743DDMu8bDX0hA45L8XqORa\r\n" + 
				"/X0JRoD2hctc6AXbNyakzcb+4ry3OTPNX+4oQJE0ft8CQQDRj8dOvFCP3QJ7BMYm\r\n" + 
				"AFAkZTt/hWxFkVMQCG7mzsbFPA1ESw0ficfnUw8kRhQzJMrHoYehPDeeDuck2HT2\r\n" + 
				"bsOTAkAKbm3A1Bnvyj2Oev3+f5Rn+WnN7zP1Au942HpRw6pbG1sZi4/rSjzn5T6C\r\n" + 
				"9Vbbot3FQcfVLp2iIItU5fF2IOAVAkEAsUDE21MrbVQKChXSqEfxgadiuQ0YtIsk\r\n" + 
				"T9ZiP7W8qQ2PRymYEwoOXRqDHzJrWYsQPeTh/L4baQS//AlefSbZIwJBANll2Ta0\r\n" + 
				"QpBZWAlVdsmDTCKok7SvybC3lIzIlljOTTWKhNda8scnWImdEE6JxO2ufT3Seuai\r\n" + 
				"/S6EiWc0KON8rp8=";
		
		// salt
		private String salt = "3EuEtihqYOd6JPHB8jNefg";
		
		/**
		 * Decrypts the encrypted password
		 * @param str encrypted password
		 * @return decrypted password
		 */
		public String decrypt(String str) throws Exception{
			// input
	        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
	        // private key
	        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privatekeyString)));
	        
	        // RSA decrypt
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, priKey);
	        String plaintext = new String(cipher.doFinal(inputByte));
	        return plaintext;
	    }
		
		/**
		 * Salts and hashes the password
		 * @param password plain text password
		 * @return hashed password
		 */
		public String md5(String password) {
			return DigestUtils.md5DigestAsHex((password+salt).getBytes());
		}
	    

}
