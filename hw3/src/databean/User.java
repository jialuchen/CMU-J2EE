/**
 * Author : Jialu Chen
 */
package databean;

import org.genericdao.PrimaryKey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */
@PrimaryKey("userId")
public class User {

    private int userId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String hashedPassword;
    private int salt;
    
    public User() {
    }

    public User(String emailAddress, String firstName, String lastName, String password) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        salt = newSalt();
        this.hashedPassword = hash(password);
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    private String hash(String clearPassword) {
        if (salt == 0) return null;

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Can't find the SHA1 algorithm in the java.security package");
        }

        String saltString = String.valueOf(salt);

        md.update(saltString.getBytes());
        md.update(clearPassword.getBytes());
        byte[] digestBytes = md.digest();

        // Format the digest as a String
        StringBuffer digestSB = new StringBuffer();
        for (int i = 0; i < digestBytes.length; i++) {
            int lowNibble = digestBytes[i] & 0x0f;
            int highNibble = (digestBytes[i] >> 4) & 0x0f;
            digestSB.append(Integer.toHexString(highNibble));
            digestSB.append(Integer.toHexString(lowNibble));
        }
        String digestStr = digestSB.toString();

        return digestStr;
    }

    private int newSalt() {
        Random random = new Random();
        return random.nextInt(8192) + 1;  // salt cannot be zero
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPassword(String password) {
        salt = newSalt();
        hashedPassword = hash(password);
        System.out.println("salt:" + salt + ", password:" + password);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean checkPassword(String password) {
        return password != null && !password.replaceAll(" ", "").equals("")
                && this.hashedPassword != null && this.hashedPassword.equals(hash(password));
    }

}
