package oicq.wlogin_server.utils;

public class TeaUtil {
    // 加解密tea 没什么奇怪的
    public static byte[] encrypt(byte[] data, byte[] key) {
        Cryptor cryptor = new Cryptor();
        return cryptor.encrypt(data, key);
    }

    public static byte[] encrypt(byte[] data, byte[] key, int offset, int len) {
        Cryptor cryptor = new Cryptor();
        return cryptor.encrypt(data, offset, len, key);
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        Cryptor cryptor = new Cryptor();
        return cryptor.decrypt(data, key);
    }

    public static byte[] decrypt(byte[] data, byte[] key, int offset, int len) {
        Cryptor cryptor = new Cryptor();
        return cryptor.decrypt(data, offset, len, key);
    }
}
