package com.elk.request;

import com.elk.request.service.ElkRequestBuilderImpl;
import org.apache.http.client.config.RequestConfig;

import javax.net.ssl.SSLContext;
import java.util.Map;
/**
 * Abstract class with builder design pattern
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public abstract class ElkRequest {
    /**
     * ElasticSearch/OpenSearch Hostname
     * @return string
     */
    public abstract String hostname();

    /**
     * ElasticSearch/OpenSearch Port
     * @return int
     */
    public abstract int port();

    /**
     * scheme with Http or Https
     * @return String
     */
    public abstract String scheme();

    /**
     * UserName
     * @return String
     */
    public abstract String username();

    /**
     * Password
     * @return String
     */
    public abstract String password();

    /**
     * Query with variables
     * @return String
     */
    public abstract String query();

    /**
     * Http Methods : GET, POST, PUT and DELETE
     * @return String
     */
    public abstract String method();

    /**
     * Query Parameters  key and value
     * @return map
     */
    public abstract Map<String, String> queryParam();

    /**
     * All parameters after the basic URI
     * @return String
     */
    public abstract String uriSuffix();

    /**
     * Http RequestConfig from httpclient
     * @return RequestConfig
     */
    public abstract RequestConfig httpRequestConfig();

    /**
     * SSL Context from javax
     * @return SSLContext
     */
    public abstract SSLContext sslContext();
    public interface Builder {

        Builder hostname(String hostname);

        Builder port(int port);

        Builder scheme(String scheme);

        Builder username(String username);

        Builder password(String password);

        Builder query(String query);

        Builder uriSuffix(String uriSuffix);

        Builder queryParam(String name, String value);

        Builder GET();

        Builder POST();

        Builder PUT();

        Builder DELETE();

        Builder httpRequestConfig(RequestConfig httpRequestConfig);
        Builder sslContext(SSLContext sslContext);
        ElkRequest build();
    }

    public static Builder newBuilder() {
        return new ElkRequestBuilderImpl();
    }

}
