package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.entitiy.PointHistory;
import io.hhplus.tdd.point.entitiy.TransactionType;
import io.hhplus.tdd.point.entitiy.UserPoint;
import io.hhplus.tdd.point.repository.HistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointServiceImpl pointService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Test
    @DisplayName("정상적으로 포인트를 충전한다")
    void chargePoint() {
        // given
        long userId = 1L;
        long amount = 1000L;

        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        UserPoint expectedUserPoint = new UserPoint(userId, amount + amount, System.currentTimeMillis());

        // when
        long expectedAmount = userPoint.point() + amount;

        when(pointRepository.insertOrUpdate(userId, expectedAmount)).thenReturn(expectedUserPoint);
        when(pointRepository.selectById(userId)).thenReturn(userPoint);

        // then
        UserPoint result = pointService.chargePoint(userId, amount);

        assertThat(2000L).isEqualTo(result.point());
        System.out.println("userPoint = " + userPoint);
    }

    @Test
    @DisplayName("포인트를 조회한다")
    void getPoint() {
        // given
        long userId = 1L;
        long point = 1000L;

        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());

        // when
        when(pointRepository.selectById(userId)).thenReturn(userPoint);

        // then
        UserPoint result = pointService.getPointBalance(userId);

        assertThat(userPoint).isEqualTo(result);

        System.out.println("userPoint = " + userPoint);
        System.out.println("result = " + result);
    }

    /**
     * 히스토리 조회
     * 조회 내역 확인
     */
    @Test
    @DisplayName("정상적으로 포인트 충전/사용 내역을 조회할 수 있다")
    void getPointBalanceHis() {
        // given
        long userId = 1L;
        long amount = 5000L;
        long chargePoint = 2000L;
        long usePoint = -1500L;

        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        List<PointHistory> historyRepositories = new ArrayList<>();
        historyRepositories.add(new PointHistory(1L, userId, chargePoint, TransactionType.CHARGE, System.currentTimeMillis()));
        historyRepositories.add(new PointHistory(2L, userId, usePoint,TransactionType.USE, System.currentTimeMillis()));
        // when
        // 포인트 충전/ 사용
        when(historyRepository.selectAllByUserId(userId)).thenReturn(historyRepositories);

        pointService.getHistory(userId);

        // then
        // when
        List<PointHistory> result = pointService.getHistory(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).type()).isEqualTo(TransactionType.CHARGE);
        assertThat(result.get(0).amount()).isEqualTo(2000L);

        assertThat(result.get(1).type()).isEqualTo(TransactionType.USE);
        assertThat(result.get(1).amount()).isEqualTo(-1500L);

    }

    @Test
    @DisplayName("포인트를 사용한다")
    void usePoint() {
        // given
        long userId = 1L;
        long amount = 10000L;
        long usePoint = 1500L;

        UserPoint userPoint = new UserPoint(userId, amount,System.currentTimeMillis());
        UserPoint expectedPoint = new UserPoint(userId, amount - usePoint, System.currentTimeMillis());

        // when
        when(pointRepository.selectById(userId)).thenReturn(userPoint);
        when(pointRepository.insertOrUpdate(userId,amount-usePoint)).thenReturn(expectedPoint);

        // then
        UserPoint result = pointService.usePoint(userId,usePoint);

        assertThat(result.point()).isEqualTo(8500L);

    }

    @Test
    @DisplayName("유저의 포인트가 사용 요청 금액보다 작아 차감할 수 없다")
    void doNotUsePoint() {
        // when
        long userId = 1L;
        long point = 50000L;
        long usePoint = 70000L;

        // then
        when(pointRepository.selectById(userId))
                .thenReturn(new UserPoint(userId, point-usePoint,System.currentTimeMillis()));

        // given
        assertThatThrownBy(() -> pointService.usePoint(userId, 70000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트 잔고가 부족하여 차감할 수 없습니다.");
    }

    @Test
    @DisplayName("유저의 포인트 충전 한도를 초과하여 충전할 수 없다")
    void doNotChargePoint() {
        // given
        long userId = 1L;
        long point = 490000L;
        long chargePoint = 20000L;

        // when
        when(pointRepository.selectById(userId))
                .thenReturn(new UserPoint(userId, point + chargePoint, System.currentTimeMillis()));

        // then
        assertThatThrownBy(() -> pointService.chargePoint(userId, 510000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트 보유 한도를 초과하였습니다.");
    }

    @Test
    @DisplayName("음수 값은 충전할 수 없다")
    void doNotChargePoint2() {
        // given
        long userId = 1L;
        long point = 1000L;

        // when
        when(pointRepository.selectById(userId))
                .thenReturn(new UserPoint(userId, 490000L, System.currentTimeMillis()));

        // then
        assertThatThrownBy(() -> pointService.chargePoint(userId, -1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트는 음수값일 수 없습니다.");
    }
}