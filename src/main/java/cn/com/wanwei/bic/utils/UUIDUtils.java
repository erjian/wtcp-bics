package cn.com.wanwei.bic.utils;

import java.util.UUID;

public class UUIDUtils {

    private static UUIDUtils instance = new UUIDUtils();

    private UUIDUtils() {
    }

    public static UUIDUtils getInstance() {
        return instance;
    }

    public String getId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(UUIDUtils.getInstance().getId());
    }

}
