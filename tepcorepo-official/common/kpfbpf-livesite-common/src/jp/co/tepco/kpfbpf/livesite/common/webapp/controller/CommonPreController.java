package jp.co.tepco.kpfbpf.livesite.common.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.tepco.kpfbpf.livesite.common.webapp.auth.AuthenticationManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.auth.AuthorizationManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.interwoven.livesite.common.web.ForwardAction;
import com.interwoven.livesite.runtime.RequestContext;

/**
 * 共通プリコントローラクラスです。<br>
 * 設定ファイルで以下を指定可能です。<br>
 * <br>
 * <table border="1">
 * <tr>
 * <th>プロパティ</th>
 * <th>説明</th>
 * <th>備考</th>
 * </tr>
 * <tr>
 * <td>saveFormFlag</td>
 * <td>ここで設定した名前のパラメータの値にtrueを指定してフォームが送信されると、プリコントローラーはそのフォームデータをセッションに保存する。</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>error403Page</td>
 * <td>認可エラーの場合にリダイレクトするエラーページ。実際にはサイト毎の設定が必要なため、別の方法で指定することを検討中。</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * <tr>
 * <td>error500Page</td>
 * <td>サーバエラーの場合にリダイレクトするエラーページ。実際にはサイト毎の設定が必要なため、別の方法で指定することを検討中。</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * </table>
 */
public class CommonPreController {
	private static Logger logger = Logger.getLogger(CommonPreController.class);

	/**
	 * ページがレンダリングされる前の処理を行います。<br>
	 * <br>
	 * <ol>
	 * <li>{@link AuthenticationManager#authenticate(HttpServletRequest)}
	 * で認証処理実施。</li>
	 * <li>{@link AuthorizationManager#isAuthorized(HttpServletRequest)}
	 * で認可チェック実施。<br>
	 * 認可エラーの場合、サイト毎に設定された認可エラーページにリダイレクトします。<br>
	 * （現状は「error403Page」 で設定したページにリダイレクトしているが、サイト毎に異なるページにリダイレクトできるよう要修正）</li>
	 * <li>リクエストパラメータに「saveFormFlag」で設定した名前のパラメータが存在し、値がtrueだった場合、
	 * {@link SessionManager#setFormData(HttpServletRequest)}
	 * を呼び出してセッションにデータを保存します。<br>
	 * また、セッションにデータ保存後、サーバ側の基本的なバリデーション処理を行います（未実装）。</li>
	 * </ol>
	 * <br>
	 *
	 * @param context
	 *            LiveSiteのコンテキスト
	 * @return プリコントローラの処理後、そのまま続行できる場合はnull。どこかにリクエストを転送する場合は、その方法を指定した
	 *         {@link ForwardAction}。
	 */
	public ForwardAction handleExecution(RequestContext context) {
		ResourceBundle bundle = ResourceBundle.getBundle("CommonPreController");
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}


		try {
			String debugText = "";
			debugText += new Date() + "\n";
			debugText += "PreController called!\n";
			FileUtils
					.writeStringToFile(new File("/tmp/toda/precontroller.log"),
							debugText, "utf-8");

			// 認証チェック
			AuthenticationManager.authenticate(request);

			// 認可チェック
			if (!AuthorizationManager.isAuthorized(request)) {
				// 認可NG
				logger.warn("認可エラーです。");
				String error403Page = bundle.getString("error403Page");
				try {
					response.sendRedirect(request.getContextPath()
							+ error403Page);
				} catch (IOException e) {
					logger.error(error403Page + "ページへのリダイレクトに失敗しました。", e);
				}

			}

			// POSTデータをセッションに保存
			String saveFormFlag = bundle.getString("saveFormFlag");
			String flag = request.getParameter(saveFormFlag);
			if (Boolean.parseBoolean(flag)) {
				logger.debug("フォームデータをセッションに保存します。");
				SessionManager.setFormData(request);
				// TODO: バリデーション
			}

		} catch (MissingResourceException mre) {
			logger.error("設定に不備があります。", mre);
			String error500Page = bundle.getString("error500Page");
			try {
				response.sendRedirect(request.getContextPath() + error500Page);
			} catch (IOException e) {
				logger.error(error500Page + "ページへのリダイレクトに失敗しました。", e);
			}
		} catch (ServerException se) {
			// TODO: エラーページにリダイレクトする。
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return null;

	}

}
