package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.entitiy.PointHistory;
import io.hhplus.tdd.point.entitiy.TransactionType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryRepositoryImpl implements HistoryRepository{

    PointHistoryTable pointHistoryTable = new PointHistoryTable();

    @Override
    public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
        PointHistory pointHistory = pointHistoryTable.insert(userId,amount,type,updateMillis);
        return pointHistory;
    }

    @Override
    public List<PointHistory> selectAllByUserId(long userId) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(userId);
        return pointHistories;
    }
}
