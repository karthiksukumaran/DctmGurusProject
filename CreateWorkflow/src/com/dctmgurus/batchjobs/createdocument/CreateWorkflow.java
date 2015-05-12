package com.dctmgurus.batchjobs.createdocument;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfActivity;
import com.documentum.fc.client.IDfProcess;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfWorkflow;
import com.documentum.fc.client.IDfWorkflowBuilder;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfList;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfList;
import com.documentum.fc.common.IDfLoginInfo;

public class CreateWorkflow {

	private IDfSessionManager sessionMrg = null;
	
	public void execute(){
		IDfSession session = null;
		try {
			//Initializing the Session Manager
			init();
			
			//Getting the Session
			session = getSession();
			
			//Getting the Process(dm_process) object
			IDfProcess workflowfTemplate = (IDfProcess) session.getObjectByQualification("dm_process where object_name='SimpleWorkflow' and r_definition_state=2");
			
			//Creating a Workflow instance
			IDfWorkflowBuilder bldr = session.newWorkflowBuilder(workflowfTemplate.getId("r_object_id"));
			
			//Getting the created workflow
			IDfWorkflow workflow = bldr.getWorkflow();
			
			//Setting the Supervisor name, usually the installation owner
			workflow.setSupervisorName("dctmgurus");
			
			//Initializing the workflow
			bldr.initWorkflow();
			
			//Staring the workflow
			String wfId = bldr.runWorkflow().toString();
			
			//Printing the Workflow Id
			System.out.println(" Workflow Id : "+wfId);
			
			//Adding the object Id of the document that is to be attached to the workflow
			IDfList list = new DfList();
			list.append(new DfId("09XXXXXXXXXXXXXX"));
			
			//Getting the first activity of the workflow
			IDfActivity theActivity = (IDfActivity)session.getObject( bldr.getStartActivityIds().getId(0) );
			
			//Attaching the document to the First activity of the workflow
			workflow.addPackage(theActivity.getObjectName(),theActivity.getPortName(0),theActivity.getPackageName(0),"dm_document",null,true,list);
			
		} catch (Exception e) {
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
		new CreateWorkflow().execute();
	}
}
