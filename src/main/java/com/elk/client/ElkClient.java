package com.elk.client;

import com.elk.dto.ElkResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elk.request.ElkRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * ELK Client : Uses CloseableHttpClient to call ELK/OpenSearch server
 *
 * @author Manjunath Melavanki
 * date    17-11-2023
 */
public class ElkClient {
    static Logger log = LogManager.getLogger(ElkClient.class);
    public static final String CONTENT_TYPE = "Content-Type";

    private ElkClient() {
    }

    public static <T> ElkResponse<T> execute(ElkRequest elkRequest, Class<T> valueType) {

        HttpHost targetHost = new HttpHost(elkRequest.hostname(), elkRequest.port(), elkRequest.scheme());
        HttpClientBuilder clientBuilder;
        HttpClientContext context;
        HttpUriRequest request;
        int statusCode;
        T t = null;
        String message = null;
        try {
            context = getHttpClientContext(elkRequest, targetHost);
            clientBuilder = getHttpClientBuilder(elkRequest);
            request = prepareHttpRequest(elkRequest, targetHost);
        } catch (Exception e) {
            log.error("Error occurred :{}", e.getMessage());
            return new ElkResponse<>(0, null, e.getMessage(), e);
        }
        ElkResponse<T> elkResponse;
        try (CloseableHttpClient httpClient = clientBuilder.build();
             CloseableHttpResponse httpResponse = httpClient.execute(request, context)) {
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                ObjectMapper om = new ObjectMapper();
                if (valueType.equals(String.class)) {
                    t = (T) EntityUtils.toString(httpResponse.getEntity());
                } else {
                    t = om.readValue(EntityUtils.toString(httpResponse.getEntity()), valueType);
                }

            } else {
                log.debug("Status code :{}", statusCode);
                message = httpResponse.getStatusLine().getReasonPhrase();
            }

            elkResponse = new ElkResponse<>(statusCode, t, message, null);
            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            log.error("Error occurred :{}", e.getMessage());
            elkResponse = new ElkResponse<>(0, null, e.getMessage(), e);
        }

        return elkResponse;
    }

    private static HttpUriRequest prepareHttpRequest(ElkRequest elkRequest, HttpHost targetHost) throws UnsupportedEncodingException {
        String method = "GET";
        HttpUriRequest httpUriRequest = null;
        if (StringUtils.isNotBlank(elkRequest.method())) {
            method = elkRequest.method();
        } else {
            log.info("Setting HTTP method by default GET");
        }

        URI uri = URI.create(targetHost.toURI() + "/" + elkRequest.uriSuffix());
        log.debug("URI : {}", uri);
        switch (method) {
            case "GET":
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(uri);
                httpUriRequest = httpGet;
                break;
            case "POST":
                HttpPost httpPost = new HttpPost();
                httpPost.setURI(uri);
                httpPost.setEntity(prepareQuery(elkRequest));
                httpUriRequest = httpPost;
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut();
                httpPut.setURI(uri);
                httpPut.setEntity(prepareQuery(elkRequest));
                httpUriRequest = httpPut;
                break;
            case "DELETE":
                HttpDelete httpDelete = new HttpDelete();
                httpDelete.setURI(uri);
                httpUriRequest = httpDelete;
                break;
            default:
                HttpGet httpDefaultGet = new HttpGet();
                httpDefaultGet.setURI(uri);
                httpUriRequest = httpDefaultGet;
                break;
        }
        httpUriRequest.setHeader(CONTENT_TYPE, String.valueOf(ContentType.APPLICATION_JSON));
        return httpUriRequest;

    }

    private static HttpEntity prepareQuery(ElkRequest elkRequest) throws UnsupportedEncodingException {

        String finalQuery = elkRequest.query();
        for (Map.Entry<String, String> e : elkRequest.queryParam().entrySet()) {
            finalQuery = finalQuery.replaceAll("@" + e.getKey(), e.getValue());
        }
        log.debug("Final Query : {}", finalQuery);
        return new StringEntity(finalQuery);
    }

    private static HttpClientContext getHttpClientContext(ElkRequest elkRequest, HttpHost targetHost) {

        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elkRequest.username(), elkRequest.password()));

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credProvider);
        context.setAuthCache(authCache);
        return context;
    }

    private static HttpClientBuilder getHttpClientBuilder(ElkRequest elkRequest) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContext sslContext = null;
        if (null == elkRequest.sslContext()) {
            log.info("Using default SSLContext");
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy);
            sslContext = sslContextBuilder.build();
        } else {
            sslContext = elkRequest.sslContext();
        }

        RequestConfig requestConfig = null;
        if (null == elkRequest.httpRequestConfig()) {
            log.info("Using default Http RequestConfig");
            requestConfig = RequestConfig
                    .custom()
                    .setConnectionRequestTimeout(300000)
                    .setConnectTimeout(300000)
                    .setSocketTimeout(300000)
                    .build();
        } else {
            requestConfig = elkRequest.httpRequestConfig();
        }

        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(sslContext);

    }
}
