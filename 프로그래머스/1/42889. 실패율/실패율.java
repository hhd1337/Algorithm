import java.util.*;

class Solution {
    public int[] solution(int N, int[] stagesPlayerStoped) {
        Map<Integer,Double> failRatesByStage = new HashMap<>();
        
        for(Integer i=0; i<N; i++){
            failRatesByStage.put(i+1 , calculateFailRate(stagesPlayerStoped, i+1));
        }
    
        return sortStageByFailureRateDescThenStageAsc(N, failRatesByStage);
    }
    
    // 실패율이 높은 스테이지부터 내림차순으로 배열 구성
    private int[] sortStageByFailureRateDescThenStageAsc(int N, Map<Integer,Double> failRatesByStage){
        List<Integer> sortedStages = new ArrayList<>();
        
        while(sortedStages.size() < N){
            Double currMaxFailRate = -1.0; // 0.0으로 하면 안됨! 이런경우 초기값은 확실하게 음수로 세팅할것! (테스트값중 [4,4,4,4,4] 가 있었는데 이러면 failRate이 0.0도 가능함. 1,2,3단계는 실패율이 0이됨.)
            Integer currStage = 0;
            for(Map.Entry<Integer, Double> entry : failRatesByStage.entrySet()){
                // sortedStages에 key가 없고 currMaxFailRate 보다 크면 currMaxFailRate과 currStage를 무조건 업데이트
                if(!sortedStages.contains(entry.getKey()) && entry.getValue() > currMaxFailRate){ // > 이기 때문에 stage오름차순 자동으로 지켜짐.
                    currMaxFailRate = entry.getValue();
                    currStage = entry.getKey();
                }
            }
            sortedStages.add(currStage);
        }
        
        return sortedStages.stream()
                        .mapToInt(Integer::intValue)
                        .toArray();
    }
    
    private double calculateFailRate(int[] stagesPlayerStoped, int stage){
        int notClearedPlayers = countNotClearedPlayers(stagesPlayerStoped, stage);
        int triedPlayers = countTriedPlayers(stagesPlayerStoped, stage);
        
        if(notClearedPlayers == 0 || triedPlayers == 0){
            return 0.0;
        }
        
        return notClearedPlayers / (double) triedPlayers;
    }
    
    private int countNotClearedPlayers(int[] stagesPlayerStoped, int stage){
        int count=0;
        
        for(int i=0; i<stagesPlayerStoped.length; i++){
            if(stagesPlayerStoped[i] == stage){
                count++;
            }
        }
        return count;
    }
    
    private int countTriedPlayers(int[] stagesPlayerStoped, int stage){
        int count=0;
        
        for(int i=0; i<stagesPlayerStoped.length; i++){
            if(stagesPlayerStoped[i] >= stage){
                count++;
            }
        }
        return count;
    }
}