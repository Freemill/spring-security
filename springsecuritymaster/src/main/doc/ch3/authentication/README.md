![img.png](img.png)
![img_1.png](img_1.png)
사용자가 요청을 하면  DelegatingFilterProxy가 이 요청을 Spring Container 쪽으로 넘겨준다. <br>
FilterChainProxy에다가 요청을 넘겨주고 여기서 이놈이 가지는 여러개의 시큐리티 필터들을 하나씩 호출하면서 <br>
그러던 중 Authentication Filter를 호출한다. 그럼 이 AuthenticationFilter는 Authentication 객체를 만든다. <br>
이놈은 AuthenticationFilter가 사용자의 요청을 가지고 Authentication에 정보를 담는데 그 역할을 한다. <br>
그리고 쭉 가다가 또 쭉 가다가 오른쪽에 Authentication이 또 잇는데 이놈은 AuthenticationManager -> AuthenticationProvider로 <br>
가다가 아때는 인증에 성공한 이후에 Authentication객체를 만든 후에 다시 Manager로 ruturn하는 것이다. <br>
AuthenticationManager는 AuthenticationProvider에게 사용자의 정보가 맞는지의 처리를 위임한다. AuthenticationProvider는 <br>
UserDetailService를 통해서 사용자의 정보를 인증하기 위해 사용자의 정보를 가지고 온다. 그리고 UserDetailService는 처리를 한다음 <br>
UserDetails를 반환한다. 이건 다시 AuthenticationProvider로 와서 사용자 객체가 null이거나 없으면 실패처리하면 되고 성공 했다면 다시 <br>
AuthenticationProvider에서 Authentication을 만든다. 그때 이게 맞는지 확인하는 과정에서 PasswordEncoder를 통해서 확인한다. <br>
그리고 AuthenticationManager는 이 객체를 AuthenticationFilter까지 넘겨준다. 그렇게 되면 이 필터가 최종적으로 Authentication 이 객체를 <br>
SecurityContext에 SecurityContextHodler를 통해서 저장하는 것이다. 그렇게 되면 SecurityContext에는 Authentication이 저장돼 있고 <br>
Authentication에는 UserDetails가 저장돼 있다. 

**여기까지가 인증 process이다.**
<br>
![img_2.png](img_2.png)
![img_3.png](img_3.png)
<br>
코드로 보자.
![img_4.png](img_4.png)
처음 인증에 성공할 수 있게 로그인 해보자.<br>
![img_5.png](img_5.png) <br>
이렇게 하면 현재는 아래 그림에서 FilterChainProxy를 거쳐서 AuthenticationFilter로 가려는 상황이다. <br>
![img_1.png](img_1.png)
위위 그림에서 attempAuthentication하면 아래 그림으로 간다. <br>
![img_7.png](img_7.png) <br>
UsernamePasswordAuthenticationToken으로 들어가 보면 <br>
![img_8.png](img_8.png)
AbstractAuthenticationToken을 상속받고 있고 또 이건 Authentication을 상속받고 있고 또 이건 최종적으로 Principal을 상속받고 있다.<br>
![img_9.png](img_9.png)
지금 보면 unauthenticated를 호출해서 저장을 하고 있다. <br>
![img_10.png](img_10.png)
그래서 지금 unauthenticated를 찾아들어가면 새로운 객체를 만들 때, 이름하고 비밀번호를 입력하고 있다. <br>
그리고 
![img_11.png](img_11.png)<br>
현재 인증을 받기 전이니까 권한은 없다. <br>
![img_12.png](img_12.png) <br>
이 인증 객체를 만들어서 AuthenticationManager에 주고 있다. <br>
![img_13.png](img_13.png)
순서를 보면 지금 이제 AuthenticationManager까지 옴. <br>
![img_14.png](img_14.png) <br>
그리고 authenticate로 들어가보면 <br>
![img_16.png](img_16.png)
이렇게 생겼다. <br>
그리고 이건 ProviderManager에 있고 이 클래스는 AuthenticationManager의 구현체이다. <br>
![img_15.png](img_15.png)
![img_17.png](img_17.png) <br>
그리고 provider 이 클래스가 사용자의 인증 처리를 총괄하는 그런 클래스이다. <br>
그래서 이 클래스에 단지 위임하고 있다. <br>
![img_18.png](img_18.png)
그리고 지금 authenticate로 들어와있다. <br>
들어온 authenticate 객체를 가지고 위의 시큐리티 인증 / 인가흐름도의 그림의 AuthenticationProvider로 왔다. <br>
그리고 그 다음 UserDetailService를 이용한다. 우리는 지금 메모리에 정보를 하나 저장해 놓았다. <br>
![img_19.png](img_19.png) <br>
저장돼 있는 모습.
![img_20.png](img_20.png)
그리고 UserDetailsService로 부터 사용자 정보를 하나 가지고 온다. <br>
![img_21.png](img_21.png) 여기서걸국 getUserDetailsService()가 <br>
![img_22.png](img_22.png)
이 정보를 가져오겠다는 소리다. <br>
그리고 이렇게 사용자 정보를 가져오면 <br>
![img_23.png](img_23.png) 
이제는 password만 검증하면 된다. <br>
![img_24.png](img_24.png) 
이제 <br>
![img_26.png](img_26.png)
패스워드 체크를 PasswordEncoder를 통해서 체크하면 된다. <br>
그리고 여기까지 하면 사용자의 검증이 끝난다.
![img_27.png](img_27.png)
그리고 인증에 성공하게 되면 또 다른 Authentication 객체를 만들게 된다. 근데 이 객체는 인증 성공한 최종 결과물을 저장한다.<br>
그리고 그 결과물의 하나는 User객체 와 또 하나는 권한에 관한 객체이다.<br>
User 객체가 위의 그림의 파라미터상 Principal이고 또 하나가 권한 정보이다. 그리고 이 정보들로 최종 리턴한다. <br>
![img_28.png](img_28.png)
최종적으로 리턴 됨. 그리고 이게 다시 역으로 AuthenticationFilter까지 전달 됨. <br>
![img_29.png](img_29.png)
![img_30.png](img_30.png)
그리고 이 정보를 다시 SecurityContext에 저장함. <br>
![img_31.png](img_31.png) <br>




 
















 