/**
 * Copyright 2009 Autonomy Corp. All rights reserved. Other trademarks
 * are registered trademarks and the properties of their respective owners.
 * Product specifications and features are subject to change without notice.
 * Use of Autonomy software is under license.
 *
 * If this product is acquired under the terms of a DoD contract: Use,
 * duplication, or disclosure by the Government is subject to restrictions
 * as set forth in subparagraph (c)(1)(ii) of 252.227-7013. Civilian agency
 * contract: Use, reproduction or disclosure is subject to 52.227-19
 * (a) through (d) and restrictions set forth in the accompanying end
 * user agreement. Unpublished-rights reserved under the copyright laws
 * of the United States. Autonomy, Inc., One Market Plaza, Spear Tower,
 * Suite 1900, San Francisco, CA. 94105, US.
 */

package jp.co.tepco.kpfbpf.livesite.sample.webapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.tepco.kpfbpf.livesite.common.webapp.form.FormManager;
import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.session.SessionManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.runtime.RequestContext;

/**
 * Sample externals
 *
 * @author $Author: jchang $
 * @version $Revision: #2 $
 */
public class OdenSampleExternal {
	/** logger */
	private static final transient Log LOGGER = LogFactory
			.getLog(OdenSampleExternal.class);

	private static final Logger log4j = Logger
			.getLogger(OdenSampleExternal.class);

	/**
	 * Simple external call usable for testing XPath: /
	 *
	 * @param context
	 *            request
	 * @return dom4j document
	 */
	public Document InitComponent(RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Executing OdenSampleExternal");
		}
		LOGGER.info("Executing OdenSampleExternal info");

		log4j.info("InitComponent start");

		HttpServletRequest request = context.getRequest();
		String debugText = "v1.2\n";
		debugText += new Date() + " " + request.getRequestURL() + "?"
				+ request.getQueryString() + "\n";

		org.dom4j.Document doc = Dom4jUtils.newDocument();
		try {
			String mode = (String) context.getParameters().get("mode");

			// // ユーザIDをセッションから取得
			String userId = SessionManager.getUserId(request);
			log4j.info("userId=" + userId);
			//
			// // ユーザ基本情報をセッションから取得
			Map<String, Object> userInfo = SessionManager.getUserInfo(request);
			log4j.info("userInfo=" + userInfo);
			//
			// // FormManagerの初期化
			FormManager formManager = new FormManager(request, this);
			String formId = formManager.getFormId();
			log4j.info("formId=" + formId);
			Map<String, String[]> formData = formManager.getFormData();
			log4j.info("formData=" + formData);
			log4j.info("mode=" + mode);

			debugText += "formId=" + formId + "\n";
			debugText += "formData=" + formData + "\n";
			if (mode.equals("1")) {

				Element pagedoc = doc.addElement("indexpage");
				Element data = pagedoc.addElement("data");

				// セッションからユーザ情報を取得してXMLにセット
				if (userId != null) {
					data.addElement("userid").setText(userId);
				}
				if (userInfo != null) {
					data.addElement("username").setText(
							(String) userInfo.get("name"));
				}

				data.addElement("prodsafeinspectionsystemdisplayflg").setText(
						"1");
				data.addElement("prodsafedisplaysystemdisplayflg").setText("1");

				Element category = data.addElement("category");
				Element child;

				child = category.addElement("option");
				child.setText("--");
				child.addAttribute("value", "0");

				child = category.addElement("option");
				child.setText("キッチン家電");
				child.addAttribute("value", "1");

				child = category.addElement("option");
				child.setText("リビング");
				child.addAttribute("value", "2");

				child = category.addElement("option");
				child.setText("寝室");
				child.addAttribute("value", "3");

				Element aptype = data.addElement("type");

				child = aptype.addElement("option");
				child.setText("--");
				child.addAttribute("value", "0");

				child = aptype.addElement("option");
				child.setText("冷蔵庫");
				child.addAttribute("value", "1");

				child = aptype.addElement("option");
				child.setText("炊飯器");
				child.addAttribute("value", "2");

				child = aptype.addElement("option");
				child.setText("電子レンジ");
				child.addAttribute("value", "3");

				if (formData != null && formData.get("settingplace") != null) {
					log4j.info("setting settingplace to xml");
					data.addElement("settingplace").setText(
							formData.get("settingplace")[0]);
				}
				if (formId != null) {
					log4j.info("setting formId to xml");
					data.addElement("formId").setText(formId);
				}
				// デバッグ用
				data.addElement("debugsession").addCDATA(
						SessionManager.dumpSession(request));

				debugText += SessionManager.dumpSession(request) + "\n";

			} else if (mode.equals("2")) {
				Element pagedoc = doc.addElement("confirmpage");
				Element data = pagedoc.addElement("data");

				// セッションからユーザ情報を取得してXMLにセット
				if (userId != null) {
					data.addElement("userid").setText(userId);
				}
				if (userInfo != null) {
					data.addElement("username").setText(
							(String) userInfo.get("name"));
				}

				data.addElement("category").setText("キッチン家電");
				data.addElement("type").setText("冷蔵庫");

				Element model = data.addElement("model");
				model.addElement("name").setText("XX-XXX-XXX");
				model.addElement("year").setText("2015");

				data.addElement("point").setText("XXXXXXX ポイント");
				data.addElement("settingaddress").setText("自宅 (江東区東雲X-X-XX)");

				if (formData != null && formData.get("settingplace") != null) {
					data.addElement("settingplace").setText(
							formData.get("settingplace")[0]);
				}

				Element purchase = data.addElement("purchase");
				Element date = purchase.addElement("date");
				date.addElement("year").setText("2014");
				date.addElement("month").setText("12");
				date.addElement("day").setText("12");

				date.addElement("store").setText("XXXXXXXX");
				date.addElement("price").setText("000,000,000");
				date.addElement("warrantyterm").setText("1");
				date.addElement("contacttellno").setText("XXXX-XXXX-XXXX");

				Element maker = data.addElement("maker");
				maker.addElement("name").setText("日本ヒューレットパッカード");
				maker.addElement("warrantyterm").setText("5");
				maker.addElement("tellno").setText("03-1111-2222");

				if (formId != null) {
					data.addElement("formId").setText(formId);
				}

				// デバッグ用
				data.addElement("debugsession").addCDATA(
						SessionManager.dumpSession(request));

			} else if (mode.equals("3")) {

				Element pagedoc = doc.addElement("completepage");
				Element data = pagedoc.addElement("data");

				RESTClient restClient = new RESTClient(request, "test",
						formManager);
				Map<String, Object> postData = new HashMap<String, Object>();
				Map<String, Object> rootObject = new HashMap<String, Object>();
				if (formData != null) {
					rootObject.put("settingplace",
							formData.get("settingplace")[0]);
					postData.put("object", rootObject);
					restClient.setPostData(postData);
					Map<String, Object> responseData = restClient.callAPI();
					if (responseData != null) {
						data.addElement("message").setText(
								(String) responseData.get("message"));
					}
				}

				// セッションからユーザ情報を取得してXMLにセット
				if (userId != null) {
					data.addElement("userid").setText(userId);
				}
				if (userInfo != null) {
					data.addElement("username").setText(
							(String) userInfo.get("name"));
				}

				// デバッグ用
				data.addElement("debugsession").addCDATA(
						SessionManager.dumpSession(request));

				doc.getRootElement().addElement("debug").setText(mode);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			String str = sw.toString();
			debugText += str + "\n";
		}
		try {
			String pre = "";
			try {
				pre = FileUtils
						.readFileToString(new File("/tmp/toda/debug.log"))
						+ "\n";
			} catch (Exception e) {

			}
			FileUtils.writeStringToFile(new File("/tmp/toda/debug.log"), pre
					+ debugText, "utf-8");
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		return doc;
	}
}
