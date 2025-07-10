package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.entitiy.PointHistory;
import io.hhplus.tdd.point.entitiy.TransactionType;

import java.util.List;

public interface HistoryRepository {
    public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis);
    public List<PointHistory> selectAllByUserId(long userId);
}
