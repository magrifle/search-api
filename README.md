# search-api
A library that helps you instantly turn your spring powered endpoints into a query engine.

It makes use of `AOP` to intercept the calls to your controller and build a `Specification` from the provided query parameters

# Example
````
@SearchApi(type = Item)
@GetMapping("/search")
public Page<Item> searchItems(EntitySpecification<Item> entitySpecification, Pageable pageable){
    return repository.findAll(entitySpecification, pageable);
}
````
# Configuration

1.  Add the dependency to the pom.xml file of your Spring boot or web MVC project. (Assume of course you're using maven package manager)

````xml
<dependency>
 <groupId>might-work</groupId>
    <artifactId>search-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
````

2.  Next, you need to define a `Bean` to enable the search API functionality

````java
@Configuration
public class ApiSearchConfig {
    
    @Bean
    public SearchApiAspect searchApiAspect() {
        return new SearchApiAspect();
    }
}

````
3) Next, we add a `Bean` of type `SearchConfigurer` that holds the configuration of your data mapping. 
This bean has a method `getSearchKeys()` that contains a list of all the `SearchKey` that are allowed in your search API.

It also contains some the configuration methods like `getDateKeyFormat()` that can be used to specify the allowed date format in the query string of the search API. 

```java
@Configuration
public class ApiSearchConfig {

...
@Bean
public SearchConfigurer getSearchKeysForItem() {
return SearchConfigurer(){
   getSearchKeys() {
    List<SearchKey> searchKeys = new ArrayList<>();
    searchKeys.add(new SearchKey("firstName", "firstNameInEntity"));
    searchKeys.add(new SearchKey("dateCreated","createdDateInEntity", true);
    return searchKeys;
   }
}

```

