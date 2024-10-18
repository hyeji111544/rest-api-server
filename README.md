## 1. 시작하기

### 1.1. 개발 환경

- OpenJDK 21
- Spring Boot 3.3.4

### 1.2. 라이브러리

- Spring Web
- Lombok
- H2 Database ( ID : pc, PW : 2024 )
- Spring Data JPA : 트랜잭션 관리 와 데이터베이스 연결 및 쿼리 실행
- Spring Validation : 다양한 유효성 검사 어노테이션 제공 (예: @NotNull) 사용
- Spring Boot DevTools : LiveReload를 통한 브라우저 자동 새로고침
- JUnit : 테스트 코드 작성


## 2. 개발 요구사항
- `@ControllerAdvice`, `@ExceptionHandler`를 이용하여, 잘못된 요청에 대한 응답을 처리한다.<br>
 API를 호출할 때, 잘못된 요청이 들어오면 HTTP 400 상태의 `{"reason": 실제사유}`을 응답한다.
```java
@RestControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler(ExceptionApi400.class)
    public ResponseEntity<?> ex400(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("reason", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
```

- 존재하지 않는 API 호출 시, HTTP 404 상태의 `{"reason": 실제사유}`을 응답한다.
```java
 @ExceptionHandler(ExceptionApi404.class)
    public ResponseEntity<?> ex404(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("reason", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
```

- Spring MVC 아키텍처와 Restful API를 준수하여 개발한다.<br>
  HTTP Method와 URI를 적절하게 사용하여 개발한다.
  ```java
   @PostMapping("/users")
    public ResponseEntity<?> save(UserRequest.JoinDTO joinDTO) {
        UserResponse.UsersDTO users= userService.getUsers(joinDTO);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> user(@PathVariable("id") int id) {
        UserResponse.UserDTO user = userService.getUser(id);

        return ResponseEntity.ok(user);
    }
  ```

- 필터 구현 <br>
  URL에 `? & = : //`를 제외한 특수문자가 포함되어 있을경우 접속을 차단하는 Filter 구현한다.
  ```java
   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        // 쿼리의 !! 도 검사하기 위해 추가함
        String queryString = httpRequest.getQueryString();

        // /h2-console 경로는 필터 적용에서 제외
        if (requestURI.startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        // URI 검사
        if (INVALID_URL_PATTERN.matcher(requestURI).find()) {
            sendResponse(httpResponse, "유효하지 않은 문자가 주소에 포함되어 있습니다.");
            return;
        }

        // 쿼리 문자열 검사
        if (queryString != null && INVALID_URL_PATTERN.matcher(queryString).find()) {
            sendResponse(httpResponse, "유효하지 않은 문자가 주소에 포함되어 있습니다.");
            return;
        }

        chain.doFilter(request, response);
    }
  ```
  
 
- Spring AOP를 활용한 로깅, API에 Request시 Console에 Client Agent를 출력한다.
  ```java
  public class AgentHandler {
      @Before("execution(* com.spring.demo.user.UserController..*(..))")
      public void logClientAgent(JoinPoint jp) {
          HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                  .getRequest();
          String clientAgent = request.getHeader("User-Agent");
         
          log.info("clientAgen : " + clientAgent);
      }
  }
  ```
