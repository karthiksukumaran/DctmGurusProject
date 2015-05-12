package com.dctmgurus.batchjobs.createdocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

public class CreateDocument {

	private IDfSessionManager sessionMrg = null;
	
	public void execute(){
		IDfSession session = null;
		FileReader reader = null;
		ByteArrayOutputStream outputStream = null;
		int buff = 0;
		try {
			//Initializing the Session Manager
			init();
			
			//Getting the Session
			session = getSession();
			
			//Creating a Reader Object for the file
			reader = new FileReader(new File("C:\\TEMP\\DCTMGurusExample.doc"));
			
			//Initializing the Output stream
			outputStream = new ByteArrayOutputStream();
			
			//Creating the ByteArrayOutputStream for the File
			while((buff = reader.read()) != -1)
			{
				outputStream.write(buff);
			}
			
			
			//Creating the dm_document object
			IDfDocument document = (IDfDocument)session.newObject("dm_document");
			
			//Setting the Object Name for the Document
			document.setObjectName("DCTMGurusExample.doc");
			
			//Setting the Title for the Document
			document.setTitle("DCTM Gurus Create Document Example");
			
			//Setting the Subject for the Document
			document.setSubject("DCTM Gurus Create Document DFC Example");
			
			//Setting the Content type
			//msw8 - is for the MS Word
			//The mapping between the extension and the corresponding Content Type
			//is available in dm_format object
			document.setContentType("msw8");
			
			//Setting the content to the object
			document.setContent(outputStream);
			
			//Linking the Document to /Temp cabinet
			document.link("/Temp");
			
			//Saving the Document Object
			document.save();
			
			
			
		} catch (Exception e) {
			//Printing the Stacktrace
			e.printStackTrace();
		} finally {
			
			if(outputStream != null)
			{
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
		new CreateDocument().execute();
	}
}
