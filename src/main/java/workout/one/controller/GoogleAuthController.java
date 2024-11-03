//package workout.one.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Controller
//public class GoogleAuthController { // 여기서 Authorization Code 요청하고
//
//    @Value("${google.client-id}")
//    private String clientId;
//
//    @Value("${google.client-secret}")
//    private String clientSecret;
//
//    @Value("${google.redirect-url}")
//    private String redirectUrl;
//
//    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
//
//    @PostMapping("/auth/google/token")
//    public ResponseEntity<String> getAccessToken(@RequestParam("code") String code) {
//        // Access Token 요청을 위한 파라미터 설정
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUrl);
//        params.add("grant_type", "authorization_code");
//        params.add("code", code);
//
//        // 구글로 Access Token 요청
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, Map.class);
//        Map<String, Object> tokenResponse = response.getBody();
//
//        // Access Token과 Refresh Token 저장 또는 반환
//        String accessToken = (String) tokenResponse.get("access_token");
//        String refreshToken = (String) tokenResponse.get("refresh_token");
//
//        // 여기서 추가적으로 accessToken을 사용해 사용자 정보를 가져오거나, 세션/DB에 저장하는 로직을 구현
//        // 예를 들어, 사용자의 email과 같은 기본 정보를 가져와 로그인 처리할 수 있습니다.
//
//        return ResponseEntity.ok("로그인 완료");  // 로그인 완료 메시지를 클라이언트에 전달
//    }
//}
