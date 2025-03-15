![img.png](img.png)
<br>
과정을 살펴보자. extends OncePerRequestFilter는 요청에 대해 한번만 수행하는 Filter이다.
![img_1.png](img_1.png)<br>
![img_2.png](img_2.png)<br>
저걸 상속받으면 doFilterInternal로 들어온다. <br>
authenticationConverter가 있다. 들어가서 확인해보자. <br>
![img_4.png](img_4.png) <br>
authRequest가 null이기 때문에 반환하지 않는다. (로그인 정보를 입력하지 않을시)<br>
정보를 입력했다면 <br>
![img_5.png](img_5.png) <br>
요청 헤더에 실려서 옴 <br>
![img_6.png](img_6.png)
"Basic (문자열)" 입력 된 값이 실려 있는게 보인다. <br>
![img_7.png](img_7.png) <br>
그리고 값들을 추출해서 UsernamePasswordAuthenticationToken에 저장하고 있다. <br>
그리고 return받은 값으로 인증처리를 한다. <br>
![img_8.png](img_8.png) <br>
authenticationIsRequired를 살펴보면 <br>
![img_9.png](img_9.png)<br>
현재 객체내에 인증 객체가 있는지 모는것임. 시큐리티 컨텍스트 안에 인증 객체가 만약에 있다면, 인증할 필요가 없다고<br>
판단하고 인증 처리를 넘기지만, 없으면 인증을 처리한다. 
![img_10.png](img_10.png) <br>
인증을 처리하고 SecurityContext에 저장하고 기타 인증 처리를 한다. 
그리고 securityContextRepository에 저장하고 이건 시큐리티 컨텍스트가 아니고 RequestAttributeSecurtiyContextRepository<br>
요청 컨텍스트임 그러니까 요청 범위내에 시큐리티 컨텍스트를 저장한다. 이렇게 하면 인증을 처리가 된것이다.<br>
그래서 한번 요청이 끝나면 이 요청은 없어진다. 그래서 다시 이 과정을 거쳐야한다. <br>
그럼 한번 다시 요청을 해보자. 
원래라면 요청을 할 때마다 창이 떠서 아이디와 패스워드를 넣고 요청을 해야하고 서버는 헤더를 통해서 다시 인증처리를<br>
해야하는데, 지금같은 경우에는 브라우저에서 요청 헤더에 <br>
![img_11.png](img_11.png)
Authorization에 헤더값이 지정되 있기 때문에 별도로 창이 뜨지 않고 바로 간다.<br>
![img_12.png](img_12.png) <br>
그래서 여기에서 인증처리를 할것인지 안할것인지만 판단하면 된다.<br>
아까처럼 <br>
![img_13.png](img_13.png)<br>
시큐리티 컨텍스트 창에서 인증객체를 가져 오는데, 얘가 null이 아니면 인증을 할 필요가 없다.<br>
세션이면 널이 아니라서 인증할 필요가 없는데, 여기서는 request라서 다시 null이된다. 그렇기 때문에<br>
또 인증 과정을 거쳐야한다.
<br>
![img_14.png](img_14.png) <br>
![img_15.png](img_15.png) <br>
![img_16.png](img_16.png) <br>
![img_17.png](img_17.png) <br>
rememberMe 속성을 넣는다. 넣었더니 <br>
![img_19.png](img_19.png) <br>
속성이 생겼다. 그리고 속성 name도 remember라고 써서 remember로 박혀있다. 이제 쿠키가 생성되는지 보자. <br>
![img_20.png](img_20.png)<br> 
![img_22.png](img_22.png)<br>
쿠키가 생긴것을 확인할 수 있다.
![img_23.png](img_23.png)
그런데 JSessionId를 삭제하고 rememberme만 남기고 새로고침을 해도 인증이 정상적으로 이루어진다. <br>
자 그러면 이게 정말 그런지 서버를 다시 기동해보고 모든 쿠키 지운 다음에 인증을 다시 해보면 rememberme를 체크하지 않고
JSessionId를 지우고 다시 로그인해보면 다시 또 인증을 받아야한다. <br>
근데 .alwaysRemember(true) 속성은 체크박스를 체크하지 않아도 무조건 쿠키를 생기게 한다. 근데 이건 좀...<br>
![img_24.png](img_24.png) <br>
![img_25.png](img_25.png) <br>
코드로 한번 확인해보자. 
![img_26.png](img_26.png)  Start!<br>
앞의 과정을 통화하고 이제 인증을 성공해서 
![img_27.png](img_27.png)  <br>
![img_28.png](img_28.png) <br>
여기서 rememberMeService.loginSuccess가 있다.<br>
![img_29.png](img_29.png)
여기서 parameter는 remember이다. 이 파라미터가 요청 정보의 파라미터와 동일한지 봄(여기선 동일) <br>
![img_30.png](img_30.png) <br>
여기서 이제 쿠키를 만들고 전달하는 여러가지 작업들을 한다. 
![img_31.png](img_31.png) <br>
이 작업들을 하는것. <br>
![img_32.png](img_32.png) <br>
리스폰스로 전달하는것이 보인다. 
이제 다시 로그인을 하면 우리가 checkbox에 설정을 해놨기 때문에<br>
![img_33.png](img_33.png) 
여기로 들어와서  this.securityContextHolderStrategy.getContext().getAuthentication()이 null이 아니라서<br>
자동인증을 수행하지 않는다. <br>
어떻게 하면 null이 될까?  JSESSIONID를 날려주면 된다. 그런데 다시 인증을 받아야함에도 remember라는 쿠키 때문에<br>
인증이 수행이 돼버린다. 
![img_34.png](img_34.png)
null이라서 아래로 넘어간다.
![img_36.png](img_36.png)
![img_35.png](img_35.png)  <br>
최종적으로 autologin이 완성이 된다.

![img_37.png](img_37.png) <br>
![img_38.png](img_38.png) <br>
![img_39.png](img_39.png) <br>
![img_40.png](img_40.png) <br>

테스트 해보자 <br>
![img_41.png](img_41.png) <br>









