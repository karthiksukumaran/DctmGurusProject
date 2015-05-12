package com.dctmgurus.session;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

public class CreateSession {

	public static void main(String[] args)
	{
		IDfSessionManager sessionMrg = null;
		IDfSession session = null;
		try {
			IDfLoginInfo loginInfo = new DfLoginInfo();
			loginInfo.setUser("dctmguru");
			loginInfo.setPassword("dctmguru");
			
			sessionMrg = DfClient.getLocalClient().newSessionManager();
			//Here "documentum" is the repository name, Replace with your repository name
			sessionMrg.setIdentity("documentum", loginInfo);
			//Here "documentum" is the repository name, Replace with your repository name
			session = sessionMrg.getSession("documentum");
		} catch (DfException e) {
			e.printStackTrace();
		} finally {
			if(sessionMrg != null && session != null) {
				sessionMrg.release(session);
			}
		}
	}
}
