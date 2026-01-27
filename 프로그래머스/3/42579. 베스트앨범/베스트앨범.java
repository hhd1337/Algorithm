// [구현 아이디어]
// 1. genres와 plays를 인덱스로 돌면서 장르별 노래 리스트와 장르의 총 재생횟수 저장
// 2. 총 재생횟수가 많은 Genre부터, 모든 장르 돌면서
//    1) 해당장르의 노래들 조건에 맞게 정렬
//    2) 앞에서부터 노래 두개 고유id를 List<Integer> 에 담음. (만약 노래가 하나밖에 없으면 하나만 담음)
// 3. List<Integer> 를 int[]로 변환해서 반환

import java.util.*;

class Solution {
    public int[] solution(String[] genres, int[] plays) {
        Map<String, List<Song>> songsByGenre = new HashMap<>(); // 장르별 Song 리스트 저장
        Map<String, Integer> totalCountsByGenre = new HashMap<>(); // 장르의 총 재생횟수 저장
        
        for(int i=0; i<genres.length; i++){
            // 장르별 Song 리스트 업데이트
            List<Song> list = songsByGenre.getOrDefault(genres[i], new ArrayList<>());
            list.add(new Song(i, plays[i]));
            songsByGenre.put(genres[i], list);
            // 장르의 총 개수 누적
            totalCountsByGenre.put(genres[i], totalCountsByGenre.getOrDefault(genres[i], 0) + plays[i]);
        }
        
        List<Integer> songIds = new ArrayList<>();
        
        while(songsByGenre.size() > 0){
            // 현재 max 장르 알아냄
            String maxGenre = "";
            int maxCount = 0;

            for(Map.Entry<String, Integer> entry : totalCountsByGenre.entrySet()){
                if(entry.getValue() > maxCount){
                    maxGenre = entry.getKey();
                    maxCount = entry.getValue();
                }
            }
            
            // 이 장르의 노래들을 playedCount 내림차순 (같으면 id 오름차순) 으로 정렬
            List<Song> songList= songsByGenre.get(maxGenre);
            songList.sort(
                Comparator.comparingInt((Song s) -> s.playedCount).reversed() // 내림차순
                          .thenComparingInt(s -> s.id)
            );
            
            // 정렬된 리스트에서 노래 두곡 찾아 id를 songIds.add() 함.
            songIds.add(songList.get(0).id);
            if(songList.size() > 1){
                songIds.add(songList.get(1).id);
            }
            
            // 두 맵에서 이 장르 삭제
            songsByGenre.remove(maxGenre);
            totalCountsByGenre.remove(maxGenre);
        }
        
        
        
        return songIds.stream().mapToInt(Integer::intValue).toArray();
    }
}

class Song {
    int id;
    int playedCount;
    
    public Song(int id, int playedCount){
        this.id = id;
        this.playedCount = playedCount;
    }
}