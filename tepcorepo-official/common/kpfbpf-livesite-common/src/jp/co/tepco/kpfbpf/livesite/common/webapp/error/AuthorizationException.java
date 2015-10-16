package jp.co.tepco.kpfbpf.livesite.common.webapp.error;

import jp.co.tepco.kpfbpf.livesite.common.webapp.restclient.RESTClient;

/**
 * {@link RESTClient#callAPI()}の結果が認可エラーだった場合にthrowされる例外です。
 *
 */
public class AuthorizationException extends RuntimeException {

}
