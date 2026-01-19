import java.util.*;

class Solution {
    public int solution(int[][] triangle) {
        // 두번째 행부터 시작
        for(int i=1; i < triangle.length; i++){
            // 각 행의 양 끝을 먼저 처리
            triangle[i][0] += triangle[i-1][0];
            triangle[i][i] += triangle[i-1][i-1];
            // 남은애들 처리
            for(int j=1; j<i; j++){
                triangle[i][j] += Math.max(triangle[i-1][j], triangle[i-1][j-1]);
            }
        }
        // 마지막 행에서 최댓값 반환
        return Arrays.stream(triangle[triangle.length-1]).max().getAsInt();
    }
}
