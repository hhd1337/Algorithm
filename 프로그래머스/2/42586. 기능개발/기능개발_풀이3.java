import java.util.Arrays;

class Solution {
    public int[] solution(int[] progresses, int[] speeds) {
        int[] dayOfEnd = new int[100]; // (progress < 100 이고 speed는 1이상인 자연수) progress가 0이어도 100일 이상 걸리는 기능개발작업은 없다.
        int day = -1;
        // 각 기능마다 while에서 day를 0부터 다시 세는 것이 아니라, 앞에서 사용한 day를 그대로 이어서 사용.
        for (int i = 0; i < progresses.length; i++) {
            while (progresses[i] + (day * speeds[i]) < 100) {
                day++;
            }
            dayOfEnd[day]++;
        }
        return Arrays.stream(dayOfEnd).filter(i -> i != 0).toArray(); // 0 제거하고 반환
    }
}