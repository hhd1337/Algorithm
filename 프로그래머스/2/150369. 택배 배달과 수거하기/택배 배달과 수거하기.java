/*
어떻게 접근을 해야 할 지 몰랐음.

[핵심 아이디어]
1. 가장 먼 집 i에 배달/수거가 남아 있다면, 어떤 방법이든, 언젠가 그 집까지는 가야 함.
   그러면 "그 집까지 가는 김에 cap 만큼 가능한 한 많이 처리하고 오자" 가 항상 이득이다.
   (예시를 보고 힌트를 얻었어야 했다)
2. 어쨌든 배달/수거가 남아있는 가장 먼 집에 갔다가 오는 과정에서 cap만큼 배달, cap만큼 수거 하게 된다.
3. 배달, 수거를 각각 어디까지 했는지 저장하는 전역 포인터가 있으면 좋을 것 같음.

[구현]
마지막 집까지 가서 거기서부터 배달,수거를 cap 만큼 쭉 차감하고 나온다.
1. 배달을 먼저 가면서 하고(마지막 원소에서부터 앞으로 오면서 cap 만큼 배달개수 차감)
   즉 마지막 집에 도달했을 때 항상 트럭이 비도록 만듦.
2. 마지막에서 앞으로 오면서 순서대로 cap 만큼 수거 개수 차감
3. 이 왕복(roundTrip)의 거리인 (마지막집인덱스+1) * 2 를 계속 누적합함.
*/

public class Solution {
    private int dIndex;
    private int pIndex;
    private long minDistanceSum = 0;

    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        dIndex = n - 1;
        pIndex = n - 1;

        while (dIndex >= 0 || pIndex >= 0) {
            // 가장 먼저 배달, 수거가 0인 집은 모두 건너뛴다!!
            while (dIndex >= 0 && deliveries[dIndex] == 0) {
                dIndex--;
            }
            while (pIndex >= 0 && pickups[pIndex] == 0) {
                pIndex--;
            }

            int destination = Math.max(dIndex + 1, pIndex + 1);
            minDistanceSum += destination * 2L; // 이번 운행의 최종 목적지의 두배를 누적합 함.(왕복이니까)

            int remainDelCount = cap;
            int remainPickCount = cap;

            // 배달개수가 0이될 때 까지, 첫번째 집까지
            deliverThisRound(deliveries, remainDelCount);

            // 수거개수가 0 될때 까지, 첫번째 집까지
            pickUpThisRound(pickups, remainPickCount);

        }

        return minDistanceSum;
    }

    private void pickUpThisRound(int[] pickups, int remainPickCount) {
        while (remainPickCount > 0 && pIndex >= 0) {
            int actualPickupInThisHouse = Math.min(remainPickCount, pickups[pIndex]);

            pickups[pIndex] -= actualPickupInThisHouse;
            remainPickCount -= actualPickupInThisHouse;

            if (pickups[pIndex] == 0) { // 이 집 수거 끝났으면 앞집으로 이동
                pIndex--;
            }
        }
    }

    private void deliverThisRound(int[] deliveries, int remainDelCount) {
        while (remainDelCount > 0 && dIndex >= 0) {
            int actualDeliverInThisHouse = Math.min(remainDelCount, deliveries[dIndex]);

            deliveries[dIndex] -= actualDeliverInThisHouse;
            remainDelCount -= actualDeliverInThisHouse;

            if (deliveries[dIndex] == 0) {
                dIndex--;
            }
        }
    }

}

// lagacy 코드 - deliverThisRound()
//    private void deliverThisRound(int[] deliveries, int remainDelCount) {
//        while (remainDelCount > 0 && dIndex >= 0) {
//            // 이집에 배달해야할 개수가 remainDelCount보다 많으면 이집에서 remainDelCount 싹 소모하면 됨
//            if (deliveries[dIndex] > remainDelCount) { // 그래도 남았으니 이집은 나중애 한번 더와야함. 인덱스 놔둠
//                deliveries[dIndex] = deliveries[dIndex] - remainDelCount;
//                remainDelCount = 0;
//            } else if (deliveries[dIndex] == remainDelCount) { // 인덱스 하나 -- (다음 왕복시 이집부터 조사하도록)
//                deliveries[dIndex] = deliveries[dIndex] - remainDelCount;
//                remainDelCount = 0;
//                dIndex--;
//            } else { // 이집에 배달 다하고도 배달물량이 남았으면
//                remainDelCount -= deliveries[dIndex];
//                deliveries[dIndex] = 0;
//                dIndex--;
//            }
//        }
//    }