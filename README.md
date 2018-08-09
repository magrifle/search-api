# search-api
A library that helps you instantly turn your spring powered endpoints into a query engine.

It makes use of `AOP` to intercept the calls to your controller and build a `Specification` from the provided query parameters

#example
```
@SearchApi(type = Item, failOnMissingQueryString = true)
@GetMapping("/search")
public Page<Item> searchItems(EntitySpecification<Item> entitySpecification, Pageable pageable){
    return repository.findAll(entitySpecification, pageable);
}
```
#configuration

1) Add the dependency to the pom.xml file of your Spring boot or web MVC project. (Assume of course you're using maven package manager)

````
<dependency>
 <groupId>might-work</groupId>
    <artifactId>search-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
````

2) Next, you need to define a `Bean` to enable the search API functionality

````
@Configuration
class ApiSearchConfig {
    
    @Bean
    public SearchApiAspect searchApiAspect() {
        return new SearchApiAspect();
    }


@Bean("invoiceItemSearchKeys")
    fun getInvoiceItemSearchKeys() = object : SearchConfigurer<Item>() {
        override fun getSearchKeys(): MutableList<SearchKey> {
            val searchKeys = ArrayList<SearchKey>()
            searchKeys.add(SearchKey("dateClose", "closeDate", true))
            val searchKey = SearchKey("companyId", "idCompany")
            searchKey.isRequired = true
            searchKeys.add(searchKey)
            return searchKeys
        }
    }

    @Bean("invoiceItemHistorySearchKeys")
    fun getInvoiceItemHistorySearchKeys() = object : SearchConfigurer<ItemHistory>() {
        override fun getSearchKeys(): MutableList<SearchKey> {
            val searchKeys = ArrayList<SearchKey>()
            searchKeys.add(SearchKey("dateClose", "closeDate", true))
            searchKeys.add(SearchKey("companyId", "idCompany"))
            return searchKeys
        }
    }

````

```
@SearchApi(type = Item::class, failOnMissingQueryString = true)
    @GetMapping("/bookings2")
    fun getInvoiceItems2(specification: EntitySpecification<Item>, pageable: Pageable): Page<Item>? =
        invoiceItemService.findAll(specification, pageable)

```
