package jp.co.tepco.kpfbpf.livesite.sample.webapp;

import java.util.HashMap;
import java.util.Map;

import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.bl.BaseWebPageComponentExternal;

import org.dom4j.Document;
import org.dom4j.Element;

import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.runtime.RequestContext;

public class OdenSampleExternal3 extends BaseWebPageComponentExternal {
	// DELETE ME Begin
	// 複数Componentが配置されるページでは、Componentの　の実行順は
	// 保障されないため、Session上に画面遷移状態を保持する場合、
	// Component単位にSessionデータを持つ必要があるか、
	// Page単位で十分か、慎重に検討する必要がある。
	// DELETE ME End
	final static String KEY_OdenSampleExternal3_Mode_Save_ID = "OdenSampleExternal3_Mode_Save_ID";
	
	
	public Document ExecuteInitComponent( RequestContext context ) {
		// Externalは、XML Documentを返す必要がある。
		Document doc = Dom4jUtils.newDocument();
		
		if (getComponentModeParameter().equals("1")) {
			// フォームページ

			// XMLデータの作成
			Element pagedoc = doc.addElement("indexpage");
			Element data = pagedoc.addElement("data");

			// 入力しかかりの場合（一度完了ページに行ってから戻ってきた場合）はformDataがnullではないので、
			// 入力途中のデータをXMLに埋め込む
			if (getFormData() != null) {
				if (getFormData().get("settingplace") != null) {
					data.addElement("settingplace").setText(
							getFormData().get("settingplace")[0]);
				}
			}

			// ページ間でフォームデータを共有するため、formIdを引き回す。
			addFormIdElement(data);

			// Sessionに画面遷移状態を保存
			StoreObjectToSession(KEY_OdenSampleExternal3_Mode_Save_ID, "2");
			
		} else if (getComponentModeParameter().equals("2")) {
			// 確認ページ
			
			// Sessionデータで画面遷移状態を確認
			String sessionState = (String )QueryObjectFromSession(KEY_OdenSampleExternal3_Mode_Save_ID);
			if ( ! sessionState.equals("2")) {
				// TODO ERROR 異常な画面遷移
			}

			// XMLデータの作成
			
			Element pagedoc = doc.addElement("confirmpage");
			Element data = pagedoc.addElement("data");

			if (getFormData() == null) {
				// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
				// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
			} else {
				// 入力途中のデータをXMLに埋め込む
				if (getFormData().get("settingplace") != null) {
					data.addElement("settingplace").setText(
							getFormData().get("settingplace")[0]);
				}
			}

			// ページ間でフォームデータを共有するため、formIdを引き回す。
			addFormIdElement(data);
			
			// Sessionに画面遷移状態を保存
			StoreObjectToSession(KEY_OdenSampleExternal3_Mode_Save_ID, "3");

		} else if (getComponentModeParameter().equals("3")) {
			// 完了ページ
			
			// Sessionデータで画面遷移状態を確認
			String sessionState = (String )QueryObjectFromSession(KEY_OdenSampleExternal3_Mode_Save_ID);
			if ( ! sessionState.equals("3")) {
				// TODO ERROR 異常な画面遷移
			}

			// XMLデータの作成

			Element pagedoc = doc.addElement("completepage");
			Element data = pagedoc.addElement("data");

			if (getFormData() == null) {
				// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
				// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
			} else {
				// APIコールを実行するために、RESTClientを生成する。
				// コンストラクタで渡しているformManagerに紐づいたセッション上のデータは、
				// APIコール成功時にクリアされる。
				RESTClient restClient = getRESTClientInstance("test1");

				// ここでは、jsonPostDataがAPIコールする際に送信するjsonのもととなるMap。
				Map<String, Object> jsonPostData = new HashMap<String, Object>();

				// ここでは、APIのメソッドがGETではなくPOSTだった場合の例を記述している。
				// POSTの場合、最上位にname=valueの羅列があるのではなく、
				// いったんobjectをかましてその中にname=valueがあるようなので、
				// ここではそのようにしている。（ただし、APIによってはその限りではないかもしれないので、
				// API毎に送信する構造は確認すること）
				Map<String, Object> rootObject = new HashMap<String, Object>();
				rootObject.put("settingplace",
						getFormData().get("settingplace")[0]);
				jsonPostData.put("object", rootObject);
				restClient.setPostData(jsonPostData);
				Map<String, Object> responseData = restClient.callAPI();
				if (responseData != null) {
					data.addElement("message").setText(
							(String) responseData.get("message"));
				}
			}
		}
		return doc;
	}


}
