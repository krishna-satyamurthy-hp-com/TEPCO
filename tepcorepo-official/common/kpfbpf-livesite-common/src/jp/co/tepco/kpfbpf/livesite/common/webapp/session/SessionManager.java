package jp.co.tepco.kpfbpf.livesite.common.webapp.session;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.co.tepco.kpfbpf.livesite.common.webapp.form.FormManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.interwoven.livesite.runtime.RequestContext;

/**
 * セッションの操作に関するメソッドを提供します。また、フォームデータもこのクラスでセッションに管理します。<br>
 * セッションに対する操作は基本的にはこのクラスを経由して行ない、直接{@link HttpSession}を操作することは極力避けてください。<br>
 * <font color="red">もしどうしてもセッションに直接操作が必要な場合は、
 * {@link RequestContext#getSession()}経由で取得するのではなく、
 * {@link RequestContext#getRequest()}から{@link HttpServletRequest}を取得した上で、
 * {@link HttpServletRequest#getSession()}から操作してください。</font><br>
 * <br>
 */
public class SessionManager {

	private static final String KEY_USER_ID = "userId";
	private static final String KEY_ROLE = "role";
	private static final String KEY_API_KEY = "apiKey";
	private static final String KEY_FORM_DATA = "formData";
	private static final String KEY_FORM_ID_MAP = "formIdMap";
	private static final String KEY_USER_INFO = "userInfo";
	private static Logger logger = Logger.getLogger(SessionManager.class);

	/**
	 * ユーザIDをセッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param userID
	 *            保存するユーザID
	 */
	public static void setUserId(HttpServletRequest request, String userID) {
		request.getSession().setAttribute(KEY_USER_ID, userID);
	}

	/**
	 * ロールをセッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param role
	 *            保存するロール
	 */
	public static void setRole(HttpServletRequest request, String role) {
		request.getSession().setAttribute(KEY_ROLE, role);
	}

	/**
	 * APIキーをセッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param apiKey
	 *            保存するAPIキー
	 */
	public static void setAPIKey(HttpServletRequest request, String apiKey) {
		request.getSession().setAttribute(KEY_API_KEY, apiKey);
	}

	/**
	 * ユーザの基本情報をセッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param data
	 *            保存するユーザ基本情報
	 */
	public static void setUserInfo(HttpServletRequest request,
			Map<String, Object> data) {
		request.getSession().setAttribute(KEY_USER_INFO, data);
	}

	/**
	 * POSTされたすべてのデータを、{@link HttpServletRequest#getParameterMap()}
	 * で取得してセッションに保存します。<br>
	 * また、セッションにフォームIDを保存します。<br>
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 */
	public static void setFormData(HttpServletRequest request) {
		logger.debug("setFormData!");
		Map<String, Object> copyOfParameterMap = new HashMap<String, Object>();
		copyOfParameterMap.putAll(request.getParameterMap());
		request.getSession().setAttribute(KEY_FORM_DATA, copyOfParameterMap);
	}

	/**
	 * セッションに保存されているユーザIDを取得します。情報がなかったり、空文字列の場合はnullが返ります。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return セッションに保存されているユーザID。ない場合はnull。
	 */
	public static String getUserId(HttpServletRequest request) {
		String userId = null;
		try {
			userId = (String) request.getSession().getAttribute(KEY_USER_ID);
			if (userId.isEmpty()) {
				userId = null;
			}
		} catch (Exception e) {
			// エラーがあった場合は握りつぶしてnullを返す
		}
		return userId;
	}

	/**
	 * セッションに保存されているロールを取得します。情報がなかったり、空文字列の場合はnullが返ります。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return セッションに保存されているロール。ない場合はnull。
	 */
	public static String getRole(HttpServletRequest request) {
		String role = null;
		try {
			role = (String) request.getSession().getAttribute(KEY_ROLE);
			if (role.isEmpty()) {
				role = null;
			}
		} catch (Exception e) {
			// エラーがあった場合は握りつぶしてnullを返す
		}
		return role;
	}

	/**
	 * セッションに保存されているAPIキーを取得します。情報がなかったり、空文字列の場合はnullが返ります。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return セッションに保存されているAPIキー。ない場合はnull。
	 */
	public static String getAPIKey(HttpServletRequest request) {
		String apiKey = null;
		try {
			apiKey = (String) request.getSession().getAttribute(KEY_API_KEY);
			if (apiKey.isEmpty()) {
				apiKey = null;
			}
		} catch (Exception e) {
			// エラーがあった場合は握りつぶしてnullを返す
		}
		return apiKey;
	}

	/**
	 * セッションに保存されているユーザの基本情報を取得します。情報がなかったり、空の場合はnullが返ります。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return セッションに保存されているユーザの基本情報。ない場合はnull。
	 */
	public static Map<String, Object> getUserInfo(HttpServletRequest request) {
		Map<String, Object> userInfo = null;
		try {
			userInfo = (Map<String, Object>) request.getSession().getAttribute(
					KEY_USER_INFO);
			if (userInfo.isEmpty()) {
				userInfo = null;
			}
		} catch (Exception e) {
			// エラーがあった場合は握りつぶしてnullを返す
		}
		return userInfo;
	}

	/**
	 * セッションに保存されているフォームデータを取得します。フォームデータがなかったり、空の場合はnullが返ります。<br>
	 * 取得の際には、以下のチェックが入ります。<br>
	 * <ul>
	 * <li>セッションに保存されている現在のフォームデータのフォームIDが、 リクエストから送られてきたフォームIDと一致しているか？</li>
	 * <li>引数で渡されたキー（クラス名を推奨）が、フォームIDを発行した際のキーと一致するか？</li>
	 * </ul>
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param requestFormId
	 *            リクエストから送られてきたフォームID
	 * @param key
	 *            フォームIDを発行した際のキー
	 * @return セッションに保存されているフォームデータ。取得できない場合はnull。
	 */
	public static Map<String, String[]> getFormData(HttpServletRequest request,
			String requestFormId, String key) {
		Map<String, String[]> formData = null;
		String sessionFormId = null;
		try {
			Map<String, String> formIdMap = (Map<String, String>) request
					.getSession().getAttribute(KEY_FORM_ID_MAP);
			sessionFormId = formIdMap.get(key);

			logger.debug("sessionFormId=" + sessionFormId);
			logger.debug("requestFormId=" + requestFormId);

			String debugText = new Date() + " " + request.getRequestURL() + "?"
					+ request.getQueryString() + "\n";
			debugText += "sessionFormId=" + sessionFormId + "\n";
			debugText += "requestFormId=" + requestFormId + "\n";

			if (sessionFormId.equals(requestFormId)) {
				formData = (Map<String, String[]>) request.getSession()
						.getAttribute(KEY_FORM_DATA);
				logger.debug("formData=" + formData);
				debugText += "equals! formData=" + formData + "\n";
				if (formData.isEmpty()) {
					formData = null;
				}
			}
			String pre = "";
			try {
				pre = FileUtils.readFileToString(new File(
						"/tmp/toda/session.log")) + "\n";
			} catch (Exception e) {

			}
			FileUtils.writeStringToFile(new File("/tmp/toda/session.log"), pre
					+ debugText, "utf-8");
		} catch (Exception e) {
			// エラーがあった場合は握りつぶしてnullを返す
			logger.error("getFormDataでエラー", e);
		}
		return formData;
	}

	/**
	 * セッションに保存されているユーザ基本情報を削除します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 */
	public static void removeUserInfo(HttpServletRequest request) {
		request.getSession().removeAttribute(KEY_USER_INFO);
	}

	/**
	 * セッションに保存されている、指定したフォームマネージャのフォームデータを削除します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param formManager
	 *            削除するフォームマネージャ
	 */
	public static void removeFormData(HttpServletRequest request,
			FormManager formManager) {
		logger.debug("removeFormData開始。");
		Map<String, String[]> formData = getFormData(request,
				formManager.getFormId(), formManager.getBizObject().getClass()
						.getName());
		if (formData != null) {
			request.getSession().removeAttribute(KEY_FORM_DATA);
		}
	}

	/**
	 * 指定したキー（クラス名を推奨）に紐づくフォームIDを発行し、セッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param key
	 *            キー
	 * @return 生成されたフォームID
	 */
	public static String generateFormId(HttpServletRequest request, String key) {
		String formId = RandomStringUtils.randomAlphanumeric(20);
		Map<String, String> formIdMap = null;
		formIdMap = (Map<String, String>) request.getSession().getAttribute(
				KEY_FORM_ID_MAP);
		if (formIdMap == null) {
			formIdMap = new HashMap<String, String>();
			request.getSession().setAttribute(KEY_FORM_ID_MAP, formIdMap);
		}
		formIdMap.put(key, formId);
		return formId;
	}

	/**
	 * セッションを再生成します。<br>
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 */
	public static void recreateSession(HttpServletRequest request) {
		request.getSession().invalidate();
		request.getSession();
	}

	/**
	 * デバッグ用。セッションの中身をすべてダンプします。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return セッションの中身をダンプした文字列
	 */
	public static String dumpSession(HttpServletRequest request) {
		String dump = "<table border=1>";
		HttpSession session = request.getSession();
		Enumeration attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = (String) attributeNames.nextElement();
			dump = dump + "<tr><td>" + attributeName + "</td>";
			Object attribute = session.getAttribute(attributeName);
			if (attribute == null) {
				dump = dump + "<td>null</td></tr>";
			} else {
				if (attributeName.equals("formData")) {
					dump = dump + "<td>{";
					Map<String, String[]> map = (Map<String, String[]>) attribute;
					Set<String> keySet = map.keySet();
					for (String key : keySet) {
						String[] value = map.get(key);
						dump = dump + key + "=";
						if (value == null) {
							dump = dump + "null,";
						} else {
							dump = dump + value[0] + ",";
						}

					}
					dump = dump + "}</td></tr>";
				} else {
					dump = dump + "<td>" + attribute.toString() + "</td></tr>";
				}
			}
		}
		return dump + "</table>";
	}

}
