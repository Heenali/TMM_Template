package net.simplifiedcoding.navigationdrawerexample;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "TrukkerApp";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	private static final String IS_CHECKED = "IsChecked";
	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	public static final String KEY_PIC = "photo";
	public static final String KEY_TRUCKID = "truck_id";
	public static final String KEY_DRIVERID = "driver_id";
	public static final String KEY_INQNO = "load_inquiry_no";

	public static final String KEY_DATE = "date";
	public static final String KEY_DATE_Message = "lastmsgdate";

	public static final String KEY_Notes = "AppNotes";
	// password (make variable public to access from outside)
	public static final String KEY_PASS = "pass";

	public static final String KEY_INS_NM = "ins_nm";
	public static final String KEY_INS_ID = "ins_id";

	public static final String OAM_Counter = "oam";
	public static final String CAM_Counter = "cam";
	public static final String GCM_REGID = "regId";

	public static final String KEY_USERID = "user_id";
	public static final String KEY_EMAILID = "email_id";
	public static final String KEY_FIRSTNAME = "first_name";
	public static final String KEY_LASTNAME = "last_name";
	public static final String KEY_MIDDLENAME = "middle_name";
	public static final String KEY_ROLE_ID = "role_id";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_CLIENT_TYPE = "client_type";
	public static final String KEY_UNIQUE_ID = "unique_id";

	//Transporter
	public static final String KEY_TRUCKLAT = "truck_lat";
	public static final String KEY_TRUCKLNG = "truck_lng";


	//Log Email and Password
	public static final String KEY_LOG_EMAIL = "log_email";
	public static final String KEY_LOG_PASSWOD = "log_password";
	public static final String KEY_LOG_SENDTOEMAIL = "log_sendto_email";

	//Truck Status
	public static final String KEY_TRUCKSTATUS = "truck_status";


	//Save Owner Id for Driver Postload
	public static final String KEY_OWNERID_FOR_DRIVER = "owner_id";
	public static final String KEY_OWNEDRIVER_ID = "owner_driver_id";


	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name li_history pref
		editor.putString(KEY_NAME, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}



	public void setLogEmailAndPassword(String email, String password)
	{
		editor.putString(KEY_LOG_EMAIL, email);
		editor.putString(KEY_LOG_PASSWOD, password);
		editor.commit();
	}


	public void setLogSendToEmail(String email) {
		editor.putString(KEY_LOG_SENDTOEMAIL, email);

		editor.commit();
	}

	public void storeUserPhoto(String name) {
		// Storing login value as TRUE


		// Storing name li_history pref
		editor.putString(KEY_PIC, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}



	public void saveTruckId(String name) {
		// Storing login value as TRUE


		// Storing name li_history pref
		editor.putString(KEY_TRUCKID, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}

	public void saveDriverId(String name) {
		// Storing login value as TRUE


		// Storing name li_history pref
		editor.putString(KEY_DRIVERID, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}

	public void saveOwnerIdForDriverPostLoad(String owner_id)
	{
		editor.putString(KEY_OWNERID_FOR_DRIVER, owner_id);

		editor.commit();
	}


	public void saveInqNO(String name) {
		// Storing login value as TRUE


		// Storing name li_history pref
		editor.putString(KEY_INQNO, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}



	public void saveTruckLatLNG(String lat, String lng) {
		// Storing login value as TRUE


		// 
		editor.putString(KEY_TRUCKLAT, lat);
		editor.putString(KEY_TRUCKLNG, lng);


		// commit changes
		editor.commit();
	}

	//Truck Status
	public void saveLastTruckStatus(String name) {
		// Storing login value as TRUE


		// Storing name li_history pref
		editor.putString(KEY_TRUCKSTATUS, name);

		// Login Date
		//editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}


	public String getDriverID() {

		String id = pref.getString(KEY_DRIVERID, null);

		return id;
	}

	public String getOwnerIDForDriverPostLoad() {

		String ownerid = pref.getString(KEY_OWNERID_FOR_DRIVER, null);

		return ownerid;
	}

	public String getLastTruckStatus() {

		String status = pref.getString(KEY_TRUCKSTATUS, null);

		return status;
	}

	public String getLogEmail() {

		String email = pref.getString(KEY_LOG_EMAIL, null);

		return email;
	}

	public String getLogPassword() {

		String pass = pref.getString(KEY_LOG_PASSWOD, null);

		return pass;
	}

	public String getLogSendToEmail() {

		String email = pref.getString(KEY_LOG_SENDTOEMAIL, null);

		return email;
	}

	public String getTruckLat() {

		String uid = pref.getString(KEY_TRUCKLAT, null);

		return uid;
	}

	public String getTruckLng() {

		String uid = pref.getString(KEY_TRUCKLNG, null);

		return uid;
	}


	public String getTruckID() {

		String uid = pref.getString(KEY_TRUCKID, null);

		return uid;
	}


	public String getInqId() {

		String uid = pref.getString(KEY_INQNO, null);

		return uid;
	}

	public String getDevice_Id() {

		String uid = pref.getString(KEY_USERID, null);

		return uid;
	}

	public String getUserId() {

		String uid = pref.getString(KEY_USERID, null);

		return uid;
	}
	public String getRoleId() {

		String role_id = pref.getString(KEY_ROLE_ID, null);

		return role_id;
	}
	public String getUniqueId() {

		String unique_id = pref.getString(KEY_UNIQUE_ID, null);

		return unique_id;
	}

	public String getstorephoto() {

		String pic = pref.getString(KEY_PIC, null);

		return pic;
	}

	public String getemailid() {

		String pic = pref.getString(KEY_EMAILID, null);

		return pic;
	}

	public void setUserId(String uid, String first_name, String last_name, String middle_name, String password, String client_type, String unique_id, String role_id, String email_id) {


		// Storing name li_history pref
		editor.putString(KEY_USERID, uid);
		editor.putString(KEY_FIRSTNAME,first_name);
		editor.putString(KEY_LASTNAME, last_name);
		editor.putString(KEY_MIDDLENAME, middle_name);
		editor.putString(KEY_PASSWORD,password);
		editor.putString(KEY_CLIENT_TYPE, client_type);
		editor.putString(KEY_UNIQUE_ID, unique_id);
		editor.putString(KEY_ROLE_ID, role_id);
		editor.putString(KEY_EMAILID, email_id);
		// Login Date
		// editor.putString(KEY_DATE, date);

		// commit changes
		editor.commit();
	}
	/*
	 * public void setChecked(){ editor.putBoolean(IS_CHECKED, true);
	 * 
	 * editor.commit(); }
	 */
	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged li_history redirect him to Login Activity
			Intent i = new Intent(_context, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}

	}

	/**
	 * Get stored session data
	 * */
	public String getUserDetails() {

		String uName = pref.getString(KEY_NAME, null);

		return uName;
	}


	public String getUserName() {

		String uName = pref.getString(KEY_FIRSTNAME, null);

		return uName;
	}
	public String getUserName1() {

		String uName = pref.getString(KEY_LASTNAME, null);

		return uName;
	}
	public String getUserName2() {

		String uName = pref.getString(KEY_MIDDLENAME, null);

		return uName;
	}

	public String getUserLoginDate() {

		String logged_date = pref.getString(KEY_DATE, null);

		return logged_date;
	}


	public void setUserNewDate(String date) {

		editor.putString(KEY_DATE, date);

		editor.commit();
	}


	public String getLastMessageDate() {

		String last_date = pref.getString(KEY_DATE_Message, null);

		return last_date;
	}




	public void setLastMessageDate(String lastDate) {

		editor.putString(KEY_DATE_Message, lastDate);

		editor.commit();
	}

	public void setOAMCounter(int counter) {

		editor.putInt(OAM_Counter, counter);
		editor.commit();
	}

	public void setCAMCounter(int counter) {

		editor.putInt(CAM_Counter, counter);

		editor.commit();
	}

	public int getOAMCounter() {

		int oam_counter = pref.getInt(OAM_Counter, 0);

		return oam_counter;
	}

	public int getCAMCounter() {

		int cam_counter = pref.getInt(CAM_Counter, 0);

		return cam_counter;
	}

	public void setAppNotes(String notes) {

		editor.putString(KEY_Notes, notes);

		editor.commit();
	}

	public String getNotes() {

		String notes = pref.getString(KEY_Notes, null);

		return notes;
	}


	public void setRegId(String regId) {

		editor.putString(GCM_REGID, regId);

		editor.commit();
	}


	public String getRegId() {

		String regid = pref.getString(GCM_REGID, null);

		return regid;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}


	public void msg(String msg) {
		Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
	}

	public void saveODID(String driver_id)
	{
		editor.putString(KEY_OWNEDRIVER_ID, driver_id);
		editor.commit();

	}

	public String getODID() {
		String owner_id = pref.getString(KEY_OWNEDRIVER_ID, null);

		return owner_id;
	}
}
