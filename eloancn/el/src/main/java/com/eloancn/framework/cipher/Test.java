package com.eloancn.framework.cipher;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EnvironmentStringPBEConfig con =new EnvironmentStringPBEConfig();
		con.setAlgorithm("PBEWithMD5AndDES");
		con.setPassword("1qazxsw23edc");
		StandardPBEStringEncryptor en =new StandardPBEStringEncryptor();
		en.setConfig(con);
		System.out.println(en.encrypt("el_eloan_db"));
		System.out.println(en.encrypt("eleloandbjck6JXi9"));
		//System.out.println(en.decrypt("6z3nD1ld48bQKi8A3eVLanQ7nRD2UqbF"));
		//System.out.println(en.decrypt("X6gCOouqw71tVKEK1Xs+Fvpwt80/TA32bN9Yw0db/UA="));
		
	}

}
