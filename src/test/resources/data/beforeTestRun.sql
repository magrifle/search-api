insert into many_entity (id, name) values
(1, 'Margret'),
(2, 'Ingrid');

insert into child_entity (id, name) values
(1, 'Tobi');

insert into test_entity (id, name, age, dateCreated, childEntity_id) values
(1,'John Smith', 12, now(), 1),
(2,'Paul Whales', 10, now(), null),
(3,'Alice Conny', 12, now(), null),
(4,'Paul Adams', 5, now(), null);

insert into test_entity_many_entity (testEntity_id, manyEntities_id) values
(1,1),
(1,2);
