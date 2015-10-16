package com.tepco.oden.externals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.springframework.context.ApplicationContext;

import com.hp.proserv.mo.LSCSClientFactory;
import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.runtime.RequestContext;
import com.interwoven.wcm.lscs.Client;

public class SampleExternal {
	
	/** logger */
	  private static final transient Log LOGGER = LogFactory.getLog(SampleExternal.class);
	  
	  public Document testLSCS(RequestContext context){
		  Document doc = Dom4jUtils.newDocument();
		  doc.addElement("message").addCDATA("Hello World!");		  
			try{
				final ApplicationContext applicationContext = context.getApplicationContext();				
				System.out.println("Application context object "+applicationContext);
				LSCSClientFactory lscsClientFactory = (LSCSClientFactory) applicationContext.getBean("lscsClientFactory");
				LOGGER.error("This is error level logger");
				System.out.println("lscs host "+lscsClientFactory.getLscsConnectionProperties().getProperty("lscs.host"));				
				final Client lscsClient = lscsClientFactory.getLSCSClient();
				System.out.println("lscs client "+lscsClient);
				System.out.println("Available LSCS Projects");
				final String[] allLSCSProjects = lscsClient.getAllProjects();
				
				for (String lscsProject : allLSCSProjects){
					LOGGER.error("LSCS Project : "+lscsProject);
				}
				LOGGER.info("This is info");
				LOGGER.debug("This is debug");
			
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		  return doc;
	  }

}
