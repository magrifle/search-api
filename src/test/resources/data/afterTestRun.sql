alter table test_entity drop constraint if exists fk_child_entity;
alter table test_entity drop constraint if exists fk_many_entities;
alter table test_entity drop constraint if exists fk_vehicle_entity;
alter table test_entity drop constraint if exists fk_test_entity;

truncate table child_entity;
truncate table many_entity;
truncate table vehicle_entity;
truncate table test_entity;
