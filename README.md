# search-api
[![Build Status](https://travis-ci.org/magrifle/search-api.svg?branch=master)](https://travis-ci.org/magrifle/search-api)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.magrifle/data-search-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.magrifle/data-search-api)

A library that helps you instantly turn your Spring powered endpoints into a query engine.
It makes use of `AOP` to intercept the calls to your `@Controller` or `@RestController` endpoint and then builds a `Specification` from the provided query parameters

**Inspired by** [GitHub Search API](https://developer.github.com/v3/search/)

# Example
````curl
curl http://your.awesome.api/search?q=firstName:Jones,lastName:Fran*,dateCreated>2018-01-01,age<67,city:*ondon*
````

# Configuration

1.  Add the dependency to the pom.xml file of your Spring boot or web MVC project. (Assume of course you're using maven package manager)

````xml
<dependency>
  <groupId>com.github.magrifle</groupId>
  <artifactId>data-search-api</artifactId>
</dependency>
````

2.  Next, you need to define a `@Bean` to enable the search API functionality

````java
@Configuration
public class ApiSearchConfig {
    
    @Bean
    public DataSearchApi dataSearchApi() {
        return new DataSearchApi();
    }
}

````
3) Next, we add a `@Bean` of type `SearchConfigurer` that holds the configuration of your data mapping.
This bean has a method `getSearchKeys()` that contains a list of all the `SearchKey` that are allowed in your search API.

It also contains some the configuration methods like `getDateKeyFormat()` that can be used to specify the allowed date format in the query string of the search API. 

```java
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
                searchKeys.add(new SearchKey("pno", "passportNumber"));
                return searchKeys;
            }
        };
    }
}

```

4)  Finally, in your `@Controller` or `@RestController`, add the `@SearchApi` annotation

````java

@RestController
public class ApiController {

    @Autowired
    private ItemRepository itemRepository;
    ...

    @SearchApi(entity = Item.class)
    @GetMapping("/search")
    public Page<Item> searchItems(SearchBuilder<Item> builder, Pageable pageable){
        return itemRepository.findAll(builder.build(), pageable);
    }
}
````
**Note that your repository class must extend the `JpaSpecificationExecutor<T>` interface in `spring-data` to have access to the `findAll(Specification)` method.**


# Parameters
### `@SearchApi`

| Name | Type | Description |
|---|---|---|
|`queryString`|`String`| `default` `"q"`. This is the query string parameter in the request that contains the search criteria. |
|`keySeparator`|`char`| `default` `","`. The character used to separate different criteria in the `queryString` |
|`entity`|`class`| `required`. The entity class to be queried.|
|`failOnMissingQueryString`|`boolean`| `default` `"false"`. By default, if the `queryString` is empty, the endpoint would query the repository with an empty criteria which translates to `select * ...` in `sql`. You can turn off this behaviour by setting this parameter to `true` in which case a `SearchKeyValidationException` exception is thrown if the `queryString` is missing or does not contain any criteria. |