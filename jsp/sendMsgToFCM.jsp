<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.security.*, java.io.*, java.net.*" %>
<%
String result = "";

// 1. fcm 서버정보 세팅
String fcm_url = "https://fcm.googleapis.com/fcm/send";
String content_type = "application/json";
String server_key = "AAAAahBNznU:APA91bE_Wwup7hAH0kdrU9Fql5HIDzm74XWL_yh6wASQXzkjzCeSjyoEvTrWf0k8EideiB5zw9r49DIVDjoTy3J6Bcm301NADwuWG3_XcjgHpQ9L8xWDN6H5O0Ib_ulSM3PAVEyrf-T_";

// 2. 메시지정보를 클라이언트(핸드폰)로 부터 수신
request.setCharacterEncoding("utf-8");  // 요청값이 한글일 경우 처리

String to_token = request.getParameter("to_token");
String msg      = request.getParameter("msg");

// 3. fcm 서버로 메시지를 전송
// 3.1 수신한 메시지를 json 형태로 변경해준다.
String json_string = "{\"to\": \""+to_token+"\", \"notification\": { \"title\":\"FCM TEST\" , \"body\": \"" + msg + "\"}}";

// 3.2 HttpUrlConnection 을 사용해서 FCM서버측으로 메시지를 전송한다
//     a.서버연결
URL url = new URL(fcm_url);
HttpURLConnection con = (HttpURLConnection) url.openConnection();
//     b.header 설정
con.setRequestMethod("POST");
con.setRequestProperty("Authorization","key="+server_key);
con.setRequestProperty("Content-Type",content_type);
//     c.POST데이터(body) 전송
con.setDoOutput(true);
OutputStream os = con.getOutputStream();
os.write(json_string.getBytes());
os.flush();
os.close();
//     d.전송후 결과처리
int responseCode = con.getResponseCode();
if(responseCode == HttpURLConnection.HTTP_OK){ // code 200
	// 결과처리후 FCM 서버측에서 발송한 결과메시지를 꺼낸다.
	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	String dataLine = "";
	// 메시지를 한줄씩 읽어서 result 변수에 담아두고 
	while((dataLine = br.readLine()) != null){
		result = result + dataLine;
	}
	br.close();
}

out.print("RESULT:"+result);
%>