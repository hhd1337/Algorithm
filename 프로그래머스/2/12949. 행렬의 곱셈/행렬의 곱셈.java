import java.util.*;

class Solution {
    public int[][] solution(int[][] arr1, int[][] arr2) {
        int answerHorizonCount = arr1.length;
        int answerVerticalCount = arr2[0].length;
            
        int[][] answer = new int[answerHorizonCount][answerVerticalCount];
        
        // answer[i][j]를 계산 및 채움.
        for(int i = 0; i < answerHorizonCount; i++){
            for(int j = 0; j < answerVerticalCount; j++){
                answer[i][j] = calculateElement(arr1[i], makeVerticalLineArrayByIndex(arr2, j));
            }
        }
        
        return answer;
    }
    
    private int[] makeVerticalLineArrayByIndex(int[][] arr, int index){
        int[] result = new int[arr.length];
        
        for(int i = 0; i<arr.length; i++){
            result[i] = arr[i][index];
        }
        return result;
    }
    
    private int calculateElement(int[] arr1, int[] arr2){
        int[] resultArray = new int[arr1.length];
        
        for(int i=0; i<arr1.length; i++){
            resultArray[i] = arr1[i] * arr2[i];
        }
        
        return Arrays.stream(resultArray).sum();
    }
}