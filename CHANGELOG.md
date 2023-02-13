# What's new - 2.0.9?
- Support for space and @ in search value

### 2.0.8
- Allow case-insensitive entity search
- Update vulnerable dependencies

### 2.0.5
- Allow support for `@Discriminators` when fetching entities

```java

@Entity
@DiscriminatorValue("CAR")
public class CarEntity extends VehicleEntity
{
    private Integer numberOfDoors;
}


@Entity(name = "vehicle_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public class VehicleEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer numberOfWheels;

}

//bean config
...
SearchKey numberOfDoors = new SearchKey("numberOfDoors", "vehicleEntity.numberOfDoors");
numberOfDoors.setType(CarEntity.class);
searchKeys.add(numberOfDoors);
``` 

### 2.0.4
Allow query for collections like `q=id:[1_2_30]` // filter where `id` in `1,2,30`

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
