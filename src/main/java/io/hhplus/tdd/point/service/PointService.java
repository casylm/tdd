package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.entitiy.PointHistory;
import io.hhplus.tdd.point.entitiy.UserPoint;

import java.util.List;

public interface PointService {

    // 4가지 기본 기능 (포인트 조회, 포인트 충전/사용 내역 조회, 충전, 사용)에 대한 구현

    // 포인트 조회
    UserPoint getPointBalance(long userId);

    // 포인트 충전/사용 내역 조회
    List<PointHistory> getHistory(long userId);

    // 포인트 충전
    UserPoint chargePoint(long userId, long amount);

    // 포인트 사용
    UserPoint usePoint(long userId, long amount);

}
