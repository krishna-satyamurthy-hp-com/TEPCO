package jp.co.tepco.kpfbpf.livesite.common.webapp.error;

import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;

/**
 * {@link RESTClient#callAPI()}の結果がサーバエラーだった場合にthrowされる例外です。
 *
 */
public class ServerException extends RuntimeException {

}
