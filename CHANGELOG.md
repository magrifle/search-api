### 2.0.3
Support for boolean and enum search with the help of the customization functions

```java
...
 searchKeys.add(new SearchKey("role", Role::valueOf));
 searchKeys.add(new SearchKey("human", Boolean::new));
...
```

### 2.0.2
Support for child entity `@OneToOne`, `@OneToMany`, `@ManyToMany` (up to one level deep :( )


```java

public class Item {
    
    @OneToMany
    List<Passport> passports;
    
    @OneToOne
    Company company;
}

public class Passport {
    String number;
}

public class Company {
    String name;
}

@Configuration
public class ApiSearchConfig {
    ...

    @Bean
    public SearchConfigurer<Item> getSearchKeysForItem() {
        return new SearchConfigurer<Item>() {
            @Override
            public List<SearchKey> getSearchKeys() {
                List<SearchKey> searchKeys = new ArrayList();
                searchKeys.add(new SearchKey("name"));
                searchKeys.add(new SearchKey("pno", "passports.number"));
                searchKeys.add(new SearchKey("company", "company.name"));
                return searchKeys;
            }
        };
    }
}

```
