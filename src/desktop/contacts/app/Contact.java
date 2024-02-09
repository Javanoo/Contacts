package desktop.contacts.app;

import java.io.Serializable;
import java.util.HashMap;

public class Contact implements Cloneable, Serializable, Comparable<Contact>{
	private static final long serialVersionUID = -3346460993797773103L; 
	
	private double[] profileColors = new double[3];
	
	private String name = "unkown";
	
	//each phone number is associated with a label, i.e +182...89 (home).
	private HashMap<String, String> phoneNumbers = new HashMap<>();
	public final static String[] PHONENUMBERlABEL = new String[] {"mobile", "home", "work"};
	
	//same as the phone number, each email is tagged or labeled.
	private HashMap<String,String> email  = new HashMap<>();
	public final static String[] EMAILlABEL = new String[] {"personal", "business"};
	
	private String address = "unkown";
	private String note = "unknown";
	
	Contact(String name, HashMap<String, String> numbers, HashMap<String, String> mails, 
			String address, String note){
		this.name = name;
		phoneNumbers = numbers;
		email = mails;
		this.address = address;
		this.note = note;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the phoneNumbers
	 */
	public HashMap<String, String> getPhoneNumbers() {
		return phoneNumbers;
	}

	/**
	 * @return the email
	 */
	public HashMap<String, String> getEmail() {
		return email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param phoneNumbers the phoneNumbers to set
	 */
	public void setPhoneNumbers(HashMap<String, String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(HashMap<String, String> email) {
		this.email = email;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		if(!note.isBlank())
			this.note = note.strip();
	}
	
	
	@Override
	public int compareTo(Contact o) {
		// TODO Auto-generated method stub
		return name.compareTo(o.getName());
	}
	@Override
	public String toString() {
		return name + "\n" + address;
	}

	/**
	 * @return the profileBackGroundColors
	 */
	public double[] getProfileColors() {
		return profileColors;
	}

	/**
	 * @param profileColors the profileBackGroundColors to set
	 */
	public void setProfileColors(double[] profileColors) {
		this.profileColors = profileColors;
	}
}


