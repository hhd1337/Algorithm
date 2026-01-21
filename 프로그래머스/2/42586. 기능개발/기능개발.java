import java.util.*;

class Solution {
    public int[] solution(int[] progresses, int[] speeds) {
        Features features = makeFeatures(progresses, speeds);
        List<Integer> answer = new ArrayList<>();
        
        while(features.isLeft()){
            int todayDeployedCount = 0;
            features.developOneDay();
            todayDeployedCount = features.deploy();
            
            if(todayDeployedCount != 0){
                answer.add(todayDeployedCount);
            }
        }
        
        return answer.stream().mapToInt(Integer::intValue).toArray();
    }
    
    private Features makeFeatures(int[] progresses, int[] speeds){
        Queue<Feature> featureQueue = new ArrayDeque<>();
        
        for(int i = 0; i < progresses.length; i++){
            featureQueue.add(new Feature(progresses[i], speeds[i]));
        }
        return new Features(featureQueue);
    }
}

class Features {
    private Queue<Feature> featureQueue;
    private List<Feature> doneFeatureList;
    
    public Features(Queue<Feature> featureQueue){
        this.featureQueue = featureQueue;
    }
    
    public void developOneDay(){
        featureQueue.stream().forEach(feat -> feat.developOneDay());
    }
    
    public int deploy(){
        int count = 0;
        while (!featureQueue.isEmpty() && featureQueue.peek().isDone()) {
            featureQueue.poll();
            count++;
        }   
        return count;
    }
    
    public boolean isLeft(){
        return !featureQueue.isEmpty();
    }
}

class Feature {
    private int progress; // 100 미만 자연수
    private int speed; // 100 이하 자연수
    
    public Feature(int progress, int speed){
        this.progress = progress;
        this.speed = speed;
    }
    
    public void developOneDay(){
        progress += speed;
        
        if(progress>100){
            progress=100;
        }
    }
    
    public boolean isDone(){
        return progress == 100;
    }
}