package com.example.demo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
	//	return getTemplate("JKS", "nonprod.jks", "Youmustchange@1");
		RestTemplate restTemplate = new RestTemplate()
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate
	}

	public RestTemplate getTemplate(String type, String keystoreFile, String password) {
		RestTemplate restTemplate = null;
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient(type, keystoreFile, password));
		restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}

	public HttpClient httpClient(String type, String keystoreFile, String password){
		HttpClient httpClient = null;
		try {
			SSLContextBuilder sslContextBuilder = SSLContexts.custom();
			KeyStore devKeys = KeyStore.getInstance(type);

			devKeys.load(new ClassPathResource(keystoreFile).getInputStream(), password.toCharArray());
			sslContextBuilder = sslContextBuilder.loadKeyMaterial(devKeys, password.toCharArray());
			sslContextBuilder = sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());

			SSLContext sslContext = sslContextBuilder.build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		}catch (NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException | KeyManagementException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpClient;
	}
}

class NoopHostnameVerifier implements javax.net.ssl.HostnameVerifier {
	public boolean verify(java.lang.String hostname, javax.net.ssl.SSLSession session) {
		return true;
	}
}