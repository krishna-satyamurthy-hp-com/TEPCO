package jp.co.tepco.kpfbpf.livesite.sample.webapp;

import java.util.HashMap;
import java.util.Map;

import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.bl.BaseWebPageComponentExternal;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.AuthorizationException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.runtime.RequestContext;

public class ApiExternal extends BaseWebPageComponentExternal {
	// DELETE ME Begin
	// 複数Componentが配置されるページでは、Componentの　の実行順は
	// 保障されないため、Session上に画面遷移状態を保持する場合、
	// Component単位にSessionデータを持つ必要があるか、
	// Page単位で十分か、慎重に検討する必要がある。
	// DELETE ME End
	final static String KEY_OdenSampleExternal4_Mode_Save_ID = "OdenSampleExternal4_Mode_Save_ID";	
	private static final transient Log logger = LogFactory.getLog(ApiExternal.class);
	@SuppressWarnings("deprecation")
	public Document execute( RequestContext context ) {
		logger.info("execute method called... ");
		Document document = null;
		
		final String apiRequest = context.getParameterString("api-request");		
		logger.info("api request parameter is "+apiRequest);
		if(apiRequest!= null && apiRequest != ""){
			try {
				RESTClient importConfigClient = getRESTClientInstance(apiRequest.trim());
				//Map<String, Object> jsonPostData = new HashMap<String, Object>(); //This is for input json request which is banned for now
				final Map<String, Object> responseData = importConfigClient.callAPI();
				logger.info(responseData);
				if(logger.isDebugEnabled()){
					logger.debug("Response data from API "+ responseData);
				}
				if(responseData!=null && !responseData.isEmpty()){
					
					//Convert JavaMap to JSON object
					final JSONObject jsonResponseData = new JSONObject(responseData); 
					//System.out.println("json object "+jsonResponseData);
					if(logger.isInfoEnabled()){
						logger.info("json object "+jsonResponseData);
					}
					//Convert JSON to XML
					final String xmlResponseData = XML.toString(jsonResponseData);				
					logger.info("xmlResponseData : "+xmlResponseData);
					//Append a standard Root element to the response XML as it might come without a root element
					StringBuffer xmlResponseDataSB = new StringBuffer("<APIResponse>");
					xmlResponseDataSB.append(xmlResponseData);
					xmlResponseDataSB.append("</APIResponse>");			
					//System.out.println("xml response data "+xmlResponseDataSB.toString());
					if(logger.isInfoEnabled()){
						logger.info("xml response data "+xmlResponseDataSB.toString());
					}
					//Finally add the xml converted response to document
					document = Dom4jUtils.newDocument(xmlResponseDataSB.toString());
					//System.out.println("final doc object "+document.asXML());
					if(logger.isDebugEnabled()){
						logger.debug("final doc object "+document.asXML());
					}
					
				}else{
					//Cannot send an empty document as it will throw NullPointer on the screen
					document = Dom4jUtils.newDocument();
					document.addElement("APIResponse").addCDATA("No results");
				}
			} catch (AuthorizationException e) {
				// TODO Auto-generated catch block
				if(logger.isErrorEnabled()){
					logger.error(e.getStackTrace());
				}
				//e.printStackTrace();
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				if(logger.isErrorEnabled()){
					logger.error(e.getStackTrace());
				}
				//e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if(logger.isErrorEnabled()){
					logger.error(e.getStackTrace());
				}
				logger.info("Exeception : "+e.getMessage());
			}
		}else{
			//Cannot send an empty document as it will throw NullPointer on the screen
			document = Dom4jUtils.newDocument();
			document.addElement("APIResponse").addCDATA("No api request parameter");
		}
		return document;
	}

	@Override
	protected Document ExecuteInitComponent(RequestContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
