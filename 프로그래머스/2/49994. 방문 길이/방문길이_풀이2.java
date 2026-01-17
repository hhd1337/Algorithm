import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/* Functional Interface + Enum + Lambda를 결합한 전략 패턴
상황) 전략의 개수가 제한적일 때 (U,D,L,R)
상황) 각 전략은 함수 1개짜리 로직이다

1. 그 함수의 시그니처를 인터페이스(RoadMoveStrategy)로 통일한다 (@FunctionalInterface)
2. 전략 종류가 고정되어 있으므로 enum으로 묶는다
3. 각 enum 상수는 그 전략을 람다(익명함수) 필드로 갖는다
4. enum 자체가 그 인터페이스의 구현체 역할도 한다
5. 호출자는 “U가 무슨 전략인지” 몰라도 command(p)만 호출하면 된다
*/

class Solution {
    public static int solution(String dirs) {
        Set<Road> visited = new HashSet<>();
        Point currPoint = new Point(0, 0);

        for (char command : dirs.toCharArray()) {
            RoadMover mover = RoadMover.valueOf(Character.toString(command));
            Optional<Road> currRoad = mover.command(currPoint);
            if (currRoad.isPresent()) {
                Road road = currRoad.get();
                visited.add(road);
                currPoint = road.getEndPoint();
            }
        }
        return visited.size();
    }
}

class Road {
    private Point startPoint;
    private Point endPoint;

    public Road(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public static Road of(Point startPoint, Point endPoint) {
        if (endPoint.isInBoundary()) {
            return new Road(startPoint, endPoint);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { // 같은 객체이면 무조건 true
            return true;
        }
        // 비교 객체가 null이거나, 클래스 타입이 다르면 무조건 false
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        // 이제 o가 Road라고 확정되었으니 다운캐스팅
        Road road = (Road) o;

        // start, end가 반대여도 같은 길(Road)로 본다.
        return (startPoint.equals(road.startPoint) && endPoint.equals(road.endPoint)) ||
                (startPoint.equals(road.endPoint) && endPoint.equals(road.startPoint));

    }

    @Override
    public int hashCode() {
        // Objects.hash(start, end)는 내부적으로 (start, end) 튜플 순서를 고려한다.
        // 그래서 hash(A, B) ≠ hash(B, A) 이다.
        // HashSet/HashMap에서는 hashCode가 다르면 서로 다른 버킷으로 분류되고
        // equals 비교조차 안 해서, 사실상 서로 다른 객체로 취급될 수도 있음.
        // 그래서 “순서를 무시하는 hash”가 필요했고, 둘의 해시코드를 곱하는 식으로 생성했다.

        return Objects.hash(startPoint.hashCode() * endPoint.hashCode()); // 결과적으로 A->B와 B->A가 해시코드가 같게 됨!!
        // Objects.hash() 사용: 단순 곱셈 결과 하나만 쓰는 것보다 자바 표준 유틸이 제공하는 해시 혼합 로직을 적용해 조금 더 분산된 해시 값을 얻으려는 시도

        // [다른 방법]
        // return startPoint.hashCode() ^ endPoint.hashCode(); // 이렇게도 사용한다. (XOR: 배타적 논리합)
        // return Objects.hash(Math.min(h1, h2), Math.max(h1,h2));
    }

    public Point getEndPoint() {
        return endPoint;
    }
}

@FunctionalInterface
interface RoadMoveStrategy {
    // 현재 위치 p에서 이동했을 때의 Road를 반환 (이동 불가능 하면 Optional.empty 반환)
    Optional<Road> command(Point currPoint);
}

enum RoadMover implements RoadMoveStrategy {
    U(currPoint -> Optional.ofNullable(Road.of(currPoint, new Point(currPoint.getX(), currPoint.getY() + 1)))),
    D(currPoint -> Optional.ofNullable(Road.of(currPoint, new Point(currPoint.getX(), currPoint.getY() - 1)))),
    R(currPoint -> Optional.ofNullable(Road.of(currPoint, new Point(currPoint.getX() + 1, currPoint.getY())))),
    L(currPoint -> Optional.ofNullable(Road.of(currPoint, new Point(currPoint.getX() - 1, currPoint.getY()))));

    private RoadMoveStrategy roadMoveStrategy;

    RoadMover(RoadMoveStrategy roadMoveStrategy) {
        this.roadMoveStrategy = roadMoveStrategy;
    }

    @Override
    public Optional<Road> command(Point currPoint) {
        return roadMoveStrategy.command(currPoint);
    }
}

class Point {
    private static final int X_MAX = 5;
    private static final int X_MIN = -5;
    private static final int Y_MAX = 5;
    private static final int Y_MIN = -5;

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInBoundary() {
        return x <= X_MAX && x >= X_MIN && y <= Y_MAX && y >= Y_MIN;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    // 만약 같은 좌표를 매번 새 new Point(0, 0)로 만들기 시작하면 좌표값은 같아도 다른 객체라 equals가 false가 될 수 있음.
    // 좌표값이 같으면 같은 점으로 보도록 재정의.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}