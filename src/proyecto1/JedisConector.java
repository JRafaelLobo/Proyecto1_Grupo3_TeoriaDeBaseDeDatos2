package proyecto1;

import redis.clients.jedis.Jedis;

public class JedisConector {
    private final Jedis jedis;

    public JedisConector() {
        // Crear una conexión a un servidor Redis
        jedis = new Jedis("redis-19086.c256.us-east-1-2.ec2.redns.redis-cloud.com", 19086);
        jedis.auth("Ssc2Sld7FVY2wfXosDhwnq6eeRN2VBK0"); // Autenticación si es necesaria
    }

    // Métodos para interactuar con Redis
    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void close() {
        jedis.close();
    }
}
