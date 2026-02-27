/*
퍼즐1: N1 * M1
퍼즐2: N2 * M2

액자 가격 = N * M

- 퍼즐1 4회전, 퍼즐2 4회전 (총 16가지 회전 조합)
- 각 회전 조합에 대해 퍼즐2를 (dx, dy)로 이동시키는 모든 경우 탐색
- 겹침 없으면 넓이 계산 후 최소넓이 갱신

*/

import java.io.*;
import java.util.*;

public class Main {
    private static int N1, M1, N2, M2;
    private static int[][] puzzle1, puzzle2;
    private static int minArea = Integer.MAX_VALUE;
        
    public static void main(String[] args) throws Exception {
        initPuzzles();
        
        for(int i=0; i<4; i++){ // 퍼즐1을 4번 회전해가며 확인
            for(int j=0; j<4; j++){ // 퍼즐2도 4번 회전해가며 확인
                tryAllShifts(puzzle1, puzzle2);
                puzzle2 = rotate90(puzzle2);
            }
            puzzle1 = rotate90(puzzle1);
        }
        
        System.out.println(minArea);
    }
    
    // 퍼즐1은 고정, 퍼즐2를 옮겨보며 가능한 배치 전부 검사
    private static void tryAllShifts(int[][] p1, int[][] p2){
        int h1 = p1.length, w1 = p1[0].length;
        int h2 = p2.length, w2 = p2[0].length;
        
        for(int dy = -h2; dy<= h1; dy++){
            for (int dx = -w2; dx <= w1; dx++) {
                // 현재 (dx,dy)위치에서 p1의 1과 p2의 1이 겹치면, 다음 배치로 넘어감
                if(isOverlap(p1,p2,dx,dy)){ 
                    continue;
                }
                int thisArea = calcArea(h1,w1,h2,w2,dx,dy);
                if(thisArea < minArea){
                    minArea = thisArea;
                }
            }
        }
    }
    
    // p1고정, p2를 (dx, dy)만큼 이동했을 때 겹침이 있는지 검사
    private static boolean isOverlap(int[][] p1, int[][] p2, int dx, int dy){
        int h1 = p1.length;
        int w1 = p1[0].length;
        int h2 = p2.length;
        int w2 = p2[0].length;
        
        for(int y=0; y<h2; y++){ // p2의 모든 칸 순회
            for(int x=0; x<w2; x++){
                if(p2[y][x] == 0){ // p2의 이 칸이 0이면 무조건 안겹침
                    continue;
                }
                int movedX = x + dx;
                int movedY = y + dy; // 이동시킴
                
                if(0 <= movedX && movedX < w1 && 0 <= movedY && movedY < h1){ // indexOutOfBound 검사
                    if(p1[movedY][movedX] == 1){
                        return true; // p2도 1이고 p1도 1임 -> 겹침
                    }
                }
            }
        }
        
        return false;
    }
    
    private static int calcArea(int h1,int w1,int h2,int w2,int dx,int dy){
        int minX = Math.min(0, dx);
        int maxX = Math.max(w1-1, w2-1+dx);
        int minY = Math.min(0, dy);
        int maxY = Math.max(h1-1, h2-1+dy);
        
        int widthResult = maxX - minX + 1;
        int heightResult = maxY - minY + 1;
        
        return widthResult * heightResult;
    }
    
    private static int[][] rotate90(int[][] arr){
        int originH = arr.length;
        int originW = arr[0].length;
        
        int[][] rotated = new int[originW][originH];
        
        for(int i=0; i< originH; i++){
            for(int j=0; j<originW; j++){
                rotated[j][originH - 1 - i] = arr[i][j];
            }
        }
        
        return rotated;
    }
    
    private static void initPuzzles() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N1 = Integer.parseInt(st.nextToken());
        M1 = Integer.parseInt(st.nextToken());
        //System.out.println("N1: " + N1);
        //System.out.println("M1: " + M1);
        
        puzzle1 = new int[N1][M1];
        
        for(int i=0; i<N1; i++){
            st = new StringTokenizer(br.readLine());
            String numberLine = st.nextToken();
            //System.out.println(i + "번째 줄: " + numberLine);
            for(int j=0; j<M1; j++){
                puzzle1[i][j] = numberLine.charAt(j);
            }
        }
        
        //System.out.println(puzzle1.toString());
        
        st = new StringTokenizer(br.readLine());
        N2 = Integer.parseInt(st.nextToken());
        M2 = Integer.parseInt(st.nextToken());
        
        puzzle2 = new int[N2][M2];
        
        for(int i=0; i<N2; i++){
            st = new StringTokenizer(br.readLine());
            String numberLine = st.nextToken();
            //System.out.println(i + "번째 줄: " + numberLine);
            for(int j=0; j<M2; j++){
                puzzle2[i][j] = numberLine.charAt(j);
            }
        }
    }
}