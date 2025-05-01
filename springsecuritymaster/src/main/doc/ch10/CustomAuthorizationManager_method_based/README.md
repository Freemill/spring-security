![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
```java
@Service
public class DataService {

    @PreAuthorize(value = "")
    public String getUser() {
        return "user";
    }

    @PostAuthorize(value = "")
    public Account getOwner(String name) {
        return new Account(name, false);
    }

    public String display() {
        return "display";
    }
}
```