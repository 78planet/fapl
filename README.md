![fapl-logo.png](readme%2Ffapl-logo.png)


## 주요 기능
- 패션 전용 sns 
- 게시글 좋아요 or 싫어요 버튼 
- 좋아요 마일리지 적립
- 마일리지로 기프티콘, FAPL에서 사용할 수 있는 스티커 등 구매 가능 


## 기술 스택 
### Application
- Java17
- Gradle
- Spring Boot 3.1.0
- Spring Data JPA

### Test
- JUnit
- Mockito

### Database
- Mysql 8.0
- Redis
- Flyway 9.4.0

### Infra
- AWS EC2
- AWS S3
- AWS CodeDeploy
- AWS RDS
- Nginx
- Docker
- Github Action
- Jacoco
- Sonarcloud

### Docs
- Swagger


## CI/CD
![cicd.png](readme%2Fcicd.png)
- GitHub Actions의 Workflow 설정으로 CI/CD 과정을 자동화하고 모니터링.
- Pull Request 요청 시 자동으로 SonarQube와 JaCoCo를 활용하여 코드 정적 분석 및 테스트 커버리지 검사 수행.
- 배포는 머지가 이루어진 경우에 실행되며 GitHub Actions, Amazon S3, AWS CodeDeploy, Docker, Nginx 를 활용하여 자동으로 배포 진행.
- Nginx를 활용하여 리버스 프록시 설정 및 로드 밸런싱 등을 구성하여 안정적인 배포 환경 제공.

## Java 스펙을 활용한 코드 품질 관리

'이팩티브 자바', '자바 성능 튜닝 이야기' 등의 책을 참고하여 코드 품질 향상을 목표로 프로젝트 코드를 개선하였습니다. 코드의 양질화를 위해 지속적으로 코드를 점검하고 있습니다.

- Controller 호출 후 비정상 결과시 예외 처리 강화하여 무분별한 If-else 문 및 유효성 검사 코드 제거
- map 대신 명확한 DTO 및 Response 객체 도입으로 개발 및 유지보수 용이성 향상 확인
- 스레드 안정성 강화를 위해 setter 대신 값을 복사한 불변 객체 활용으로 의도치 않은 값의 변화를 억제.
- 불필요한 객체 생성 방지 위해 static final 상수 활용. 이를 통해 객체 생성에 필요한 리소스를 줄일 수 있었음

## REST API 문서화

프로젝트에 Swagger 적용하여 개발한 REST API를 편리하게 문서화하였습니다.

- API 경로, 요청 및 응답 형식, 파라미터, 헤더 등을 자동으로 문서화하여 개발자 간 소통 및 협업 향상.
- API의 예상 입력값, 응답 포맷, 에러 코드 등을 명시하여 사용자 이해도 향상 및 테스트 용이성 향상.
- Swagger를 통해 API의 시각적 표현 및 상호 작용을 제공하므로 개발 생산성과 품질 향상에 기여함.

## 로그인 확인 공통 로직 AOP 적용

문제

- 회원 정보 확인 및 접근 권한 필요한 메소드에서 로그인 여부 확인 중복 문제 발생.
- 프로젝트 요구사항에 따라 로그인 여부를 확인하는 공통 로직을 구현해야 했음.

고려사항

- 중복 코드를 제거하기 위한 세 가지 방법을 고려함:
    1. 필터: 서블릿 컨테이너의 프론트 컨트롤러 앞에서 요청과 응답을 처리하는 방식.
        - 스프링 빈 활용이 어려움.
    2. 인터셉터: DispatcherServlet과 Handler 사이에서 요청 전후 작업을 가로채는 방식.
        - 스프링 빈 활용 가능.
        - 세션과 쿠키 비교로 로그인 확인.
        - 컨트롤러 메서드에서 세션 값 재사용으로 중복 코드 발생.
    3. AOP: 객체지향 프로그래밍에서 중복 기능 분리를 위해 활용.
        - AspectJ를 이용하여 컨트롤러 메서드 실행 전 로그인 여부 확인 및 파라미터 주입.
        - 중복 코드 제거 및 객체지향적 코드 작성 가능.

해결

- 결론적으로 AOP 를 통해 중복 코드를 제거하고 좀 더 객체지향적인 코드를 작성할 수 있었음.
    - @LoginRequired 커스텀 어노테이션 생성.
    - 메소드 위에만 적용하여 로그인 체크 부가 기능을 제거하고 유지보수성 향상.
    - 비즈니스 로직에 집중 가능한 구조 생성.
