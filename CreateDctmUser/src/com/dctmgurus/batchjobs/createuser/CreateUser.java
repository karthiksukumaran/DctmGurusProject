package com.dctmgurus.batchjobs.createuser;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

public class CreateUser {

	private IDfSessionManager sessionMrg = null;
	
	public void execute(){
		IDfSession session = null;
		try {
			//Initializing the Session Manager
			init();
			
			//Getting the Session
			session = getSession();
			
			//Creating a new User Object
			IDfUser user = (IDfUser)session.newObject("dm_user");
			
			//Setting the User Name
			//This value will be set to dm_user.user_name
			user.setUserName("DCTM Gurus DFC");
			
			//Setting the User Login Password
			//This value will be set to dm_user.user_password
			user.setUserPassword("abcd1234");
			
			//Setting the User Login Name
			//This value will be set to dm_user.user_login_name
			user.setUserLoginName("dfcuserdemo");
			
			//Setting the User OS Name and Domain name
			//This value will be set to dm_user.user_os_name and dm_user.user_os_domain
			user.setUserOSName("dfcuserdemo",null);
			
			//Setting the User Email Address
			//This value will be set to dm_user.user_address
			user.setUserAddress("dfcuserdemo@gmail.com");
			
			//Setting the Description for the user
			//This value will be set to dm_user.description
			user.setDescription("This User is created by DFC");
			
			//Setting the User DB Name
			//This value will be set to dm_user.user_db_name
			user.setUserDBName(session.getDocbaseName());
			
			//Setting the User's Client Capability
			//This value will be set to dm_user.client_capability
			//The available values are None, Consumer, Contributor, CoOrdinator and System Admin
			user.setClientCapability(IDfUser.DF_CAPABILITY_SYSTEM_ADMIN);
			
			//Setting the User Default Cabinet
			//This value will be set to dm_user.default_folder
			//The cabinet will be created if it does not exists,
			//Default is /Temp
			user.setDefaultFolder("/DFCUserDemo", true);
			
			//Setting the Home Docbase
			//This value will be set to dm_user.home_docbase
			user.setHomeDocbase(session.getDocbaseName());
			
			//Setting the User Privileges
			//This value will be set to dm_user.user_privileges
			//The available values are Create Type, Create Group, Create Cabinet
			//System Admin and Super User
			user.setUserPrivileges(IDfUser.DF_PRIVILEGE_SUPERUSER);
			
			//Setting the User Extended Privileges
			//This value will be set to dm_user.user_xprivileges
			user.setUserXPrivileges(IDfUser.DF_XPRIVILEGE_CONFIG_AUDIT 
						+ IDfUser.DF_XPRIVILEGE_VIEW_AUDIT 
						+IDfUser.DF_XPRIVILEGE_PURGE_AUDIT);
			
			//Setting the User Source
			//This value will be set to dm_user.user_source
			user.setString("user_source","inline password");
			
			//Saving the Object
			user.save();
			
			
		} catch (DfException e) {
			//Printing the Stacktrace
			e.printStackTrace();
		} finally {
			//Releasing the session
			releaseSession(session);
		}
	}
	
	//Generic Method for Initializing the session manager
	public void init() throws DfException{
		IDfLoginInfo loginInfo = new DfLoginInfo();
		try {
			sessionMrg = DfClient.getLocalClient().newSessionManager();
			//TODO Change the User Name according to your docbase
			loginInfo.setUser("dctmgurus");
			//TODO Change the Password according to your requirement
			loginInfo.setPassword("dctmgurus");
			//TODO Change the docbase name
			sessionMrg.setIdentity("dctm", loginInfo);
		} catch (DfException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//This method will return the Documentum Session
	public IDfSession getSession(String repoName) throws DfException{
		return sessionMrg.getSession(repoName);
	}
	
	//This method will release the session
	public void releaseSession(IDfSession session) {
		
		if(session != null)
		{
			session.getSessionManager().release(session);
		}
	}
	
	//This method will return the Documentum Session for the dctm repository
	public IDfSession getSession() throws DfException{
		return getSession("dctm");
	}
	
	//Main method. Starting point if the job
	public static void main(String[] args){
		new CreateUser().execute();
	}
}
