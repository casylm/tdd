package io.hhplus.tdd.point.entitiy;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
//
//    public static final long MIN_VALUE = 0L;
//    public static final long MAX_VALUE = 500000L; // 포인트의 최대 한도는 50만원으로 정의한다.
//
//    public UserPoint{
//        if(point < MIN_VALUE) {
//            throw new IllegalArgumentException("잔고는 0이하가 될 수 없습니다.");
//        }
//
//        if(point > MAX_VALUE) {
//            throw new IllegalArgumentException("포인트 적립 한도를 초과하였습니다");
//        }
//    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

//    public long getAmount(long id) {
//        return point;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public long addAmount(long id, long amount) {
//        return point+amount;
//    }
}
