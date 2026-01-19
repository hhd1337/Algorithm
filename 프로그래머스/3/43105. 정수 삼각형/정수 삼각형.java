// [7]
// [3, 8]
// [8, 1, 0]
// [2, 7, 4, 4]
// [4, 5, 2, 6, 5]

import java.util.*;

class Solution {
    public int solution(int[][] triangle) {
        int height = triangle.length;
        // 해당 칸까지의 최대합을 저장하는 삼각형
        int[][] dp_triangle = triangle.clone();
        
        // 자기 자리의 바로 위칸(첫 인덱스만-1)과 왼쪽 위칸(두 인덱스 모두 -1) 중 더 큰값과 자신의 값을 더해서 저장
        for(int y = 0; y<dp_triangle.length; y++){
            for(int x = 0; x<dp_triangle[y].length; x++){
                dp_triangle[y][x] = getMaxSumIncludingMe(dp_triangle, y, x);
            }
        }
        // 마지막 열 중 max값 반환
        return Arrays.stream(dp_triangle[dp_triangle.length-1]).max().getAsInt();
    }
    
    private Integer getMaxSumIncludingMe(int[][] dp_triangle, int y, int x){
        Integer up = getUpOrNull(dp_triangle, y, x);
        Integer upLeft = getUpLeftOrNull(dp_triangle, y, x);
        int me = dp_triangle[y][x];
        
        if(up == null && upLeft == null){
            return me;
        }
        if(up == null && upLeft != null){
            return upLeft+me;
        }
        if(up != null && upLeft == null){
            return up+me;
        }
        
        if(up >= upLeft){
            return up+me;
        }
        return upLeft+me;
        
    }
    
    private Integer getUpOrNull(int[][] dp_triangle, int y, int x){
        try{
            return dp_triangle[y-1][x];
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
    private Integer getUpLeftOrNull(int[][] dp_triangle, int y, int x){
        try{
            return dp_triangle[y-1][x-1];
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
}