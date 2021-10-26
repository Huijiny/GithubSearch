# GithubSearch

### ✔️ Description

[Github API](https://docs.github.com/en/rest) 중 [search repository](https://docs.github.com/en/rest/reference/search#search-repositories) api를 사용하였으며, 레포지토리를 **검색할** 수 있습니다.

**star 개수를 기준**으로 **내림차순하여** 인기있는 레포지토리부터 볼 수 있습니다.



### 📱GIF

<img src="README.assets/screengif.gif" alt="screengif" style="zoom:30%;" />



### 🛠 Used tech stack

Kotlin

MVVM

Rx

Databinding, Retrofit2, OkHttp3



### 🪜Project Structure with brief description

```defalut
java.com.huijiny.githubsearch
 ┣ base
 ┃ ┣ BaseViewModel.kt
 ┃ ┗ BindingActivity.kt
 ┣ data
 ┃ ┣ model
 ┃ ┃ ┣ response
 ┃ ┃ ┃ ┣ OwnerResponse.kt
 ┃ ┃ ┃ ┣ RepositoriesResponse.kt
 ┃ ┃ ┃ ┗ RepositoryResponse.kt
 ┃ ┃ ┣ Owner.kt
 ┃ ┃ ┣ Repositories.kt
 ┃ ┃ ┣ Repository.kt
 ┃ ┃ ┗ RepositoryMapper.kt
 ┃ ┗ service
 ┃ ┃ ┗ SearchService.kt
 ┣ network
 ┃ ┣ GithubInjection.kt
 ┃ ┗ GithubRetrofit.kt
 ┣ repository
 ┃ ┗ SearchRepository.kt
 ┗ ui
 ┃ ┣ MainActivity.kt
 ┃ ┣ MainAdapter.kt
 ┃ ┗ MainViewModel.kt
```

> 화면 하나의 규모가 작은 앱이라고 판단하여 fragment 대신 activity에 구현했습니다.

- base 
  - BaseViewModel
    - 뷰모델 베이스코드로 사용하며, compositeDisposiable에 disposable을 추가하고 clear합니다.
  - BindingActivity 
    - DataBinding을 위한 Activity 베이스 코드입니다. 
- data
  - 데이터 모델과 mapper가 있습니다.
- service
  - SearchService가 있으며, 쿼리 종류 중, star와 desc는 기본 파라메터로 구현해놨습니다.
- network
  - GithubInjection
    - Retrofit빌더에 service를 create하여 반환받은 service를 provide하는 함수를 만들어놨습니다. repository 생성시에 service를 넘길 때 사용됩니다.
  - GithubRetrofit
    - OkHttp Interceptor로 네트워크 통신 시 Body를 로깅할 수 있습니다.
    - Service를 create하는 레트로핏 빌더 함수가 있습니다.
- repository
  - viewmodel에서 repository를 실행하며 결과값을 Single로 받습니다.
- ui
  - MainActivity와 MainAdapter, MainViewModel이 있습니다.
  - TextWatcher를 통해 글자 변화를 감지하며 debounce를 사용하여 일정시간 이후 검색쿼리를 viewmodel searchRepository 함수에 전달합니다.
  - api 결과 값에서 에러가 발생한다면 에러 코드 별 String 값 Id 데이터를 발행하며, MainActivity에서 구독하여 Toast로 메세지를 띄워줍니다.







