package test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class HelloServletTest {

	public String send(String url, String request) throws Exception {

		String response = null;
		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("charset", "GBK");
		method.addRequestHeader("content-type", "application/x-www-form-urlencoded");
		method.setRequestEntity(new StringRequestEntity(request, null, "GBK"));

		try {
			int retcode = httpClient.executeMethod(method);
			if (retcode == HttpStatus.SC_OK) {
				byte[] responseBody = new byte[10240];
				java.io.InputStream istream = method.getResponseBodyAsStream();
				int npos = 0;
				int nread = 0;
				while ((nread = istream.read(responseBody, npos, responseBody.length - npos)) >= 0) {
					npos += nread;
					if (npos >= responseBody.length) {
						byte[] tmpBuf = new byte[npos + 51200];
						System.arraycopy(responseBody, 0, tmpBuf, 0, npos);
						responseBody = tmpBuf;
					}
				}
				response = new String(responseBody, 0, npos);
			} else {
				throw new IOException("发送失败: retcode: " + retcode);
			}
			method.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void main(String[] args) throws Exception {
		String reqStr = "<?xml version=\"1.0\" encoding=\"GBK\"?><request><params><param><curDate>20170714</curDate></param></params></request>";
		HelloServletTest sender = new HelloServletTest();
		String reponse = sender.send("http://localhost:8080/test/hello", reqStr);
		System.out.println("返回报文:");
		System.out.println(reponse);
	}

}
