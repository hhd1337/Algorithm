/* 
[구현 아이디어]
land 배열을 그대로 dp배열로 쓰면서, 현재 칸에 도달할 수 있는 max값을 항상 저장.
*/

import java.util.*;

class Solution {
    
    public int solution(int[][] land) {
        for(int row=1; row<land.length; row++){
            for(int i=0; i<4; i++){
                land[row][i] += getMaxValue(land[row-1][(i + 1) % 4], 
                                            land[row-1][(i + 2) % 4], 
                                            land[row-1][(i + 3) % 4]);
            }
        }
        
        return getMaxValue(land[land.length-1][0], 
                           land[land.length-1][1], 
                           land[land.length-1][2], 
                           land[land.length-1][3]);
    }
    
    private int getMaxValue(int... values) {
        int max = values[0];
        
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }
}