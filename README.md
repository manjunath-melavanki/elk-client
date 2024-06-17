**Common Java Client for Elasticsearch/OpenSearch (elk-client)**

Please refer detail documentation here : elk-doc.docx

**Maven Dependency :**
<dependency>
    <groupId>com</groupId>
    <artifactId>elk-client</artifactId>
    <version>1.0.0</version>
</dependency>

**Request Creation :**
ElkRequest elkRequest = ElkRequest.newBuilder()
.scheme()     //http or https
.hostname()   //Hostname
.port()       //Server port
.uriSuffix()  //indexes etc, Ex: index/_search?pretty&size=0
.username()   //Username
.password()   //Password
.query()      //Query
.queryParam() // Variables : key-value
.sslContext() // SSLContext parametrs
.httpRequestConfig() // Http request configs
.POST()       // HTTP method : GET(), POST(), PUT(), DELETE(). By default it will be GET().
.build();

**Client:**
ElkResponse<object_type> elkResponse = ElkClient.execute(elkRequest, object_class)

**Response**
public class ElkResponse<T> {
private final int statusCode;
private final T entity;
private final String message;
private final Exception exception;
}