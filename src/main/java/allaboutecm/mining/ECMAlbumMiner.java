package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicianGroup;
import allaboutecm.model.Rating;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.Validate.notNull;

public class ECMAlbumMiner {
    public static final java.lang.String K_ERROR_MSG = "k cannot be less than 1";
    private static Logger logger = LoggerFactory.getLogger(ECMMusicianMiner.class);
    private final DAO dao;

    public ECMAlbumMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     *
     * The best-selling albums in terms of the number of units sold
     *
     * @Param k the number of albums to be returned.
     */

    public List<Album> bestSellingAlbums(int k) {
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<Album> albums = dao.loadAll(Album.class);
        if (albums.isEmpty())
            return Lists.newArrayList();

        //sort album descending by Units Sold
        return albums
                .stream()
                .filter(album -> album.getUnitsSold() > 0)
                .sorted(Comparator.comparing(Album::getUnitsSold).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }

    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */

    public List<Album> mostSimilarAlbums(int k, Album album) {
        notNull(album);
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<Album> albums = dao.loadAll(Album.class);
        if (albums.isEmpty())
            return Lists.newArrayList();

        List<Album> similarAlbum = Lists.newArrayList();
        for (Album albumDB : albums) {

            boolean similar = isSimilarName(albumDB.getAlbumName(), album.getAlbumName())
                    || isSimilarMusicians(album, albumDB)
                    || isSimilarGroupName(albumDB.getMusicianGroup(), album.getMusicianGroup());

            if (similar) {
                similarAlbum.add(albumDB);
            }
        }

        return similarAlbum.stream()
                            .sorted(Comparator.comparing(Album::getAlbumName))
                            .limit(k)
                            .collect(Collectors.toList());
    }

    /**
     *
     * The highest-rated albums in terms of the average rating score from different sources
     *
     * @Param k the number of albums to be returned.
     */

    public List<Album> highestRatedAlbums(int k) {
        validateArgument(k < 1, K_ERROR_MSG);
        Collection<Album> albums = dao.loadAll(Album.class);
        if (albums.isEmpty())
            return Lists.newArrayList();

        Map<Album, Double> ratedAlbumMap = Maps.newHashMap();
        for (Album albumDB : albums) {
            if(albumDB.getRatings() == null || albumDB.getRatings().isEmpty())
                continue;
            double averageRatingScore = albumDB.getRatings()
                    .stream()
                    .mapToInt(Rating::getScore)
                    .average()
                    .getAsDouble();
            ratedAlbumMap.put(albumDB, averageRatingScore);
        }
        Map<Album, Double> sortedRatedAlbumMap = sortMapByValue(ratedAlbumMap);
        return sortedRatedAlbumMap.keySet().stream().limit(k).collect(Collectors.toList());
    }

    private boolean isSimilarMusicians(Album album, Album albumDB) {
        if ((albumDB.getFeaturedMusicians() == null) || (album.getFeaturedMusicians() == null)){
            return false;
        } else {
            return albumDB.getFeaturedMusicians().stream().anyMatch(musician -> album.getFeaturedMusicians().contains(musician))
                    || album.getFeaturedMusicians().stream().anyMatch(musician -> albumDB.getFeaturedMusicians().contains(musician));
        }
    }

    private boolean isSimilarGroupName(MusicianGroup musicianGroupDB, MusicianGroup musicianGroup) {
        return musicianGroupDB != null
                && musicianGroup != null
                && isSimilarName(musicianGroupDB.getGroupName(), musicianGroup.getGroupName());
    }

    private boolean isSimilarName(String nameDB, String name) {
        return nameDB != null
                && name != null
                && (name.contains(nameDB) || nameDB.contains(name));
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
