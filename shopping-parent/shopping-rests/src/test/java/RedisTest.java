import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class RedisTest {
    @Test
    public void test(){
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.43.49",6379);
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("k1", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("k1"));
        jedis.close();
    }

    @Test
    public void test2(){
        JedisPool pool = new JedisPool("192.168.43.49",6379);
        //连接本地的 Redis 服务
        Jedis jedis = pool.getResource();
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("keys1", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("keys1"));
        jedis.close();
    }

    @Test
    public void test3(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.43.49",7001));
        nodes.add(new HostAndPort("192.168.43.49",7002));
        nodes.add(new HostAndPort("192.168.43.49",7003));
        nodes.add(new HostAndPort("192.168.43.49",7004));
        nodes.add(new HostAndPort("192.168.43.49",7005));
        nodes.add(new HostAndPort("192.168.43.49",7006));
        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("clust1","clust111");
        System.out.println(cluster.get("clust1"));
        cluster.close();
    }
}
