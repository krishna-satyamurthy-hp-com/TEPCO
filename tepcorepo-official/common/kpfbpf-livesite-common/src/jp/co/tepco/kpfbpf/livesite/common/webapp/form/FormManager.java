package jp.co.tepco.kpfbpf.livesite.common.webapp.form;

import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.error.ServerException;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.log4j.Logger;

/**
 * フォームを管理する共通クラスです。<br>
 * 以下に、フォームを取り扱うコンポーネントのBizObjectで行なうべき実装のサンプルを記載します。<br>
 * フォームデータはセッションで管理しますが、１世代しか管理しないため、他のページがフォームデータを上書きしてしまうことがありえます。<br>
 * <font color="red">あるページで送信されたフォームデータが別のページで表示されたりサブミットされてしまうことがないように、
 * セッションではコンポーネントのBizObjectのクラス名とフォームIDを関連付けて管理しています。<br>
 * このため、FormManagerのコンストラクタではBizObjectをthisで渡していることに注意してください。</font><br>
 * <br>
 * ■フォームページ<br>
 *
 * <pre>
 * FormManager formManager = new FormManager(request, this);
 *
 * // 確認ページやエラーページから戻ってきた場合は下記メソッドでフォームIDが取得可能。新規フォームの場合は、新規フォームIDが発行される。
 * String formId = formManager.getFormId();
 *
 * // 確認ページやエラーページから戻ってきた場合は下記メソッドでフォームデータが取得可能。新規フォームの場合は、nullとなる。
 * Map&lt;String, String[]&gt; formData = formManager.getFormData();
 *
 * // フォームデータを元に、画面用のXMLを生成する。
 * // 生成したフォームでは、最低限以下のhiddenフィールドが必要。
 *
 * // ↓フォームをセッション上で識別するためのフォームID
 * // &lt;input type=&quot;hidden&quot; name=&quot;formId&quot; value=&quot;【フォームID】&quot;&gt;
 * // ↓trueを指定することで、プリプロセッサがフォームデータをセッションに保存する
 * // &lt;input type=&quot;hidden&quot; name=&quot;saveForm&quot; value=&quot;true&quot;&gt;
 * </pre>
 *
 * <br>
 * ■確認ページ<br>
 *
 * <pre>
 * FormManager formManager = new FormManager(request, this);
 *
 * // フォームページから渡されてきたフォームIDを取得する。
 * String formId = formManager.getFormId();
 *
 * // フォームページでセットされたフォームデータを取得する。
 * Map&lt;String, String[]&gt; formData = formManager.getFormData();
 *
 * if (formData == null) {
 *
 * 	// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
 * 	// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
 *
 * } else {
 *
 * 	// フォームデータを元に、画面用のXMLを生成する。
 * 	// 生成したフォームでは、最低限以下のhiddenフィールドが必要。
 *
 * 	// ↓フォームをセッション上で識別するためのフォームID
 * 	// &lt;input type=&quot;hidden&quot; name=&quot;formId&quot; value=&quot;【フォームID】&quot;&gt;
 * 	// また、フォーム画面への「戻る」アンカーにも、フォームIDをURLパラメータとして埋め込む必要あり。
 * }
 * </pre>
 *
 * <br>
 * ■完了ページ<br>
 *
 * <pre>
 * String apiID = ... // APIのIDは別途設定ファイル等から取得する。
 *
 * // フォームデータを元に、実際にAPIに対して送信するデータを組み立てる
 * FormManager formManager = new FormManager(request, this);
 *
 * // セッションに保存されたフォームデータを取得する。
 * Map&lt;String, String[]&gt; formData = formManager.getFormData();
 *
 * if (formData == null) {
 *
 * 	// ここでフォームデータがnullということは、別のフォームがセッションデータを上書きしてしまったか、
 * 	// 無効なフォームIDが渡されたことを意味するので、エラー処理を行なう。
 *
 * } else {
 * 	Map&lt;String, Object&gt; postData = new HashMap&lt;String, Object&gt;();
 * 	postData.put(&quot;aaa&quot;, formData.get(&quot;aaa&quot;)[0]);
 * 	...
 *
 * 	// APIコール成功時にセッションのフォームデータを削除するように、３つ目の引数にFormManagerを指定してRESTClientを生成する。
 * 	RESTClient client = new RESTClient(request, apiID, formManager);
 * 	client.setPostData(postData);
 * 	Map&lt;String, Object&gt; responseData = client.callAPI();
 *
 * 	// レスポンスを元に、画面用のXMLを生成する。
 * }
 * </pre>
 *
 * <br>
 * なお、APIコール部品の詳細については、{@link RESTClient}を参照してください。<br>
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
 * <td>formIdParameterName</td>
 * <td>フォームで使用するフォームIDのパラメータ名</td>
 * <td>必須。未指定の場合、エラーになる</td>
 * </tr>
 * </table>
 *
 */
public class FormManager {

	private static Logger logger = Logger.getLogger(FormManager.class);

	private String formId;
	private Map<String, String[]> formData;
	private Object bizObject;

	/**
	 * FormManagerを初期化します。bizObjectには、このフォームを操作するBizObject（通常はthis）を渡してください。
	 *
	 * @param request
	 *            HTTPサーブレットリクエストオブジェクト
	 * @param bizObject
	 *            このフォームを操作するBizObject
	 */
	public FormManager(HttpServletRequest request, Object bizObject) {
		try {
			this.bizObject = bizObject;
			ResourceBundle bundle = ResourceBundle.getBundle("FormManager");
			formId = request.getParameter(bundle
					.getString("formIdParameterName"));
			logger.debug("formId=" + formId);
			if (formId == null) {
				formId = SessionManager.generateFormId(request, bizObject
						.getClass().getName());
				logger.debug("フォームIDがnullだったため、再生成しました。新しいformId=" + formId);
			} else {
				formData = SessionManager.getFormData(request, formId,
						bizObject.getClass().getName());
				logger.debug("formData=" + formData);
				if (formData == null) {
					formId = SessionManager.generateFormId(request, bizObject
							.getClass().getName());
					logger.debug("フォームデータがnullだったため、フォームIDを再生成しました。新しいformId="
							+ formId);
				}
			}
		} catch (Exception e) {
			logger.error("インスタンスの生成に失敗しました。", e);
			throw new ServerException();
		}
	}

	/**
	 * formIdリクエストパラメータをもとに、このフォームのフォームIDを取得します。formIdリクエストパラメータがなかった場合は、
	 * 新規フォームIDが発行されます。
	 *
	 * @return このフォームのフォームID
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * セッション上に保存されたフォームデータがあればそれを返します。セッション上に対応するフォームデータがない場合は、nullになります。
	 *
	 * @return セッション上に保存されたフォームデータか、null
	 */
	public Map<String, String[]> getFormData() {
		return formData;
	}

	/**
	 * このフォームを操作するBizObjectを取得します。
	 *
	 * @return このフォームを操作するBizObject
	 */
	public Object getBizObject() {
		return bizObject;
	}
}
