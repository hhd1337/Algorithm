import java.util.*;

class Solution{
    public int solution(String s){
        int answer = -1;

        ArrayDeque<Character> stack = new ArrayDeque<>();
        
        for(char c : s.toCharArray()){
            if(stack.isEmpty()){
                stack.push(c);
                continue;
            } 
            if(stack.peek() == c){
                stack.pop();
                continue;
            }
            if(stack.peek() != c){
                stack.push(c);
            }
        }
        
        if(stack.isEmpty()){
            return 1;
        }
        return 0;
    }
}