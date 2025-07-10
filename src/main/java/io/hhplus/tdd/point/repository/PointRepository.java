package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.entitiy.UserPoint;

public interface PointRepository {
    public UserPoint insertOrUpdate(long id, long amount);
    public UserPoint selectById(Long id);
}
