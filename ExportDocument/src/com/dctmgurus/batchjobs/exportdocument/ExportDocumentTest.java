package com.dctmgurus.batchjobs.exportdocument;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;

public class ExportDocumentTest {

	private IDfSessionManager sessionMrg = null;
	
	public void execute(){
		IDfSession session = null;
		IDfSysObject sysObj = null;
		try {
			//Initializing the Session Manager
			init();
			
			//Getting the Session
			session = getSession();
			
			//Creating a IDfSysObject object for the document to be exported
			sysObj = (IDfSysObject) session.getObject(new DfId("09XXXXXXXXXXXXXX"));
			
			//Creating the Export operation object
			IDfExportOperation operation = new DfClientX().getExportOperation();
			
			//Setting the Destination directory to which the Documents will be exported to
			operation.setDestinationDirectory("C:\\temp\\destination");
			
			//Adding the object(to be exported)to the Export Operation
			//More than one document can be added, just repeat the below step for it
			IDfExportNode node = (IDfExportNode) operation.add(sysObj);
			
			//Setting the Format of the document
			node.setFormat(sysObj.getFormat().getName());
			
			//Exporting the document
			operation.execute();
			
			//Getting the filepath and filename
			String fileName = node.getFilePath();
			
			//Printing the filepath and filename
			System.out.println(fileName);
			
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
		new ExportDocumentTest().execute();
	}
}
