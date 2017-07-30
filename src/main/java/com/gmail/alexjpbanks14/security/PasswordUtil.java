package com.gmail.alexjpbanks14.security;

import org.apache.commons.codec.binary.Hex;

import com.gmail.alexjpbanks14.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
	
	public static String getSecureSalt(){
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		return Hex.encodeHexString(salt);
	}
	
	public static String hashPassword(String password, String salt){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(password.getBytes());
			return Hex.encodeHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean authenticateUser(User user, String password){
		String pass = hashPassword(password, user.getSalt());
		System.out.println(password);
		System.out.println("J" + pass);
		System.out.println("M" + user.getPassword());
		return user.getPassword().equals(pass);
	}
	
}
