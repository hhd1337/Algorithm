/*
A배열: 3*3
-R연산: 
-C연산: 
*/

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        
        int[][] A = new int[3][3];
        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 3; j++) {
                A[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        int time = 0;
        while (time <= 100) {
            if (r-1 < A.length && c-1 < A[0].length && A[r-1][c-1] == k) {
                System.out.println(time);
                return;
            }
            if (time == 100){
                break;
            }
        
            if (A.length >= A[0].length) {
                A = r(A);
            } else {
                A = c(A);
            }
            
            time++;
        }
        System.out.println(-1);

    }
    
    private static int[] sortLineWithTreeMap(int[] line){
        // num -> cnt (num 오름차순)
        TreeMap<Integer, Integer> countMap = new TreeMap<>();
        for (int value : line){
            if (value == 0){
                continue;
            }
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }

        // cnt -> nums (cnt 오름차순)
        TreeMap<Integer, List<Integer>> countToNums = new TreeMap<>();
        for(Map.Entry<Integer, Integer> e : countMap.entrySet()){
            int num = e.getKey();
            int cnt = e.getValue();
            if (!countToNums.containsKey(cnt)) {
                countToNums.put(cnt, new ArrayList<>());
            }
            countToNums.get(cnt).add(num);
        }

        // 펼침
        int[] res = new int[100]; // 최대 100개임
        int idx = 0;

        for(Map.Entry<Integer, List<Integer>> e : countToNums.entrySet()){
            int cnt = e.getKey();
            List<Integer> nums = e.getValue();

            for(int num : nums){
                if (idx >= 100){
                    break;
                }
                res[idx++] = num;
                
                if (idx >= 100){
                    break;
                }
                res[idx++] = cnt;
            }
            
            if (idx >= 100){
                break;
            }
        }

        return Arrays.copyOf(res, idx); // 실제 길이만 반환
    }
    
    private static int[][] r(int[][] A){
        int row = A.length, col = A[0].length;
    
        int[][] rows = new int[row][];
        int maxLength = 0;
    
        for(int i = 0; i < row; i++){
            rows[i] = sortLineWithTreeMap(A[i]);
            maxLength = Math.max(maxLength, rows[i].length);
        }
        maxLength = Math.min(100, Math.max(1, maxLength));
    
        int[][] next = new int[row][maxLength];
        for(int i = 0; i < row; i++){
            int copyLen = Math.min(maxLength, rows[i].length);
            System.arraycopy(rows[i], 0, next[i], 0, copyLen);
        }
        return next;
    }

    private static int[][] c(int[][] A){
        int row = A.length, col = A[0].length;

        int[][] cols = new int[col][];
        int maxLength = 0;

        for(int j = 0; j < col; j++){
            int[] column = new int[row];
            for (int i = 0; i < row; i++){
                column[i] = A[i][j];
            }
            cols[j] = sortLineWithTreeMap(column);
            maxLength = Math.max(maxLength, cols[j].length);
        }
        maxLength = Math.min(100, Math.max(1, maxLength));

        int[][] next = new int[maxLength][col];
        for(int j = 0; j < col; j++){
            int copyLen = Math.min(maxLength, cols[j].length);
            for (int i = 0; i < copyLen; i++){
                next[i][j] = cols[j][i];
            }
        }
        return next;
    }
}