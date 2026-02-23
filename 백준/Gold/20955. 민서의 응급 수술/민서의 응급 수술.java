/*
[구현 아이디어]
1. 먼저 사이클을 이루고 있는 뉴런 그룹(트리)을 찾아 사이클을 끊는 연산
   -> 정점이 n개인 그룹(그래프)이 트리가 되려면 간선이 정확히 n-1이어야 함.
   -> 따라서 한 그래프의 사이클을 끊는 최소 삭제 횟수는 (현재간선 수 - (n-1)) 이다.
   -> G개 그룹이 있다고 할 때, 각 그룹을 사이클이 없는 트리로 만들기 위한 간선 수는
      n1-1, n2-1, n3-1, ... nG-1 이 된다. 
      즉 모든 그룹을 트리로 만들었을때 총 간선 수는 N-G가 된다. 
      따라서 현재 총 간선수가 M이니까 사이클을 끊는 최소 삭제 횟수는 M-(N-G)이다.
2. 뉴런 그룹(트리)을 모두 이으면 되니까 총 그룹 수(트리 수) -1 의 잇는 연산 필요.
   -> G-1개의 연산이 발생한다. 
   
-> 따라서 M-(N-G) + G-1, 즉 M-N+2G-1 이 총 최소연산 횟수가 된다.
G만 구하면 된다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static class DSU {
        int[] parent; // parent[x] = x의 부모(루트면 자기 자신)

        DSU(int n) {
            parent = new int[n + 1];
            for (int i = 1; i <= n; i++) parent[i] = i;
        }

        int find(int x) {                     // x가 속한 집합의 루트 찾기
            if (parent[x] == x) return x;     // 루트면 반환
            parent[x] = find(parent[x]);      // 경로 압축
            return parent[x];                 // 압축된 루트 반환
        }

        void union(int a, int b) {            // 기준 없이 합치기
            int aRoot = find(a);              // a의 루트
            int bRoot = find(b);              // b의 루트
            if (aRoot == bRoot) return;       // 이미 같은 집합이면 종료

            parent[bRoot] = aRoot;            // 그냥 bRoot를 aRoot 밑에 붙임
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // 총 뉴런 수
        int M = Integer.parseInt(st.nextToken()); // 총 시냅스 수

        DSU dsu = new DSU(N); // N개 정점으로 DSU 초기화

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int node1 = Integer.parseInt(st.nextToken());
            int node2 = Integer.parseInt(st.nextToken());
            dsu.union(node1, node2); // node1과 node2가 속한 그룹을 합침
        }

        boolean[] isRootCounted = new boolean[N + 1];
        int G = 0; // 그룹 개수

        // 모든 노드 순회하면서, 해당 노드의 루트를 찾음. 루트 수 만큼 그룹이 있으니 G++
        for (int node = 1; node <= N; node++) {
            int root = dsu.find(node);
            if (!isRootCounted[root]) {
                isRootCounted[root] = true; // 루트 카운트 표시
                G++;
            }
        }

        int minOperationCount = M - N + (2 * G) - 1;
        System.out.println(minOperationCount);
    }
}