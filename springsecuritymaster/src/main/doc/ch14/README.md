![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
```shell
docker run --name springboot-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=pass -d postgres
```
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)
![img_9.png](img_9.png)
![img_11.png](img_11.png)
 authenticationprovider를 지정하면 userdetailsservice를 지정할 필요 없음.
![img_10.png](img_10.png)
![img_12.png](img_12.png)
![img_13.png](img_13.png)
로그아웃은 이렇게 하는게 좋은 방식은 아님. 커스텀 로그아웃 다시 확인.
![img_14.png](img_14.png)

<br>

![img_15.png](img_15.png)
![img_16.png](img_16.png)
![img_17.png](img_17.png)

<br>

![img_18.png](img_18.png)
![img_19.png](img_19.png)
![img_20.png](img_20.png)

<br>

![img_21.png](img_21.png)
![img_22.png](img_22.png)
![img_23.png](img_23.png)

<br>

![img_24.png](img_24.png)
![img_25.png](img_25.png)
![img_26.png](img_26.png)

<br>

![img_27.png](img_27.png)
![img_28.png](img_28.png)
![img_29.png](img_29.png)

<br>

![img_31.png](img_31.png)
![img_30.png](img_30.png)
![img_32.png](img_32.png)

<br>

![img_33.png](img_33.png)
![img_34.png](img_34.png)
![img_35.png](img_35.png)

<br>

![img_36.png](img_36.png)
![img_37.png](img_37.png)
![img_38.png](img_38.png)
![img_39.png](img_39.png)
Rest로 로그인해도 로그인이 유지가 안됨. Form같은 경우는 애초에 초기화 과정에서 세션에 인증을 영속화하기 위한 작업이 추가가 되는 반면, <br>
Rest는 초기화를 위해 명시적인 작업을 추가해줘야한다. <br>

<br>

![img_40.png](img_40.png)
![img_41.png](img_41.png)

<br>

![img_42.png](img_42.png)
![img_43.png](img_43.png)
![img_44.png](img_44.png)
![img_45.png](img_45.png)
![img_46.png](img_46.png)

<br>

![img_47.png](img_47.png)
![img_48.png](img_48.png)




