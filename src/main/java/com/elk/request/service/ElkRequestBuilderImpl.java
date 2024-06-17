package com.elk.request.service;

import com.elk.request.ElkRequest;
import org.apache.http.client.config.RequestConfig;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.elk.request.util.Utils.newIAE;
import static java.util.Objects.requireNonNull;

/**
 * ElkRequestBuilderImpl
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public class ElkRequestBuilderImpl implements ElkRequest.Builder {

    private String hostname;
    private int port;
    private String scheme;
    private String username;
    private String password;
    private String query;

    private String uriSuffix;
    private final Map<String, String> queryParam;
    private String method;
    private RequestConfig httpRequestConfig;
    private SSLContext sslContext;

    public ElkRequestBuilderImpl() {
        queryParam = new LinkedHashMap<>();
    }


    @Override
    public ElkRequestBuilderImpl hostname(String hostname) {
        requireNonNull(hostname, "hostname must be non-null");
        this.hostname = hostname;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl scheme(String scheme) {
        requireNonNull(scheme, "scheme must be non-null");
        checkScheme(scheme);
        this.scheme = scheme;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl username(String username) {
        requireNonNull(username, "username must be non-null");
        this.username = username;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl password(String password) {
        requireNonNull(password, "password must be non-null");
        this.password = password;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl query(String query) {
        requireNonNull(query, "query must be non-null");
        this.query = query;
        return this;
    }

    @Override
    public ElkRequest.Builder uriSuffix(String uriSuffix) {
        requireNonNull(uriSuffix, "uriSuffix must be non-null");
        this.uriSuffix = uriSuffix;
        return this;
    }

    @Override
    public ElkRequestBuilderImpl queryParam(String name, String value) {
        requireNonNull(name, "parameter name must be non-null");
        requireNonNull(value, "parameter value must be non-null");
        this.queryParam.put(name, value);
        return this;
    }

    @Override
    public ElkRequest.Builder GET() {
        this.method = "GET";
        return this;
    }

    @Override
    public ElkRequest.Builder POST() {
        this.method = "POST";
        return this;
    }

    @Override
    public ElkRequest.Builder PUT() {
        this.method = "PUT";
        return this;
    }

    @Override
    public ElkRequest.Builder DELETE() {
        this.method = "DELETE";
        return this;
    }

    @Override
    public ElkRequest.Builder httpRequestConfig(RequestConfig httpRequestConfig) {
        requireNonNull(httpRequestConfig, "httpRequestConfig must be non-null");
        this.httpRequestConfig = httpRequestConfig;
        return this;
    }

    @Override
    public ElkRequest.Builder sslContext(SSLContext sslContext) {
        requireNonNull(sslContext, "sslContext must be non-null");
        this.sslContext = sslContext;
        return this;
    }

    @Override
    public ElkRequest build() {
        if (hostname == null) throw new IllegalStateException("uri is null");
        assert method != null;
        return new ImmutableElkRequest(this);
    }

    static void checkScheme(String scheme) {
        if (!(scheme.equals("https") || scheme.equals("http"))) {
            throw newIAE("invalid URI scheme %s", scheme);
        }
    }

    static void checkURI(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null) throw newIAE("URI with undefined scheme");
        scheme = scheme.toLowerCase(Locale.US);
        if (!(scheme.equals("https") || scheme.equals("http"))) {
            throw newIAE("invalid URI scheme %s", scheme);
        }
        if (uri.getHost() == null) {
            throw newIAE("unsupported URI %s", uri);
        }
    }

    public String hostname() {
        return hostname;
    }

    public int port() {
        return port;
    }

    public String scheme() {
        return scheme;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String query() {
        return query;
    }

    public String uriSuffix() {
        return uriSuffix;
    }

    public String method() {
        return method;
    }

    public Map<String, String> queryParam() {
        return queryParam;
    }

    public RequestConfig httpRequestConfig() {
        return httpRequestConfig;
    }

    public SSLContext sslContext() {
        return sslContext;
    }
}
