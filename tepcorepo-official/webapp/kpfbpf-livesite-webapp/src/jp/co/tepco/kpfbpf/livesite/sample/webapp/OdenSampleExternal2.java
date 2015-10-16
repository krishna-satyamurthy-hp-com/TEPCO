package jp.co.tepco.kpfbpf.livesite.sample.webapp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.error.AuthorizationException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.form.FormManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.dom4j.Document;
import org.dom4j.Element;

import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.runtime.RequestContext;

public class OdenSampleExternal2 {
	public Document InitComponent(RequestContext context) {
		HttpServletRequest request = context.getRequest();

		// SessionManagerからは、ユーザIDやロール、APIキーの他、ユーザの基本情報が取得できる。
		String userId = SessionManager.getUserId(request);
		String role = SessionManager.getRole(request);
		String apiKey = SessionManager.getAPIKey(request);
		Map<String, Object> userInfo = SessionManager.getUserInfo(request);

		// Externalは、XML Documentを返す必要がある。
		Document doc = Dom4jUtils.newDocument();
		try {
			// フォームページ、確認ページ、完了ページは、modeパラメータで見分ける。
			String mode = (String) context.getParameters().get("mode");

			// FormManagerにこのオブジェクト自身を渡し、FormManagerを取得する。
			// FormManagerの詳細は、javadoc参照のこと。
			FormManager formManager = new FormManager(request, this);
			String formId = formManager.getFormId();
			Map<String, String[]> formData = formManager.getFormData();

			if (mode.equals("1")) {
				// フォームページ

				// XMLデータの作成
				Element pagedoc = doc.addElement("indexpage");
				Element data = pagedoc.addElement("data");

				// 入力しかかりの場合（一度完了ページに行ってから戻ってきた場合）はformDataがnullではないので、
				// 入力途中のデータをXMLに埋め込む
				if (formData != null) {
					if (formData.get("settingplace") != null) {
						data.addElement("settingplace").setText(
								formData.get("settingplace")[0]);
					}
				}

				// ページ間でフォームデータを共有するため、formIdを引き回す。
				if (formId != null) {
					data.addElement("formId").setText(formId);
				}
				
				// TODO Sessionに画面遷移状態の保存が必要

			} else if (mode.equals("2")) {
				// 確認ページ
				
				// TODO Sessionデータで画面遷移の確認とエラー処理が必要

				// XMLデータの作成
				Element pagedoc = doc.addElement("confirmpage");
				Element data = pagedoc.addElement("data");

				if (formData == null) {
					// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
					// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
				} else {
					// 入力途中のデータをXMLに埋め込む
					if (formData.get("settingplace") != null) {
						data.addElement("settingplace").setText(
								formData.get("settingplace")[0]);
					}
				}

				// ページ間でフォームデータを共有するため、formIdを引き回す。
				if (formId != null) {
					data.addElement("formId").setText(formId);
				}
				
				// TODO Sessionに画面遷移状態の保存が必要

			} else if (mode.equals("3")) {
				// 完了ページ

				// TODO Sessionデータで画面遷移の確認とエラー処理が必要
				
				// XMLデータの作成
				Element pagedoc = doc.addElement("completepage");
				Element data = pagedoc.addElement("data");

				if (formData == null) {
					// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
					// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
				} else {
					// APIコールを実行するために、RESTClientを生成する。
					// コンストラクタで渡しているformManagerに紐づいたセッション上のデータは、
					// APIコール成功時にクリアされる。
					RESTClient restClient = new RESTClient(request, "test1D",
							formManager);

					// ここでは、jsonPostDataがAPIコールする際に送信するjsonのもととなるMap。
					Map<String, Object> jsonPostData = new HashMap<String, Object>();

					// ここでは、APIのメソッドがGETではなくPOSTだった場合の例を記述している。
					// POSTの場合、最上位にname=valueの羅列があるのではなく、
					// いったんobjectをかましてその中にname=valueがあるようなので、
					// ここではそのようにしている。（ただし、APIによってはその限りではないかもしれないので、
					// API毎に送信する構造は確認すること）
					Map<String, Object> rootObject = new HashMap<String, Object>();
					rootObject.put("settingplace",
							formData.get("settingplace")[0]);
					jsonPostData.put("object", rootObject);
					restClient.setPostData(jsonPostData);
					Map<String, Object> responseData = restClient.callAPI();
					if (responseData != null) {
						data.addElement("message").setText(
								(String) responseData.get("message"));
					}
				}

			}
		} catch (AuthorizationException ae) {
			// APIコールの認可エラーがあった場合はこの例外が発生する。
		} catch (ServerException se) {
			// APIコールでその他致命的なエラーが発生した場合は、この例外が発生する。
		} catch (Exception e) {
			// 共通部品の例外（現状、手元にないので、ただのExceptionとなっていますが……）
		}
		return doc;
	}
}
