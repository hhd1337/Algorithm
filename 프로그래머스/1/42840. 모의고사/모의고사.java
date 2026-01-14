import java.util.*;

class Solution {
    public int[] solution(int[] answers) {
        int[] student1Answers = {1,2,3,4,5};
        int[] student2Answers = {2,1,2,3,2,4,2,5};
        int[] student3Answers = {3,3,1,1,2,2,4,4,5,5};
        
        int[] scores = new int[3];
        
        for(int i=0; i<answers.length; i++){
            if(student1Answers[i%student1Answers.length] == answers[i]){
                scores[0]++;
            }
            if(student2Answers[i%student2Answers.length] == answers[i]){
                scores[1]++;
            }
            if(student3Answers[i%student3Answers.length] == answers[i]){
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