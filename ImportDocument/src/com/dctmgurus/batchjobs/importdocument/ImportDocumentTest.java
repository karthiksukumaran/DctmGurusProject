package com.dctmgurus.batchjobs.importdocument;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfList;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfImportNode;
import com.documentum.operations.IDfImportOperation;

public class ImportDocumentTest {

	private IDfSessionManager sessionMrg = null;
	
	
	public void execute(){
		IDfSession session = null;
		IDfDocument document = null;
		IDfImportOperation importOperation = null;
		IDfImportNode node = null;
		String folderId = null;
		try {
			//Initializing the Session Manager
			init();
			
			//Getting the Session
			session = getSession();
			
			//Getting the ImportOperation from the DfClientX
			importOperation = new DfClientX().getImportOperation();
			
			//Setting the session
			importOperation.setSession(session);
			
			//Setting the new.txt document present in the C:\\temp\\ for Import
			node = (IDfImportNode)importOperation.add("c:\\temp\\new.txt");
			
			//Setting the Type in which the document will be inported to
			//You can specify your Custom Type here
			node.setDocbaseObjectType("dm_document");
			
			//Setting Object Name for the document
			node.setNewObjectName("new.txt");
			
            //Importing the Document. Will return True if it is successful, False if the import operation fails
            boolean executeFlag = importOperation.execute();
            
            if (executeFlag)
            {
            	//Getting the List of documents that are successfully Imported
            	//Here in this case only one document is Imported, so only one value will be
            	//present, If multiple document are imported then more than one document will be 
            	//present in the List
	            IDfList newObjLst = importOperation.getNewObjects();
	            
	            //Getting the First document from the List
	            document = (IDfDocument)newObjLst.get(0);
	            
	            //Printing the Object Id of the document
	            System.out.println("Object Id : "+document.getObjectId().getId());
	            
	            //Getting the default folder's Id the document is linked to
	            folderId = document.getFolderId(0).getId();
	            
	            //Setting the Title for the document
	            document.setTitle("DCTM Gurus Import Document");
	            
	            //Setting the Subject for the document
	            document.setString("subject", "DCTM Gurus Import Document");
	            
	            //Linking to dctmgurus Cabinet
	            document.link("/dctmgurus");
	            
	            //Unlinking the Default Folder
	            document.unlink(folderId);
	            
	            //Saving the Document
	            document.save();
	            
            } else {
            	System.out.println("Import Operation Failed");
            }
            
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
		new ImportDocumentTest().execute();
	}
}
