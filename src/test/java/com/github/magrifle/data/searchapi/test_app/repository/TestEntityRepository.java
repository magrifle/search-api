package com.github.magrifle.data.searchapi.test_app.repository;

import com.github.magrifle.data.searchapi.test_app.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity>
{
}
