import java.util.*;

class Solution {
    private static final int[] STUDENT1_PATTERN = {1, 2, 3, 4, 5};
    private static final int[] STUDENT2_PATTERN = {2, 1, 2, 3, 2, 4, 2, 5};
    private static final int[] STUDENT3_PATTERN = {3, 3, 1, 1, 2, 2, 4, 4, 5, 5};

    
    public int[] solution(int[] answers) {
        int[] scores = new int[3];
        
        for(int i=0; i<answers.length; i++){
            if(STUDENT1_PATTERN[i%STUDENT1_PATTERN.length] == answers[i]){
                scores[0]++;
            }
            if(STUDENT2_PATTERN[i%STUDENT2_PATTERN.length] == answers[i]){
                scores[1]++;
            }
            if(STUDENT3_PATTERN[i%STUDENT3_PATTERN.length] == answers[i]){
                scores[2]++;
            }
        }
        
        
        int max = Arrays.stream(scores)
                        .max()
                        .getAsInt();
        
        List<Integer> result = new ArrayList<>();
        
        if(scores[0] == max){
            result.add(1);
        }
        if(scores[1] == max){
            result.add(2);
        }
        if(scores[2] == max){
            result.add(3);
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}