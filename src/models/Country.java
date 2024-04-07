package models;

/**
 * Национальность.
 */
public enum Country {
    USA,
    FRANCE,
    ITALY;

    /**
     * Получает строку со всеми элементами enum'а через запятую.
     * @return enum'ы
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var countries : values()) {
            nameList.append(countries.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
