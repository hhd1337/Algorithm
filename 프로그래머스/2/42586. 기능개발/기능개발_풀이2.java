import java.util.*;

class Solution {
    public int[] solution(int[] progresses, int[] speeds) {
        int[] needDays = new int[progresses.length];
        // 각 기능별 필요한 일 수 계산
        for(int i=0; i< progresses.length; i++){
            int dayCount = 0;

            while(progresses[i] < 100){
                progresses[i] += speeds[i];
                dayCount++;
            }
            needDays[i] = dayCount;
        }
        return getDeployCounts(needDays);
    }

    private int[] getDeployCounts(int[] needDays) {
        List<Integer> deployCounts = new ArrayList<>();
        int currMaxNeedDays = needDays[0]; // 현재 배포 그룹의 기준일

        int count = 1; // 현재 그룹에 포함된 기능 개수(기준기능 포함)
        for (int i = 1; i < needDays.length; i++) {
            // 뒤로 한칸씩 가면서 기준기능보다 더 빨리 or 같은날 끝나는 날들 다 count
            if (needDays[i] <= currMaxNeedDays) {
                count++;
            } else { // 더 늦게 끝나는 날이 있으면 멈추고 다음그룹 준비
                deployCounts.add(count);
                currMaxNeedDays = needDays[i];
                count = 1;
            }
        }
        // 마지막 그룹 추가
        deployCounts.add(count);

        return deployCounts.stream().mapToInt(Integer::intValue).toArray();
    }
}