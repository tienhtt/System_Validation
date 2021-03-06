package allaboutecm.mining;


import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class ECMMusicianMiner {
    public static final java.lang.String K_ERROR_MSG = "k cannot be less than 1";
    private static Logger logger = LoggerFactory.getLogger(ECMMusicianMiner.class);
    private final DAO dao;

    public ECMMusicianMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param int k the number of musicians to be returned.
     * @Param int startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        validateArgument(k < 1, K_ERROR_MSG);
        validateArgument(startYear > endYear && startYear > 0 && endYear > 0,
                            "Start Year cannot  be greater than End Year");
        Collection<Musician> musicians = dao.loadAll(Musician.class);

        if (musicians.isEmpty())
            return Lists.newArrayList();

        Map<Musician, Integer> musicianAndNoOfAlbumsMap = new HashMap<>();
        musicians.forEach(musician ->{
            List<Album> filteredAlbum =  musician.getAlbums()
                    .stream()
                    .filter(album -> !((startYear > 0 && album.getReleaseYear() < startYear)
                            || (endYear > 0 && album.getReleaseYear() > endYear)
                            ||(endYear == 0 && startYear == 0)))
                    .collect(Collectors.toList());

            if (!filteredAlbum.isEmpty())
                musicianAndNoOfAlbumsMap.put(musician, filteredAlbum.size());
        });

        Map<Musician, Integer> sortedByNoOfAlbumsMap = sortMapByValue(musicianAndNoOfAlbumsMap);
        //https://www.leveluplunch.com/java/examples/limit-or-take-first-elements-from-list/
        return sortedByNoOfAlbumsMap.keySet().stream().limit(k).collect(Collectors.toList());
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        if(musicianInstruments.isEmpty())
            return Lists.newArrayList();

        Map<Musician, Integer> musicianAndNoOfInstrumentMap = new HashMap<>();
        musicianInstruments.forEach(musicianInstrument -> {
                    if (musicianInstrument.getMusicalInstruments() != null
                        && !musicianInstrument.getMusicalInstruments().isEmpty()) {
                            musicianAndNoOfInstrumentMap.put(musicianInstrument.getMusician(),
                                    musicianInstrument.getMusicalInstruments().size());
                    }

                }
        );

        Map<Musician, Integer> sortedByNoOfMusicalsMap = sortMapByValue(musicianAndNoOfInstrumentMap);
        return sortedByNoOfMusicalsMap.keySet().stream().limit(k).collect(Collectors.toList());
    }

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<Album> albums = dao.loadAll(Album.class);
        if(albums.isEmpty())
            return Lists.newArrayList();

        Map<Musician, Integer> musicianAndNoOfCollaborationMap = new HashMap<>();
        albums.forEach(album ->{
            if (album.getFeaturedMusicians() != null){
                List<Musician> featuredMusicians = album.getFeaturedMusicians().stream().collect(Collectors.toList());
                featuredMusicians.forEach(musician -> {
                    if(musicianAndNoOfCollaborationMap.containsKey(musician)){
                        int collaborationCount = musicianAndNoOfCollaborationMap.get(musician);
                        musicianAndNoOfCollaborationMap.replace(musician, collaborationCount, collaborationCount+1);
                    }else{
                        musicianAndNoOfCollaborationMap.put(musician, 1);
                    }
                });
            }
        });

        Map<Musician, Integer> sortedByNoOfCollaborationMap = sortMapByValue(musicianAndNoOfCollaborationMap);
        return sortedByNoOfCollaborationMap.keySet().stream().limit(k).collect(Collectors.toList());
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */

    public List<Integer> busiestYears(int k) {
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<Album> albums = dao.loadAll(Album.class);
        if(albums.isEmpty())
            return Lists.newArrayList();

        Map<Integer,Integer> yearsMap = new HashMap<>();
        albums.forEach(album -> {
            int releasedYear = album.getReleaseYear();
            if (yearsMap.containsKey(releasedYear)){
                int albumCount = yearsMap.get(releasedYear);
                yearsMap.replace(releasedYear, albumCount, albumCount+1);
            }else if (releasedYear != 0){
                yearsMap.put(releasedYear,1);
            }
        });
        Map<Integer, Integer> sortedByNoOfAlbumMap = sortMapByValue(yearsMap);
        return sortedByNoOfAlbumMap.keySet().stream().limit(k).collect(Collectors.toList());
    }


    private void validateArgument(boolean condition, String msg) {
        if (condition) {
            logger.error("An IllegalArgumentException occurred.");
            throw new IllegalArgumentException(msg);
        }
    }

    private static <K,V extends Comparable<? super V>> LinkedHashMap<K, V> sortMapByValue(Map<K, V> map) {
        //reference:
        // https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
        //sort descending order by values
        return map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.<K, V>comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

}