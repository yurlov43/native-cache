import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NativeCacheTest {

    @Test
    public void put() {
        NativeCache<Integer> nativeCache = new NativeCache<>(3, Integer.class);
        nativeCache.put("Java", 43);
        nativeCache.put("Python", 33);
        nativeCache.put("C++", 47);
        nativeCache.put("Go", 98);
        Assert.assertEquals(nativeCache.isKey("Go"), true);
        Assert.assertEquals(nativeCache.isKey("C++"), false);
        nativeCache.get("Java");
        nativeCache.get("Go");
        nativeCache.put("C++", 47);
        Assert.assertEquals(nativeCache.isKey("C++"), true);
        Assert.assertEquals(nativeCache.isKey("Python"), false);
        nativeCache.get("Java");
        nativeCache.get("C++");
        nativeCache.put("Python", 33);
        Assert.assertEquals(nativeCache.isKey("C++"), false);
        Assert.assertEquals(nativeCache.isKey("Python"), true);
        Assert.assertEquals(nativeCache.hits[nativeCache.find("Java")], 2);
        Assert.assertEquals(nativeCache.hits[nativeCache.find("Go")], 1);
        Assert.assertEquals(nativeCache.hits[nativeCache.find("Python")], 0);
    }
}