package jp.co.tepco.kpfbpf.livesite.common.webapp.restclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.auth.AuthenticationManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.AuthorizationException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.form.FormManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * RestAPIをコールするクライアントです。<br>
 * 以下のように使用します。<br>
 * <br>
 *
 * <pre>
 * // APIIDを指定してクライアントを生成。
 * // 3つ目の引数で指定した{@link FormManager}のセッションに保存されているフォームデータが、APIコール成功時に削除される。
 * // フォームデータを扱わないRESTコールの場合は、{@link RESTClient#RESTClient(HttpServletRequest,
 * // String)}を使用してもよい。
 * RESTClient client = new RESTClient(request, apiID, formManager);
 *
 * // 【任意】デフォルトでは、以下は実行しなくても現在のユーザのAPIキーもしくは設定ファイルのAPIキーが自動的に使用される。
 * // もしそれら以外のAPIキーを使いたい場合は、以下のように明示的に指定することが可能。
 * client.setAPIKey(apiKey);
 *
 * // 【任意】送信したいデータがある場合は、以下のようにセットする。
 * // APIがgetの場合はクエリストリングが付与され、postの場合はjsonとして送信される。
 * client.setPostData(data);
 *
 * // 【任意】デフォルトではURL内のいくつかの動的パラメータは自動的に埋められるが、それをオーバーライドしたい場合は、下記で指定可能。
 * client.setUrlReplacement(urlReplacement);
 *
 * // 【任意】APIコール成功時にセッションに保存されているユーザの基本情報をクリアするかどうかを指定する。
 * // デフォルトではfalse。基本情報更新画面などではtrueにしておくことで、APIコール後に更新されたユーザの基本情報が再読み込みされる。
 * client.setRefreshUserInfoOnSuccess(true);
 *
 * // APIをコールすると、結果がMapで取得できる。エラー発生時には例外が発生するので、然るべき対応を行なう。
 * Map&lt;String, Object&gt; responseData = client.callAPI();
 *
 * </pre>
 *
 * 設定ファイルで以下を指定可能です。<br>
 * <br>
 * <table border="1">
 * <tr>
 * <th>プロパティ</th>
 * <th>説明</th>
 * <th>備考</th>
 * </tr>
 * <tr>
 * <td>jsonFilePath</td>
 * <td>テスト用のJSONファイルを配置するフォルダ。</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>api_【APIのID】</td>
 * <td>APIの種別（getやpostなど）、URL、任意のAPIKeyを指定する。「api_」の後にAPIのIDを指定する。</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>doNotReplaceURL</td>
 * <td>trueにすると、URL内の{}の動的置換を行なわなくなる。</td>
 * <td>任意。未指定の場合、false</td>
 * </tr>
 * </table>
 */
public class RESTClient {
	private static Logger logger = Logger.getLogger(RESTClient.class);

	private HttpServletRequest request;
	private String apiID;
	private String apiKey;

	private Map<String, Object> postData;
	private Map<String, String> urlReplacement;
	private List<FormManager> deleteSessionFormDataOnSuccess = new ArrayList<FormManager>();
	private boolean refreshUserInfoOnSuccess = false;

	/**
	 * 指定したAPIのRESTClientを作成します。フォームデータを扱うRESTコールにおいて、
	 * APIコール成功時にセッションに保存されたフォームデータを削除する場合は、
	 * {@link RESTClient#RESTClient(HttpServletRequest, String, FormManager)}
	 * を使用してください。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param apiID
	 *            コールしたいAPIのID
	 */
	public RESTClient(HttpServletRequest request, String apiID) {
		this.request = request;
		this.apiID = apiID;
	}

	/**
	 * 指定したAPIのRESTClientを作成します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param apiID
	 *            コールしたいAPIのID
	 * @param deleteSessionFormDataOnSuccess
	 *            このAPIコール成功時にセッションに保存してあるフォームデータを削除したい場合は、その{@link FormManager}
	 *            を渡す。 APIコール後にフォームデータを使わない場合は指定し、なるべくセッションにデータを残さないようにしてください。
	 *            フォームデータを扱わないようなRESTコールの場合は、
	 *            {@link RESTClient#RESTClient(HttpServletRequest, String)}
	 *            を使用してください。
	 */
	public RESTClient(HttpServletRequest request, String apiID,
			FormManager deleteSessionFormDataOnSuccess) {
		this.request = request;
		this.apiID = apiID;
		if (deleteSessionFormDataOnSuccess != null) {
			this.deleteSessionFormDataOnSuccess
					.add(deleteSessionFormDataOnSuccess);
		}
	}

	/**
	 * APIをコールし、結果をMapで返します。Mapが返るのは、APIがコールでき、HTTPレスポンスコードが200の場合のみです。<br>
	 * 認可エラーの場合は{@link AuthorizationException}が、500エラーの場合は
	 * {@link ServerException}が返ります。<br>
	 * <br>
	 * APIIDと実際のURLの対応は、プロパティファイルであらかじめ以下の形式でメソッド、URL、APIキー（任意）を指定します。<br>
	 * 例：
	 *
	 * <pre>
	 * api_1=get	/getuserinfo/{AccountID}
	 * api_2=get	/getauthdata	commonAPIkey
	 * api_3=post	/setuserinfo/{AccountID}
	 * </pre>
	 *
	 * <br>
	 * URL内の{}に囲まれた部分は、動的に置換を行います。現状は以下に対応しています。<br>
	 * <table border="1">
	 * <tr>
	 * <th>パラメータ</th>
	 * <th>取得元</th>
	 * </tr>
	 * <tr>
	 * <td>{AccountID}</td>
	 * <td>セッションから現在のユーザIDを取得</td>
	 * </tr>
	 * </table>
	 * この挙動をオーバーライドしたい場合は、{@link RESTClient#setUrlReplacement(Map)}
	 * でパラメータとその値を指定してください。 <br>
	 * 値を指定すると、指定したMapでの置き換えを行なった後、まだ{}が残っているものについて、デフォルトの置き換えを行ないます。<br>
	 * <br>
	 * 現状はスタブであるため、実際にはRESTコールは行いません。<br>
	 * 代わりに、「jsonFilePath」で指定したフォルダにあるテキストファイル（中身はjson）を取得します。<br>
	 * 取得するファイル名は、APIのURLの「/」を「_」に変換したものとなります。<br>
	 * <br>
	 * （例）：以下のURLに、ユーザID0001でアクセスした場合<br>
	 * /getuserinfo/{AccountID}　→　_getuserinfo_0001.txt<br>
	 * <br>
	 * また、送信したパラメータに応じて、JSON内に${}でパラメータを指定していれば、それを動的に置き換えます。<br>
	 * getの場合は{@link RESTClient#setPostData(Map)}で渡したMapのキーに対応するパラメータを置き換えます。<br>
	 * postの場合は、Map内にさらにMapの階層があるとみなし、１つ下の階層のMapのキーに対応するパラメータを置き換えます。<br>
	 * <br>
	 * なお、設定ファイルで「doNotReplaceURL=true」を設定すると、URL内の{}の動的置換を行ないません。<br>
	 * テスト等でユーザ毎にJSONファイルを作成するのが大変な場合は、この設定をしてください。<br>
	 *
	 * @return API呼び出しの結果JSONをMapオブジェクトに変換したもの
	 * @throws AuthorizationException
	 *             認可エラーの場合
	 * @throws ServerException
	 *             500エラーや、その他続行不能なエラーの場合
	 */
	public Map<String, Object> callAPI() throws AuthorizationException,
			ServerException {
		logger.debug("callAPI開始。");
		try {
			Map<String, Object> responseData = new HashMap<String, Object>();

			ResourceBundle bundle = ResourceBundle.getBundle("RESTClient");

			String api = bundle.getString("api_" + apiID);
			String apiSetting[] = api.split("\t");
			String apiType = apiSetting[0].toLowerCase();
			String apiURL = apiSetting[1];
			if (apiKey == null) {
				apiKey = SessionManager.getAPIKey(request);
				if (apiSetting.length > 2) {
					apiKey = apiSetting[2];
				}
			}

			logger.debug(apiType + " " + apiURL + " " + apiKey + "をコールします。");

			if (urlReplacement != null) {
				Set<String> keySet = urlReplacement.keySet();
				for (String key : keySet) {
					apiURL = apiURL.replace("{" + key + "}",
							sanitizePath(urlReplacement.get(key)));
				}
			}

			// ユーザIDを取得し、必要に応じてAPIURLを変換する。
			boolean replaceURL = true;
			try {
				replaceURL = !Boolean.parseBoolean(bundle
						.getString("doNotReplaceURL"));
			} catch (Exception e) {
				// この設定は任意のため、エラーの場合でも握りつぶす
			}
			if (replaceURL) {
				String userID = SessionManager.getUserId(request);
				if (userID != null) {
					apiURL = apiURL
							.replace("{AccountID}", sanitizePath(userID));
				}
			}

			// スタブ特有
			// RESTサービスを呼ぶのではなく、ローカルのファイルからデータを読み取る

			String jsonFilePath = bundle.getString("jsonFilePath");
			apiURL = apiURL.replace("/", "_");
			File jsonFile = new File(jsonFilePath + File.separator + apiURL
					+ ".txt");
			logger.debug(jsonFile.getAbsolutePath() + "を読み取ります。");

			String jsonData = "";

			try {
				jsonData = FileUtils.readFileToString(jsonFile, "Shift_JIS");
				if (postData != null) {
					if (apiType.equals("post")) {
						postData = (Map<String, Object>) postData.values()
								.toArray()[0];
					}
					Set<String> keySet = postData.keySet();
					for (String key : keySet) {
						Object value = postData.get(key);
						if (value != null) {
							jsonData = jsonData.replace("${" + key + "}",
									value.toString());
						}
					}
					logger.debug("jsonData=" + jsonData);
				}
			} catch (IOException e) {
				logger.error(jsonFile.getAbsolutePath() + "読み取り中にエラーが発生しました。",
						e);
				throw new ServerException();
			}

			ObjectMapper mapper = new ObjectMapper();
			try {
				responseData = mapper.readValue(jsonData, Map.class);
			} catch (Exception e) {
				logger.error("JSONをマップに変換する際にエラーが発生しました。", e);
				throw new ServerException();
			}

			int jsonStatus = 0;

			if (jsonStatus == 0) {
				for (FormManager formManager : deleteSessionFormDataOnSuccess) {
					SessionManager.removeFormData(request, formManager);
				}
				if (refreshUserInfoOnSuccess) {
					SessionManager.removeUserInfo(request);
					AuthenticationManager.authenticate(request);
				}
			}

			return responseData;
		} catch (MissingResourceException mre) {
			logger.error("設定に不備があります。", mre);
			throw new ServerException();
		}

	}

	private static String sanitizePath(String string) {
		if (string == null) {
			return null;
		}
		return string.replaceAll("[/\\\\|]", "");
	}

	/**
	 * このRESTClientのAPIのIDを取得します。
	 *
	 * @return このRESTClientのAPIのID
	 */
	public String getApiID() {
		return apiID;
	}

	/**
	 * このRESTClientのAPIのIDをセットします。
	 *
	 * @param apiID
	 *            　セットしたいAPIのID
	 */
	public void setApiID(String apiID) {
		this.apiID = apiID;
	}

	/**
	 * このRESTClientがAPIに対して送信するデータを取得します。
	 *
	 * @return このRESTClientがAPIに対して送信するデータ
	 */
	public Map<String, Object> getPostData() {
		return postData;
	}

	/**
	 * このRESTClientでAPIに対して送信したいデータをセットします。
	 *
	 * @param postData
	 *            このRESTClientでAPIに対して送信したいデータのMap
	 */
	public void setPostData(Map<String, Object> postData) {
		this.postData = postData;
	}

	/**
	 * このRESTCLientのAPIコールが成功した場合、ユーザの基本情報を再取得するかどうかを取得します。
	 *
	 * @return 成功時にユーザの基本情報を再取得する場合はtrue、削除しない場合はfalse
	 */
	public boolean isRefreshUserInfoOnSuccess() {
		return refreshUserInfoOnSuccess;
	}

	/**
	 * このRESTCLientのAPIコールが成功した場合、ユーザの基本情報を再取得するかどうかをセットします。
	 *
	 * @param refreshUserInfoOnSuccess
	 *            成功時にユーザの基本情報を再取得したい場合はtrue、削除しない場合はfalse
	 */
	public void setRefreshUserInfoOnSuccess(boolean refreshUserInfoOnSuccess) {
		this.refreshUserInfoOnSuccess = refreshUserInfoOnSuccess;
	}

	/**
	 * このRESTCLientで行なうURL置換のMapを取得します。
	 *
	 * @return このRESTCLientで行なうURL置換のMap
	 */
	public Map<String, String> getUrlReplacement() {
		return urlReplacement;
	}

	/**
	 * このRESTCLientで行ないたいURL置換のMapをセットします。
	 *
	 * @param urlReplacement
	 *            このRESTCLientでURL置換をオーバーライドしたい場合、セットする
	 */
	public void setUrlReplacement(Map<String, String> urlReplacement) {
		this.urlReplacement = urlReplacement;
	}

	/**
	 * このRESTClientで使用するAPIキーを取得します。
	 *
	 * @return このRESTClientで使用するAPIキー
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * このRESTClientで使用するAPIキーをオーバーライドしたい場合、セットします。
	 *
	 * @param apiKey
	 *            使用したいAPIキー
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
