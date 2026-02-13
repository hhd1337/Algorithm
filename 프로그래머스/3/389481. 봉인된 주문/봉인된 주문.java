/*
- 주문서에는 마법세계의 모든 주문이 (길이 1~11) 규칙대로 정렬되어 있음
- 짧은 길이 우선, 길이 같으면 사전순
[구현 아이디어]
1. 

*/

import java.util.*;

class Solution {
    private static int MAX_LENGTH = 11; // 주문의 최대 자릿수(길이)
    private long[] spellCountUpToLength = new long[MAX_LENGTH+1]; // 자릿수별 인덱스 최댓값
    private long[] banIdx;
    
    public String solution(long n, String[] bans) {    
        fillSpellCountUpToLength();
        
        // bans를 index로 변환하고, 정렬
        banIdx = new long[bans.length];
        for (int i = 0; i < bans.length; i++) {
            banIdx[i] = spellToOriginalIndex(bans[i]);
        }
        Arrays.sort(banIdx);
        
        
        long totalAll = spellCountUpToLength[MAX_LENGTH];
        long lo = 1;
        long hi = n + bans.length;
        
        while (lo < hi){
            long mid = (lo + hi) / 2;
            long removed = countBansUpTo(mid);                 
            long aliveOrder = mid - removed; // 삭제된 주문서에서 mid 위치까지 살아남은 주문 개수

            if (aliveOrder >= n) {
                hi = mid;
            } else {
                lo = mid + 1;                                  
            }
        }

        
        return indexToSpell(lo);
    }
    
    private long spellToOriginalIndex(String spell) {
        long offset = spellCountUpToLength[spell.length() - 1];

        long rankInSameLength = 0;
        for (int i = 0; i < spell.length(); i++) {
            int digit = spell.charAt(i) - 'a';
            rankInSameLength = rankInSameLength * 26L + digit;
        }

        return offset + (rankInSameLength + 1);
    }

    
    private String indexToSpell(long idx) {
        // 길이 L 찾음
        int L = 1;
        while (L <= MAX_LENGTH && spellCountUpToLength[L] < idx){
            L++;
        }

        long offset = spellCountUpToLength[L - 1];
        long value = (idx - offset) - 1;

        char[] chars = new char[L];
        for (int i = L - 1; i >= 0; i--) {
            chars[i] = (char) ('a' + (value % 26));
            value /= 26;
        }
        
        return new String(chars);
    }
    
    private void fillSpellCountUpToLength(){
        long p = 1;
        for(int i = 1; i <= MAX_LENGTH; i++){
            p *= 26L;
            spellCountUpToLength[i] = spellCountUpToLength[i-1] + p;
        }
    }
    
    private int countBansUpTo(long originalIndex){
        int lo = 0;
        int hi = banIdx.length;
        
        while(lo < hi){
            int mid = (lo + hi) / 2; 
            
            if (banIdx[mid] <= originalIndex) {
                lo = mid + 1; // mid 값이 조건을 만족하므로 더 오른쪽에서 첫 초과값을 찾음
            } else {
                hi = mid; // mid 값이 originalIndex보다 크므로 왼쪽 구간으로 줄임
            }
        }
        return lo;
    }
}