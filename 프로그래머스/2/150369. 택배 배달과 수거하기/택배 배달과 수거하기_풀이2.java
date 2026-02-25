/*
[아이디어 - 누적 미처리량 + 거리별 운행 횟수 계산]
- 뒤에서부터 배달/수거 남은 총량(deliver, pickup)을 누적한다.
- 어떤 거리 i+1에서 deliver>0 || pickup>0이면 그 거리까지 왕복 운행이 추가로 필요하므로,
  운행 1번마다 deliver-=cap, pickup-=cap 하고, 거리 2*(i+1)을 더한다(음수여도 괜찮음. 음수에다가 다시 다음집 물량 더하면 됨)
- i가 0까지 왔는데 deliver/pickup이 음수면 -> 이미 다 처리하고도 처리 용량이 남아돈다는 뜻.
*/

public class Solution {
    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        long answer = 0;

        int deliver = 0, pickup = 0;

        for (int i = n - 1; i >= 0; i--) {
            deliver += deliveries[i]; // i번째 집의 배달 물량을 '미처리 배달량'에 누적한다. i번째 집까지 포함했을 때 남아있는 배달 총량이 deliver
            pickup += pickups[i]; // i번째 집의 수거 물량을 '미처리 수거량'에 누적한다.

            // i번째 집까지 왕복운행이 '추가로 몇번' 필요한지 계산하는 루프이다.
            // deliver 또는 pickup 중 하나라도 양수면, 아직 i번째 집 이상(=거리)
            // 둘다 음수나 0이면, while문 안돌고 i만 계속 감소 -> 거리도 안더하고 건너뛴다.
            while (deliver > 0 || pickup > 0) {
                deliver -= cap;
                pickup -= cap; // 음수여도 괜찮음. 음수에다가 다시 다음집 물량 더하면 됨.

                answer += ((i + 1) * 2); // 현재 거리의
            }
        }

        return answer;
    }
}