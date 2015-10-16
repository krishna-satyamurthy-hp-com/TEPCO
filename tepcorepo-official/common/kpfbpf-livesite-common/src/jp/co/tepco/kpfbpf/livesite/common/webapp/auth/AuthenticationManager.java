package jp.co.tepco.kpfbpf.livesite.common.webapp.auth;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

/**
 * 認証を処理するクラスです。<br>
 * 設定ファイルで以下を指定可能です。<br>
 * <br>
 * <table border="1">
 * <tr>
 * <th>プロパティ</th>
 * <th>説明</th>
 * <th>備考</th>
 * </tr>
 * <tr>
 * <td>userIDHeaderName</td>
 * <td>リクエストから取得するユーザIDのヘッダ名</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>getUserInfoAPI</td>
 * <td>ユーザの基本情報を取得するAPIのID</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>invalidateSessionOnLogOut</td>
 * <td>
 * ユーザがログアウトした際、セッションを再生成するかどうかを指定する。テスト環境でIceWallが前段にない場合はリクエストヘッダが常に送信されないため
 * 、falseを指定するとテストが可能となる。</td>
 * <td>任意。未指定の場合、true</td>
 * </tr>
 * </table>
 *
 */
public class AuthenticationManager {

	private static Logger logger = Logger
			.getLogger(AuthenticationManager.class);

	/**
	 * 「userIDHeaderName」で指定したリクエストヘッダのユーザIDとセッションに保存されているユーザIDを比較し、以下の処理を行います。<br>
	 * <table border="1">
	 * <tr>
	 * <th>リクエストヘッダのユーザID</th>
	 * <th>セッションのユーザID</th>
	 * <th>処理</th>
	 * </tr>
	 * <tr>
	 * <td>あり</td>
	 * <td>なし</td>
	 * <td>ユーザがログインしたとみなし、セッションにユーザIDをセットする。</td>
	 * </tr>
	 * <tr>
	 * <td>なし</td>
	 * <td>あり</td>
	 * <td>ユーザがログアウトしたとみなす。「invalidateSessionOnLogOut」がtrueの場合、セッションを再生成する。</td>
	 * </tr>
	 * <tr>
	 * <td>あり</td>
	 * <td>あるが、リクエストヘッダと値が異なる</td>
	 * <td>ユーザが異なるユーザでログインしたとみなす。セッションを再生成し、セッションにユーザIDをセットする。</td>
	 * </tr>
	 * </table>
	 * 上記処理実施後、セッションにユーザの基本情報が存在するかを確認し、なければ「getUserInfoAPI」のAPIをコールして基本情報を取得し、
	 * セッションに保存します。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 */
	public static void authenticate(HttpServletRequest request) {

		logger.info("authenticate開始。");
		try {
			ResourceBundle bundle = ResourceBundle
					.getBundle("AuthenticationManager");

			// リクエストヘッダのユーザIDとセッションのユーザIDを比較する。
			String userIDHeaderName = bundle.getString("userIDHeaderName");
			String headerUserId = request.getHeader(userIDHeaderName);
			if (headerUserId != null && headerUserId.equals("")) {
				headerUserId = null;
			}
			String sessionUserId = SessionManager.getUserId(request);

			logger.info("リクエストヘッダ上のユーザID:" + headerUserId);
			logger.info("セッション上のユーザID:" + sessionUserId);

			String debugText = "";
			debugText += new Date() + " " + request.getRequestURL() + "?"
					+ request.getQueryString() + "\n";
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				String value = request.getHeader(headerName);
				debugText += headerName + "=" + value + "\n";
			}

			debugText += "file appenders::\n";
			Enumeration appenders = logger.getAllAppenders();
			while (appenders.hasMoreElements()) {
				Appender appender = (Appender) appenders.nextElement();
				debugText += appender.toString() + " / name="
						+ appender.getName() + "\n";
				if (appender instanceof FileAppender) {
					FileAppender fileAppender = (FileAppender) appender;
					debugText += fileAppender.getFile() + "\n";

				}

			}

			debugText += "リクエストヘッダ上のユーザID:" + headerUserId + "\n";
			debugText += "セッション上のユーザID:" + sessionUserId + "\n";
			try {
				String pre = "";
				try {
					pre = FileUtils.readFileToString(new File(
							"/tmp/toda/authentication.log")) + "\n";
				} catch (Exception e) {

				}
				FileUtils.writeStringToFile(new File(
						"/tmp/toda/authentication.log"), pre + debugText,
						"utf-8");
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			if (headerUserId != null && sessionUserId == null) {
				logger.info("新規ログインとして扱います。");
				// ユーザがログインした
				SessionManager.setUserId(request, headerUserId);
			} else if (headerUserId == null && sessionUserId != null) {
				// ユーザがログアウトした場合、セッションを再生成する（設定による）
				boolean invalidateSessionOnLogOut = true;
				try {
					invalidateSessionOnLogOut = Boolean.parseBoolean(bundle
							.getString("invalidateSessionOnLogOut"));
				} catch (Exception e) {
					// この設定は任意なので、設定取得時にエラーになっても握りつぶす
				}
				if (invalidateSessionOnLogOut) {
					logger.info("ログアウトとして扱い、セッションを再生成します。");
					SessionManager.recreateSession(request);
				} else {
					logger.info("ログアウトとしたと思われますが、設定によりセッションの再生成を行いません。");
				}
			} else if (headerUserId != null
					&& !headerUserId.equals(sessionUserId)) {
				logger.info("ユーザ切り替えとして扱います。");
				// セッションを再生成し、セッションのユーザIDを更新する
				SessionManager.recreateSession(request);
				SessionManager.setUserId(request, headerUserId);
			}

			// 基本情報取得・セッションへのセット
			if (SessionManager.getUserId(request) != null
					&& SessionManager.getUserInfo(request) == null) {
				String getUserInfoAPI = bundle.getString("getUserInfoAPI");
				logger.info("セッションに基本情報がないため、API「" + getUserInfoAPI
						+ "」で取得します。");

				RESTClient client = new RESTClient(request, getUserInfoAPI);

				Map<String, Object> apiResponse = client.callAPI();

				SessionManager.setUserInfo(request, apiResponse);
			}
		} catch (MissingResourceException mre) {
			logger.error("設定に不備があります。", mre);
			throw new ServerException();
		}

	}
}
