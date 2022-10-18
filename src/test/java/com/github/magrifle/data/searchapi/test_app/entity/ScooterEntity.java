package com.github.magrifle.data.searchapi.test_app.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("SCOOTER")
public class ScooterEntity extends VehicleEntity
{

}
