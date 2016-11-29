package me.zsj.interessant.utils;

/**
 * @author zsj
 */

public class IDUtils {

    private static final int[] ids = new int[]{
            26, 28, 30, 32, 34, 36, 38,
            4, 8, 14, 22, 24, 2, 18, 6,
            12, 10, 20
    };

    public static boolean isDetermined(int categoryId) {
        for (int id : ids) {
            if (id == categoryId) {
                return true;
            }
        }
        return false;
    }

}
