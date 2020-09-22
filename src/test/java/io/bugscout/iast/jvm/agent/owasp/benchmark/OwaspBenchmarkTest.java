package io.bugscout.iast.jvm.agent.owasp.benchmark;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.owasp.benchmark.helpers.Utils;
import org.owasp.benchmark.tools.AbstractTestCaseRequest;
import org.testcontainers.shaded.org.apache.commons.lang.time.StopWatch;

//@org.junit.jupiter.api.Disabled
public class OwaspBenchmarkTest {

	private static final String BENCHMARK_CRAWLER_XML = "benchmark-crawler-http.xml";
	
	private static CloseableHttpClient client;
	private static List<AbstractTestCaseRequest> requests;
	
	@BeforeAll
	protected static void beforeAll() throws Exception {

		client = HttpClients.custom().setSSLSocketFactory(Utils.getSSLFactory()).build();

		System.out.println("Reading Owasp XML file");
		
		requests = Utils.parseHttpFile(new FileInputStream(System.getProperty("user.dir") + File.separator + "data" + File.separator + BENCHMARK_CRAWLER_XML));

	}
	
	@AfterAll
	public static void afterAll() {
	}
	
	@TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
		
		return requests.stream().map(request -> {
			return dynamicTest(request.getFullURL(), () -> {
				
				ResponseInfo response = sendRequest(request);
				
				assertTrue(response.getStatusCode()==200);
				
			});
		})
		.collect(Collectors.toList());
		
    }
	
	private static ResponseInfo sendRequest(AbstractTestCaseRequest requestTC) {
		
		ResponseInfo responseInfo = new ResponseInfo();
		HttpRequestBase request = requestTC.buildRequest();
		responseInfo.setRequestBase(request);
		CloseableHttpResponse response = null;

		boolean isPost = request instanceof HttpPost;
		
		System.out.println((isPost ? "POST " : "GET ") + request.getURI());
		
		StopWatch watch = new StopWatch();

		watch.start();
		
		try {
			response = client.execute(request);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		watch.stop();

		try {
			
			HttpEntity entity = response.getEntity();
			
			int statusCode = response.getStatusLine().getStatusCode();
			responseInfo.setStatusCode(statusCode);
			double time = watch.getTime() / 1000;
			responseInfo.setTime(time);
			String outputString = "--> (" + String.valueOf(statusCode) + " : " + time + " sec) ";
			System.out.println(outputString);

			try {
				
				responseInfo.setResponseString(EntityUtils.toString(entity));
				
				EntityUtils.consume(entity);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} 
		finally {
			if (response != null)
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return responseInfo;
	}
	
	static class ResponseInfo {
		private String responseString;
		private double time;
		private int statusCode;
		private HttpRequestBase requestBase;

		public String getResponseString() {
			return responseString;
		}

		public void setResponseString(String responseString) {
			this.responseString = responseString;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public HttpRequestBase getRequestBase() {
			return requestBase;
		}

		public void setRequestBase(HttpRequestBase requestBase) {
			this.requestBase = requestBase;
		}
	}
	
}