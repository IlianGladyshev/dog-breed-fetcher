package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private HashMap<String, List<String>> cache = new HashMap<>();
    private BreedFetcher breedFetcher;
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        breedFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {
        List<String> list = new ArrayList<>();
        if (!cache.containsKey(breed)) {
            callsMade++;
            try {
                list = breedFetcher.getSubBreeds(breed);
                cache.put(breed, list);
            }
            catch (BreedNotFoundException e) {
                throw new BreedNotFoundException(breed);
            }
        }
        else
            list = cache.get(breed);
        return list;
    }

    public int getCallsMade() {
        return callsMade;
    }
}