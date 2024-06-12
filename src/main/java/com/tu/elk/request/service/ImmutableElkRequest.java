package com.tu.elk.request.service;

import com.tu.elk.request.ElkRequest;
import org.apache.http.client.config.RequestConfig;

import javax.net.ssl.SSLContext;
import java.util.Map;
import java.util.Objects;

/**
 * ImmutableElkRequest
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public final class ImmutableElkRequest extends ElkRequest {

    public final String hostname;
    public final int port;
    public final String scheme;
    public final String username;
    public final String password;
    public final String query;
    public final String method;
    public final String uirSuffix;
    public final Map<String, String> queryParam;
    public final RequestConfig httpRequestConfig;
    public final SSLContext sslContext;

    public ImmutableElkRequest(ElkRequestBuilderImpl elkRequestBuilder) {
        this.hostname = Objects.requireNonNull(elkRequestBuilder.hostname());
        this.port = elkRequestBuilder.port();
        this.scheme = Objects.requireNonNull(elkRequestBuilder.scheme());
        this.username = Objects.requireNonNull(elkRequestBuilder.username());
        this.password = Objects.requireNonNull(elkRequestBuilder.password());
        this.query = elkRequestBuilder.query();
        this.queryParam = elkRequestBuilder.queryParam();
        this.uirSuffix = Objects.requireNonNull(elkRequestBuilder.uriSuffix());
        this.method = elkRequestBuilder.method();
        this.httpRequestConfig = elkRequestBuilder.httpRequestConfig();
        this.sslContext = elkRequestBuilder.sslContext();
    }


    @Override
    public String hostname() {
        return hostname;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public String scheme() {
        return scheme;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String query() {
        return query;
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public Map<String, String> queryParam() {
        return queryParam;
    }

    @Override
    public String uriSuffix() {
        return uirSuffix;
    }

    @Override
    public RequestConfig httpRequestConfig() {
        return httpRequestConfig;
    }

    @Override
    public SSLContext sslContext() {
        return sslContext;
    }
}
