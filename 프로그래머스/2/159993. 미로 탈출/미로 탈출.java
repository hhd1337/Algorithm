/*
전형적인 BFS(그래프 완전탐색, 너비우선), 최단거리, 격자 문제
*/

import java.util.*;

class Solution {
    static int R, C;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public int solution(String[] maps) {
        R = maps.length;
        C = maps[0].length();

        int[] S = findCorOrNull(maps, 'S');
        int[] L = findCorOrNull(maps, 'L');
        int[] E = findCorOrNull(maps, 'E');

        int distSL = bfs(maps, S[0], S[1], 'L');
        if (distSL == -1) {
            return -1;
        }
        
        int distLE = bfs(maps, L[0], L[1], 'E');
        if (distLE == -1) {
            return -1;
        }
        
        return distSL + distLE;
    }

    // start (sr, sc)에서 목적지(destChar)까지 최단거리(BFS). 못 가면 -1.
    private int bfs(String[] maps, int sr, int sc, char destChar) {
        // 최단거리 BFS에서는 boolean[][] visited 가 아니라 int[][] dist 를 사용해서 방문 안했으면 -1, 방문했으면 0...최단거리 저장
        int[][] dist = new int[R][C];  
        for (int i = 0; i < R; i++) { // dist를 전부 -1로 초기화
            Arrays.fill(dist[i], -1);
        }
        
        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sr, sc});
        dist[sr][sc] = 0; // 최단거리 bfs에서는 시작점을 0으로 초기화.

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            if (maps[r].charAt(c) == destChar) { // 현재 칸이 목적지이면 현재 칸까지의 최단거리 반환
                return dist[r][c];
            }

            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                if (nr < 0 || nr >= R || nc < 0 || nc >= C) continue; // 1. 범위검사
                if (maps[nr].charAt(nc) == 'X') continue; // 2. 벽 검사
                if (dist[nr][nc] != -1) continue; // 3. 방문검사

                dist[nr][nc] = dist[r][c] + 1; // visited[][] = true; 대신 다음칸까지의 최단거리를 현재+1로 기록. 격자그래프에서는 최단거리가 정해져 있고, 다음칸 = 현재칸+1 로 계산하면 맞음.
                q.add(new int[]{nr, nc});
            }
        }
        return -1;
    }

    private int[] findCorOrNull(String[] maps, char ch) {
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length(); j++) {
                if (maps[i].charAt(j) == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}