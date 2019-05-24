package my.fileManager.utilities;

public class StringUtility {
    public static String strJoin(String[] array, char sep) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < array.length; index++) {
            if (index > 0)
                stringBuilder.append(sep);
            stringBuilder.append(array[index]);
        }
        return stringBuilder.toString();
    }
}
