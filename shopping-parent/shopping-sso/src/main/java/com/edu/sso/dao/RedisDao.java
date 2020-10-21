package com.edu.sso.dao;

public interface RedisDao {
    public String set(String key, String value);
    public String get(String key);
    public long hset(String hkey, String key, String value);
    public String hget(String hkey, String key);
    public long incr(String key);
    public long del(String key);
    public long hdel(String hkey, String key);
    public long expire(String key, int seconds);
}
