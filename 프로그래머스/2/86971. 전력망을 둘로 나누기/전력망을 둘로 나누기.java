import java.util.*;

class Solution {
    private static List<List<Integer>> tree = new ArrayList<>();
    private static int totalNodeCount;
    
    public int solution(int n, int[][] wires) {
        initTree(n, wires);
        
        int minDiff = totalNodeCount;
        
        for(int[] wire : wires){
            int node1 = wire[0];
            int node2 = wire[1];
            
            int oneSideNodeCount = countNodesFrom(node1, node2);
            int thisDiff = Math.abs(n - 2 * oneSideNodeCount);
            
            minDiff = Math.min(minDiff, thisDiff);
        }
        return minDiff;
    }
    
    private int countNodesFrom(int node1, int node2){
        boolean[] visited = new boolean[totalNodeCount+1]; // 노드번호를 인덱스 그대로 쓸거임
        Queue<Integer> bfsQueue = new ArrayDeque<>();
        
        visited[node1] = true;
        bfsQueue.add(node1);
        
        int nodeCount = 1; // 노드1번 포함
        
        while(!bfsQueue.isEmpty()){
            int currNode = bfsQueue.poll();
            List<Integer> nextNodeList = tree.get(currNode); // currNode와 바로 연결된 노드번호List
            
            for(int nextNode : nextNodeList){ 
                if((currNode == node1 && nextNode == node2) || (currNode == node2 && nextNode == node1)){
                    continue;
                }
                if(visited[nextNode]){
                    continue;
                }
                visited[nextNode] = true;
                bfsQueue.add(nextNode);
                nodeCount++;
            }
        }
        return nodeCount;
    }
    
    private void initTree(int n, int[][] wires){
        totalNodeCount = n;
        // 빈 리스트 그래프 생성
        for(int i=0; i<=totalNodeCount; i++){
            tree.add(new ArrayList<>());
        }
        // 간선 추가-> 각 노드 입장에서 연결된 인접 노드들을 싹 추가함.
        for(int[] wire : wires){
            int node1 = wire[0];
            int node2 = wire[1];
            tree.get(node1).add(node2);
            tree.get(node2).add(node1);
        }
    }
}