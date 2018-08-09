# search-api
A library that helps you instantly turn your spring API to a query engine

#example

````
@Configuration
class ApiSearchConfig {

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

    @Bean
    fun searchApiAspect() = SearchApiAspect()

}

````

```
@SearchApi(type = Item::class, failOnMissingQueryString = true)
    @GetMapping("/bookings2")
    fun getInvoiceItems2(specification: EntitySpecification<Item>, pageable: Pageable): Page<Item>? =
        invoiceItemService.findAll(specification, pageable)

```
