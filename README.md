# lolSearcher-Reactive

> 인기 게임 리그오브레전드 게임사로부터 유저들의 게임 데이터를 실시간으로 받아오는 애플리케이션

## 프로젝트 깃 브런치

> - **main** — 실제 메인 브런치(완성본)
> - **develop** — 다음 버전을 위한 개발 브런치(테스트용)


## 프로젝트 커밋 메시지 카테고리

> - [INITIAL] — repository를 생성하고 최초에 파일을 업로드 할 때
> - [ADD] — 신규 파일 추가
> - [UPDATE] — 코드 변경이 일어날때
> - [REFACTOR] — 코드를 리팩토링 했을때
> - [FIX] — 잘못된 링크 정보 변경, 필요한 모듈 추가 및 삭제
> - [REMOVE] — 파일 제거
> - [STYLE] — 디자인 관련 변경사항


## 프로젝트 내 적용 기술

> - 백 앤드
>  - 언어 : Java
>  - WAS : Netty
>  - 프레임 워크 : SpringBoot, Spring Webflux
>  - 빌드 관리 툴 : Gradle
>  - REST API 수집 : WebClient
> - DevOps
>   - Message Queue : Kafka
>   - Cache : Redis

## 기능 요구 사항

> 1. RiotGames 에서 제공하는 REST API 데이터를 받아와서 프론트 서버로 전달
> 2. 받아온 REST API 데이터를 카프카에 저장
> 3. REST API 요청에 대해 캐시를 적용
> 4. 해당 과정들 모두 비동기 처리로 설계할 것
