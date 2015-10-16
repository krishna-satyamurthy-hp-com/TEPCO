package jp.co.tepco.kpfbpf.livesite.common.webapp.bl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.form.FormManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.dom4j.Document;
import org.dom4j.Element;

import com.interwoven.livesite.runtime.RequestContext;

public abstract class BaseWebPageComponentExternal {
	
	private HttpServletRequest request;
		
	private String userID;
	
	private String role;
	
	private String apiKey;
	
	private String componentModeParameter;

	private FormManager formManager;
	
	private Map<String, Object> userInfo;
	
	private String formId;
	
	private Map<String, String[]> formData;
	
	
	

	/**
	 * @return request
	 */
	protected HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return userID
	 */
	protected String getUserID() {
		return userID;
	}
	
	/**
	 * @return role
	 */
	protected String getRole() {
		return role;
	}
	
	/**
	 * @return apiKey
	 */
	protected String getApiKey() {
		return apiKey;
	}
	
	/**
	 * @return componentModeParameter
	 */
	protected String getComponentModeParameter() {
		return componentModeParameter;
	}
	
	/**
	 * @return formManager
	 */
	protected FormManager getFormManager() {
		return formManager;
	}
	
	/**
	 * @return userInfo
	 */
	protected Map<String, Object> getUserInfo() {
		return userInfo;
	}
	
	/**
	 * @return formId
	 */
	protected String getFormId() {
		return formId;
	}
	
	/**
	 * @return formData
	 */
	protected Map<String, String[]> getFormData() {
		return formData;
	}
	
	// ------------- Session 操作 -------------------------
	
	protected void StoreObjectToSession( String sessionKey, Object object ){
		getRequest().getSession().setAttribute(sessionKey, object);
	}
	
	protected Object QueryObjectFromSession( String sessionKey ) {
		return getRequest().getSession().getAttribute(sessionKey);
	}
	
	// ------------- API 呼び出し　 -------------------------
	
	protected RESTClient getRESTClientInstance( String apiKey ){
		return new RESTClient(getRequest(), apiKey);
	}
	
	// ------------- FormId　設定　 -------------------------

	protected void addFormIdElement(Element data) {
		// ページ間でフォームデータを共有するため、formIdを引き回す。
		if (getFormId() != null) {
			data.addElement("formId").setText(getFormId());
		}
	}
		
	protected abstract Document ExecuteInitComponent( RequestContext context );
	
	public Document InitComponent(RequestContext context) {
		request = context.getRequest();
		
		// SessionManagerからは、ユーザIDやロール、APIキーの他、ユーザの基本情報が取得できる。
		userID = SessionManager.getUserId(request);
		role = SessionManager.getRole(request);
		apiKey = SessionManager.getAPIKey(request);
		userInfo = SessionManager.getUserInfo(request);

		try {
			// フォームページ、確認ページ、完了ページは、modeパラメータで見分ける。
			componentModeParameter = (String) context.getParameters().get("mode");

			// FormManagerにこのオブジェクト自身を渡し、FormManagerを取得する。
			// FormManagerの詳細は、javadoc参照のこと。
			formManager = new FormManager(request, this);
			formId = formManager.getFormId();
			formData = formManager.getFormData();

			
			return ExecuteInitComponent( context );
		} catch (Exception ex) {
			// TODO
		}
		
		// TODO
		return null;
	}

}
