package models;

/**
 * Цвет глаз
 */
public enum Color {
    GREEN,
    ORANGE,
    WHITE;

    /**
     * Получает строку со всеми элементами enum'а через запятую.
     * @return enum'ы
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var colors : values()) {
            nameList.append(colors.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
