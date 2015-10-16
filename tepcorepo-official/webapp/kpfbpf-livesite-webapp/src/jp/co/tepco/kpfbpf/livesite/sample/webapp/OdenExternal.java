package jp.co.tepco.kpfbpf.livesite.sample.webapp;

import java.util.Map;

import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;
import jp.co.tepco.kpfbpf.livesite.common.webapp.bl.BaseWebPageComponentExternal;

import org.dom4j.Document;
import org.dom4j.Element;

import com.interwoven.livesite.dom4j.Dom4jUtils;
import com.interwoven.livesite.json.xml.JsonXmlConverter;
import com.interwoven.livesite.runtime.RequestContext;
import org.json.JSONException;
import org.json.JSONObject;

public class OdenExternal extends BaseWebPageComponentExternal {
    // DELETE ME Begin
    // 複数Componentが配置されるページでは、Componentの　の実行順は
    // 保障されないため、Session上に画面遷移状態を保持する場合、
    // Component単位にSessionデータを持つ必要があるか、
    // Page単位で十分か、慎重に検討する必要がある。
    // DELETE ME End

    final static String KEY_OdenSampleExternal3_Mode_Save_ID = "OdenSampleExternal3_Mode_Save_ID";
    private final transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OdenExternal.class);

    @Override
    public Document ExecuteInitComponent(RequestContext context) {
        Document doc = Dom4jUtils.newDocument();
        Element root = doc.addElement("Result");
        try {
            // Externalは、XML Documentを返す必要がある。
            String api = context.getParameterString("api", "47_get");
            logger.debug("Api to use: " + api);
            RESTClient importConfigClient = getRESTClientInstance(api);
            Map<String, Object> responseData = importConfigClient.callAPI();
            logger.debug("Fetched Response Data" + responseData);
            if (responseData != null) {
                JsonXmlConverter jsonXmlConverter = new JsonXmlConverter();
                String replacedString = responseData.toString().replaceAll("=,", "='',");
                replacedString = replacedString.toString().replaceAll("=}", "=''}");
                logger.info("Replaced empty string with quotes in JSON");
                JSONObject jsonObject = new JSONObject(replacedString);
                Element xml = jsonXmlConverter.xmlFromJson(jsonObject.toString());
                logger.info("XML File Generated");
                root.add(xml.detach());
            }
        } catch (JSONException ex) {
            logger.error("Error while Executing Component: " + ex);
        }
        return doc;
    }
}
