/**
 * 
 */
package com.hp.proserv.mo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.interwoven.wcm.lscs.Client;

/**
 * @author krishkri
 *
 */
public class LSCSServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context;
		try{
			context = new ClassPathXmlApplicationContext("context.xml");			
			
			LSCSClientFactory lscsClientFactory = (LSCSClientFactory) context.getBean("lscsClientFactory");
			System.out.println("lscs host "+lscsClientFactory.getLscsConnectionProperties().getProperty("lscs.host"));
			
			final Client lscsClient = lscsClientFactory.getLSCSClient();
			System.out.println("lscs client "+lscsClient);
			
			//This does not work for some reason
			System.out.println(lscsClient.getAllProjects());
			
			lscsClient.setProject("//kpfbpf-team01/default/main/KPF_Front");
			System.out.println(lscsClient.getDocumentByPath("templatedata/life_kaden_assist/C1086/data/C1086.xml"));
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
