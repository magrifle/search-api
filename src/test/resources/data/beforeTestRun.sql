insert into many_entity (id, name) values
(1, 'Margret'),
(2, 'Ingrid');

insert into child_entity (id, name, gender, occupation) values
(1, 'Tobi', 'Male', 'Engineer'),
(2, 'Sarah', 'Female', 'Accountant');

insert into vehicle_entity (id, vehicle_type, numberOfWheels, numberOfDoors) values
(1,'CAR', 4, 4),
(2,'SCOOTER', 3, null);

insert into test_entity (id, name, age, nationality, dateCreated, childEntity_id, role, human, vehicleEntity_id) values
(1,'John Smith', 12, 'Canadian', now(), 1, 'USER', 1, 1),
(2,'Paul Whales', 10, 'Spanish', now(), 2, 'ADMIN', 0, 2),
(3,'Alice Conny', 12, 'Dutch', now(), null, null, null, null),
(4,'Paul Adams', 5, 'Nigerian', now(), null, null, null, null);

insert into test_entity_many_entity (testEntity_id, manyEntities_id) values
(1,1),
(1,2);
