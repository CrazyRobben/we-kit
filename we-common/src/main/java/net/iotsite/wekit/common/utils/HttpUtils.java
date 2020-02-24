package net.iotsite.wekit.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: fanhu3
 * @date: 2018/2/3 17:59
 * @description ：http请求工具类
 */
public class HttpUtils {

    private static final int SOCKET_TIMEOUT = 2000;

    private static final int CONNECT_TIMEOUT = 2000;

    private static final int CONNECTION_REQUEST_TIMEOUT = 20000;

    //最大不要超过1000
    private static final int MAX_CONN_TOTAL = 1000;
    //实际的单个连接池大小，如tps定为50，那就配置50
    private static final int MAX_CONN_PER_ROUTE = 500;


    private static final String UTF_8 = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static CloseableHttpClient client = null;

    private static RequestConfig requestConfig = null;

    //初始化配置
    static {
        try {
            HttpHost proxy = null;

            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setProxy(proxy)
                    .build();
            client = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                    .setMaxConnTotal(MAX_CONN_TOTAL)
                    .setMaxConnPerRoute(MAX_CONN_PER_ROUTE).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取http请求实体类
     *
     * @param url
     * @param method
     * @return
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
    private static HttpRequestBase getInstance(String url, MethodType method) throws URISyntaxException, UnsupportedEncodingException {
        HttpRequestBase http;
        if (null == method) {
            throw new IllegalArgumentException("请求类型不能为空");
        }
        if (MethodType.POST.equals(method)) {
            http = new HttpPost(url);
        } else if (MethodType.GET.equals(method)) {
            http = new HttpGet(url);
        } else if (MethodType.DELETE.equals(method)) {
            http = new HttpDelete(url);
        } else if (MethodType.PUT.equals(method)) {
            http = new HttpPut(url);
        } else {
            throw new IllegalArgumentException("不支持的请求类型:" + method);
        }
        logger.debug("method:{}", method.toString());
        return http;
    }

    /**
     * 拼接url参数
     *
     * @param http
     * @param url
     * @param queries
     * @throws URISyntaxException
     */
    private static void setQueries(HttpRequestBase http, String url, String queries) throws URISyntaxException, UnsupportedEncodingException {
        JSONObject params = null;
        try {
            params = JSON.parseObject(queries);
        } catch (Exception e) {
            logger.error("setQueries:参数格式错误，请以JSON格式传参");
            throw new IllegalArgumentException("参数格式错误，请以JSON格式传参");
        }
        if (null != params && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> p : entries) {
                String key = p.getKey();
                Object value = p.getValue();
                if (StringUtils.isBlank(key)) {
                    return;
                }
                sb.append(URLEncoder.encode(key, UTF_8)).append("=").append(URLEncoder.encode(value.toString(), UTF_8)).append("&");
            }
            url += sb.substring(0, sb.length() - 1);
        }

        logger.debug("url:{}", url);
        http.setURI(new URI(url));
    }

    /**
     * 设置请求头
     *
     * @param http
     * @param headerMap
     */
    private static void setHeaders(HttpRequestBase http, Map<String, String> headerMap) {
        if (null == headerMap || headerMap.isEmpty()) {
            logger.debug("header:empty header");
            return;
        }
        Set<Map.Entry<String, String>> entries = headerMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            http.setHeader(entry.getKey(), entry.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("header:name:{},value:{}", entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 发起http请求
     *
     * @param url     请求url
     * @param method  请求方式 GET/POST/DELETE/PUT
     * @param queries 请求参数 可以和url拼接
     * @param headers 请求头
     * @param bodies  请求体参数 比如content-type=application/json  bodies 为json格式字符串
     * @return
     */
    public static HttpResponse connect(String url, MethodType method, String queries, Map<String, String> headers, String bodies) {
        String errorMsg = null;
        try {
            logger.debug("connect:method:{},url:{}", method, URLUtils.decode(url));
            //创建请求
            HttpRequestBase instance = getInstance(url, method);

            //设置请求头
            setHeaders(instance, headers);

            //设置请求体
            setBodies(instance, bodies);

            //设置请求参数
            setQueries(instance, url, queries);

            //加载配置
            instance.setConfig(requestConfig);
            //发起请求
            CloseableHttpResponse execute = client.execute(instance);
            int statusCode = execute.getStatusLine().getStatusCode();
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, UTF_8);
            logger.debug("response code:{},result:{}", statusCode, result);
            if (statusCode != RequestCode.SUCCESS_CODE) {
                logger.error("connect：请求接口出错");
                return new HttpResponse(statusCode, "error response code", result, execute);
            }
            return new HttpResponse(RequestCode.SUCCESS_CODE, null, result, execute);
        } catch (URISyntaxException e) {
            logger.error("connect：URI 语法异常", e);
            errorMsg = e.getMessage();
        } catch (UnsupportedEncodingException e) {
            logger.error("connect：不支持的编码格式", e);
            errorMsg = e.getMessage();
        } catch (IOException e) {
            logger.error("connect：服务连接异常", e);
            errorMsg = e.getMessage();
        } catch (IllegalArgumentException e) {
            logger.error("connect：请求参数异常", e);
            errorMsg = e.getMessage();
        } catch (Exception e) {
            logger.error("connect：未知异常", e);
            errorMsg = e.getMessage();
        }
        return new HttpResponse(RequestCode.FAILED_CODE, null == errorMsg ? "请求服务异常" : errorMsg, null);
    }


    /**
     * 设置请求体
     *
     * @return void
     * @author fanhu3
     * @date 2019/5/16 8:59
     * @version DroidMe 4.1
     **/
    private static void setBodies(HttpRequestBase http, String bodies) throws Exception {
        if (StringUtils.isBlank(bodies)) {
            logger.trace("body:empty body");
            return;
        }
        if (!(http instanceof HttpEntityEnclosingRequestBase)) {
            logger.warn("body:method not support setting body");
            return;
        }
        ((HttpEntityEnclosingRequestBase) http).setEntity(new StringEntity(bodies, UTF_8));
    }

    /**
     * Http请求结果
     *
     * @author fanhu3
     * @version DroidMe 4.1
     * @date 2019/4/19 9:23
     * @return
     **/
    public static class HttpResponse implements Serializable {

        private static final long serialVersionUID = -8632198055103453511L;

        //状态码
        private int code;
        //失败返回信息
        private String message;
        //请求结果
        private String result;
        // http结果
        private CloseableHttpResponse executeResult;


        private HttpResponse(int code, String message, String result) {
            this.code = code;
            this.message = message;
            this.result = result;
        }

        HttpResponse(int code, String message, String result, CloseableHttpResponse executeResult) {
            this.code = code;
            this.message = message;
            this.result = result;
            this.executeResult = executeResult;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public String getResult() {
            return result;
        }

        public boolean isSuccess() {
            return RequestCode.SUCCESS_CODE == code;
        }

        public CloseableHttpResponse getExecuteResult() {
            return executeResult;
        }

        public void setExecuteResult(CloseableHttpResponse executeResult) {
            this.executeResult = executeResult;
        }

        @Override
        public String toString() {
            return "HttpResponse{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", result='" + result + '\'' +
                    '}';
        }

        public <T> T wrapper(ResponseWrapper<T> wrapper) {
            return wrapper.wrapper(this);
        }
    }

    public interface RequestCode {
        /**
         * 成功码
         */
        int SUCCESS_CODE = 200;
        /**
         * 错误码
         */
        int FAILED_CODE = -1;
    }

    /**
     * @author fanhu3
     * @version DroidMe 4.1
     * @date 2019/4/18 17:10
     * @return
     **/
    public enum MethodType {
        GET, POST, DELETE, PUT;
    }

    public enum BodyType {
        JSON("application/json"),
        XML("application/xml"),
        FORM("application/x-www-form-urlencoded"),
        TEXT_XML("text/xml;charset=utf-8");

        private String header;

        BodyType(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
    }


    /**
     * get请求
     *
     * @param url     请求url
     * @param queries 请求参数 以JSON形式传参
     * @return com.iflytek.droid.utils.NewHttpUtils.HttpResponse
     * @author fanhu3
     * @date 2019/5/15 13:35
     * @version DroidMe 4.1
     **/
    public static HttpResponse doGet(String url, String queries) {
        return connect(url, MethodType.GET, queries, null, null);
    }

    /**
     * get请求
     *
     * @return com.iflytek.droid.utils.NewHttpUtils.HttpResponse
     * @author fanhu3
     * @date 2019/5/15 13:36
     * @version DroidMe 4.1
     **/
    public static HttpResponse doGet(String url) {
        return connect(url, MethodType.GET, null, null, null);
    }

    /**
     * POST 默认application/json传参数
     *
     * @return com.iflytek.droid.utils.NewHttpUtils.HttpResponse
     * @author fanhu3
     * @date 2019/5/15 13:38
     * @version DroidMe 4.1
     **/
    public static HttpResponse doPost(String url, String bodies, Map<String, String> headers) {
        return connect(url, MethodType.POST, null, headers, bodies);
    }

    /**
     * post 请求 默认application/json传参数
     *
     * @return com.iflytek.droid.utils.NewHttpUtils.HttpResponse
     * @author fanhu3
     * @date 2019/5/15 13:48
     * @version DroidMe 4.1
     **/
    public static HttpResponse doPost(String url, Object bodies) {
        return doPost(url, JSON.toJSONString(bodies), BodyType.JSON);
    }


    /**
     * GET请求
     *
     * @return com.iflytek.droid.utils.NewHttpUtils.HttpResponse
     * @author fanhu3
     * @date 2019/5/16 14:23
     * @version DroidMe 4.1
     **/
    public static HttpResponse doPost(String url, String bodies, BodyType bodyType) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", bodyType.getHeader());
        return connect(url, MethodType.POST, null, map, bodies);
    }


    /**
     * 转换为业务实际的封装结果
     *
     * @param <T>
     */
    public interface ResponseWrapper<T> {
        T wrapper(HttpResponse response);
    }
}
