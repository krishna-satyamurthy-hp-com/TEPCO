package jp.co.tepco.kpfbpf.livesite.common.webapp.auth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * 認可を処理するクラスです。 <br>
 * 設定ファイルで以下を指定可能です。<br>
 * <br>
 * <table border="1">
 * <tr>
 * <th>プロパティ</th>
 * <th>説明</th>
 * <th>備考</th>
 * </tr>
 * <tr>
 * <td>getAuthDataFromAPIURLPrefixes</td>
 * <td>認可情報をAPIから取得するサイトのURLプリフィックスを、カンマ区切りで複数指定可能。</td>
 * <td>任意。未指定の場合、すべてのサイトのリクエストヘッダから認可情報が取得できるとみなす。</td>
 * </tr>
 * <tr>
 * <td>getAuthInfoAPI</td>
 * <td>ユーザの認可情報を取得するAPIのID</td>
 * <td>APIから認可情報を取得するサイトでは必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>rolePropertyName</td>
 * <td>
 * 認可情報取得APIのレスポンスからロールを取得する際のプロパティ名を指定する</td>
 * <td>APIから認可情報を取得するサイトでは必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>apiKeyPropertyName</td>
 * <td>
 * 認可情報取得APIのレスポンスからAPIKeyを取得する際のプロパティ名を指定する</td>
 * <td>APIから認可情報を取得するサイトでは必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>roleHeaderName</td>
 * <td>
 * リクエストからロールを取得する際のヘッダ名を指定する</td>
 * <td>リクエストヘッダから認可情報を取得するサイトでは必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>apiKeyHeaderName</td>
 * <td>
 * リクエストからAPIKeyを取得する際のヘッ名ダを指定する</td>
 * <td>リクエストヘッダから認可情報を取得するサイトでは必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>authDataFileName</td>
 * <td>
 * アクセス可能領域とロールのマッピングファイルの場所を指定する。</td>
 * <td>必須。未指定の場合、エラーになる。<br>
 * 認可設定ファイルの記述形式は、「【URLプリフィックス】【タブ】【カンマ区切りのロール】」。<br>
 * 以下に例を示します。<br>
 * <br>
 *
 * <pre>
 * /aaa		role1,role2
 * /aaa/bbb	role2
 * /		*
 * </pre>
 *
 * この場合、「/」はロールを持っていなくてもアクセスでき、「/aaa」はrole1とrole2が、「/aaa/bbb」
 * はrole2のみがアクセス可能となります。</td>
 * </tr>
 * </table>
 */
public class AuthorizationManager {
	private static Logger logger = Logger.getLogger(AuthorizationManager.class);

	/**
	 * key=URL, value=アクセス可能なロールのリストを保持したマップ。
	 */
	private static Map<String, List<String>> authorizationMap = new HashMap<String, List<String>>();

	/**
	 * authorizationMapのキーを、長い順にソートしたもの。
	 */
	private static List<String> urlList = new ArrayList<String>();

	/**
	 * 認可設定ファイルの最終更新日時
	 */
	private static long lastModified = 0L;

	/**
	 * 最後に認可ファイルをチェックした日時
	 */
	private static long lastChecked = 0L;

	/**
	 * 現在のユーザが、アクセスしているURLにアクセス可能であればtrue、アクセス不可であればfalseを返します。<br>
	 * 詳細仕様は以下のとおりです。<br>
	 * <br>
	 * <ol>
	 * <li>ロールとAPIキーをセッションから取得します。</li>
	 * <li>セッションになければ、「getAuthDataFromAPIURLPrefixes」の設定に応じてロールとAPIキーを取得し、
	 * セッションに保存します。<br>
	 * （「 getAuthDataFromAPIURLPrefixes」はカンマ区切りで複数指定可能です）</li>
	 * <ol>
	 * <li>現在アクセスしているURLが「getAuthDataFromAPIURLPrefixes」で設定しているprefixで始まる場合：<br>
	 * APIを使用してロールとAPIキーを取得します。<br>
	 * ロールは「rolePropertyName」から取得し、APIキーは「apiKeyPropertyName」から取得します。</li>
	 * <li>始まらない場合：<br>
	 * リクエストヘッダからロールとAPIキーを取得します。<br>
	 * ロールは「roleHeaderName」から取得し、APIキーは「apiKeyHeaderName」から取得します。</li>
	 * </ol>
	 * <li>「authDataFileName」で指定された認可設定ファイルを読み込んで内容をメモリに展開し、アクセスチェックを行います。<br>
	 * 展開の際には、 １分に１回認可設定ファイルのタイムスタンプをチェックした上で、変更がある場合にのみ行います。</li> <li>
	 * 認可設定ファイルの記述形式は、「【URLプリフィックス】【タブ】【カンマ区切りのロール】」です。<br>
	 * 以下に例を示します。<br>
	 * <br>
	 *
	 * <pre>
	 * /aaa		role1,role2
	 * /aaa/bbb	role2
	 * /		*
	 * </pre>
	 *
	 * この場合、「/」はロールを持っていなくてもアクセスでき、「/aaa」はrole1とrole2が、「/aaa/bbb」
	 * はrole2のみがアクセス可能となります。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @return 現在のユーザが現在のURLにアクセス可能ならtrue、アクセス不可ならfalse。
	 */
	public static boolean isAuthorized(HttpServletRequest request) {
		logger.debug("isAuthorized開始。");
		try {
			boolean authOK = false;

			ResourceBundle bundle = ResourceBundle
					.getBundle("AuthorizationManager");

			// ロールとAPIキーをセッションから取得する。なければ、しかるべき方法で取得してセッションにセットする。
			String role = SessionManager.getRole(request);
			String apiKey = SessionManager.getAPIKey(request);

			if (role == null || apiKey == null) {
				boolean getAutDataFromAPI = false;
				String getAuthDataFromAPIURLPrefixes = null;
				try {
					getAuthDataFromAPIURLPrefixes = bundle
							.getString("getAuthDataFromAPIURLPrefixes");
				} catch (MissingResourceException mre) {
					// 任意設定のため、握りつぶす。
				}
				if (getAuthDataFromAPIURLPrefixes != null) {
					String[] settings = getAuthDataFromAPIURLPrefixes
							.toLowerCase().split(",");
					String requestURI = request.getRequestURI().toLowerCase();
					for (String urlPrefix : settings) {
						if (requestURI.startsWith(urlPrefix)) {
							getAutDataFromAPI = true;
						}
					}
				}

				if (getAutDataFromAPI) {
					logger.debug("ロールとAPIキーがセッションに存在しないため、APIより取得します。");
					// ロールとAPIキーをAPIから取得する。
					String getAuthInfoAPI = bundle.getString("getAuthInfoAPI");

					RESTClient client = new RESTClient(request, getAuthInfoAPI);

					Map<String, Object> data = client.callAPI();

					String rolePropertyName = bundle
							.getString("rolePropertyName");
					String apiKeyPropertyName = bundle
							.getString("apiKeyPropertyName");
					role = (String) data.get(rolePropertyName);
					apiKey = (String) data.get(apiKeyPropertyName);
				} else {
					logger.debug("ロールとAPIキーがセッションに存在しないため、リクエストヘッダより取得します。");
					// ロールとAPIキーをリクエストヘッダから取得する。
					String roleHeaderName = bundle.getString("roleHeaderName");
					role = request.getHeader(roleHeaderName);

					String apiKeyHeaderName = bundle
							.getString("apiKeyHeaderName");
					apiKey = request.getHeader(apiKeyHeaderName);
				}
				SessionManager.setRole(request, role);
				SessionManager.setAPIKey(request, apiKey);

			}
			logger.debug("ロール=" + role + "／APIキー=" + apiKey);

			// 認可設定の読み込み
			String authDataFileName = bundle.getString("authDataFileName");
			File authDataFile = new File(authDataFileName);

			String currentURL = request.getRequestURI().toLowerCase();
			logger.debug("現在アクセス中のURL=" + currentURL);

			synchronized (authorizationMap) {
				// 認可設定ファイルの最終更新日時が一定以上過ぎていた場合、読み直す。
				if (System.currentTimeMillis() > lastChecked + 60 * 1000
						&& authDataFile.lastModified() != lastModified) {
					logger.debug("認可設定ファイル「" + authDataFileName + "」を読み直します。");
					lastChecked = System.currentTimeMillis();
					lastModified = authDataFile.lastModified();
					authorizationMap = new HashMap<String, List<String>>();
					urlList = new ArrayList<String>();
					try {
						List<String> lines = FileUtils.readLines(new File(
								authDataFileName));
						for (String string : lines) {
							if (!string.isEmpty() && string.contains("\t")) {
								String[] splitString = string.split("\t");
								String url = splitString[0];
								String authRoles = splitString[1];
								String[] authRoleArray = authRoles.split(",");
								List<String> authRoleList = Arrays
										.asList(authRoleArray);
								authorizationMap.put(url, authRoleList);
							}
						}
						logger.debug("認可情報マップ=" + authorizationMap);

						urlList.addAll(authorizationMap.keySet());
						Collections.sort(urlList);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// URLに対し、当該ロールがアクセス可能かをチェックする

				for (int i = urlList.size() - 1; i >= 0; i--) {
					String checkURL = urlList.get(i).toLowerCase();
					if (currentURL.startsWith(checkURL)) {
						List<String> authorizedRoles = authorizationMap
								.get(checkURL);
						if (authorizedRoles.contains(role)
								|| authorizedRoles.contains("*")) {
							logger.debug("現在のロール「" + role + "」は「" + checkURL
									+ "」に対するアクセス許可を持っています。");
							authOK = true;
							break;
						} else {
							logger.debug("現在のロール「" + role + "」は「" + checkURL
									+ "」に対するアクセス許可を持っていません。");
							authOK = false;
							break;
						}
					}
				}

			}
			return authOK;
		} catch (MissingResourceException mre) {
			logger.error("設定に不備があります。", mre);
			throw new ServerException();
		}
	}
}
