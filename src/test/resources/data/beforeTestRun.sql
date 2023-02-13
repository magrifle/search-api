insert into many_entity (id, name) values
(1, 'Margret'),
(2, 'Ingrid');

insert into child_entity (id, name) values
(1, 'Tobi');

insert into vehicle_entity (id, vehicle_type, numberOfWheels, numberOfDoors) values
(1,'CAR', 4, 4),
(2,'SCOOTER', 2, null);

insert into test_entity (id, name, age, email, dateCreated, childEntity_id, role, human, vehicleEntity_id) values
(1,'John Smith', 12, 'john.smith@email.com', now(), 1, 'USER', 1, 1),
(2,'Paul Whales', 10, 'paul.whales@email.com', now(), null, null, 0, null),
(3,'Alice Conny Victoria', 12, 'alice.conny@email.com', now(), null, null, null, null),
(4,'Paul Adams', 5, 'paul.adams@email.com', now(), null, null, null, null);

insert into test_entity_many_entity (testEntity_id, manyEntities_id) values
(1,1),
(1,2);
