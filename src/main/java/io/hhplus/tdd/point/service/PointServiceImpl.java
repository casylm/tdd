package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.entitiy.PointHistory;
import io.hhplus.tdd.point.entitiy.UserPoint;
import io.hhplus.tdd.point.entitiy.TransactionType;
import io.hhplus.tdd.point.repository.HistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;
    private final HistoryRepository historyRepository;

    /**
     * 조회
     * @param userId
     * @return
     */
    @Override
    public UserPoint getPointBalance(long userId) {

        // 유저 아이디로 조회
        UserPoint user = pointRepository.selectById(userId);

        return user;
    }

    /**
     * 내역 조회
     * @param userId
     * @return
     */
    @Override
    public List<PointHistory> getHistory(long userId) {

        return historyRepository.selectAllByUserId(userId);
    }

    /**
     * 충전
     * @param id
     * @param amount
     * @return
     */
    @Override
    public UserPoint chargePoint(long id, long amount) {

        UserPoint userPoint = pointRepository.selectById(id);

        long total = userPoint.point() + amount;

        // 정책1. 포인트는 최대 50만원까지 충전 가능하다.
        if (total > 500000L) {
            throw new IllegalArgumentException("포인트 보유 한도를 초과하였습니다.");
        }

        // 정책2. 음수값은 충전할 수 없다.
        if(amount < 0) {
            throw new IllegalArgumentException("포인트는 음수값일 수 없습니다.");
        }

        userPoint = pointRepository.insertOrUpdate(id, total);
        historyRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return userPoint;
    }

    /**
     * 사용
     * @param id
     * @param amount
     * @return
     */
    @Override
    public UserPoint usePoint(long id, long amount) {

        UserPoint userPoint = pointRepository.selectById(id);

        long balance = userPoint.point() - amount;

        if(balance < 0) {
            throw new IllegalArgumentException("포인트 잔고가 부족하여 차감할 수 없습니다.");
        }

        userPoint = pointRepository.insertOrUpdate(id,balance);
        historyRepository.insert(id,amount, TransactionType.USE ,System.currentTimeMillis());

        return userPoint;
    }

    // 4가지 기본 기능 (포인트 조회, 포인트 충전/사용 내역 조회, 충전, 사용)에 대한 구현

    // 포인트 조회
    // UserPoint getPointBalance(유저 아이디)

    // 포인트 충전/사용 내역 조회
    // List<PointHistory> getHistory(유저 아이디)

    // 포인트 충전
    // chargePoint(유저 아이디, 금액)

    // 포인트 사용
    // usePoint(유저 아이디, 금액)
}
