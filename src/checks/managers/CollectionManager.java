package checks.managers;

import models.Movie;
import models.MovieGenre;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Работа с коллекцией.
 */
public class CollectionManager {
    private int currentId = 1;
    private Map<Integer, Movie> movies = new HashMap<>();
    private ArrayList<Movie> collection = new ArrayList<Movie>();
    private ArrayDeque<String> logStack = new ArrayDeque<String>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final WithCsvManager withCsvManager;

    public CollectionManager(WithCsvManager withCsvManager) {
        this.lastInitTime = lastInitTime;
        this.lastSaveTime = lastSaveTime;
        this.withCsvManager = withCsvManager;
    }

    /**
     * @return последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекции.
     */
    public ArrayList<Movie> getCollection() {
        return collection;
    }

    /**
     * Получает элемент по ID.
     * @param id Movie
     * @return movie по id
     */
    public Movie byId(long id) {
        return movies.get((int) id);
    }

    /**
     * Проверяет содержит ли коллекция Movie.
     * @param m Movie
     * @return Movie m
     */
    public boolean isСontain(Movie m) {
        return m == null || byId((int) m.getId()) != null;
    }

    /**
     * Получает свободный ID.
     * @return id
     */
    public int getFreeId() {
        while (byId(++currentId) != null) ;
        return currentId;
    }


    /**
     * Добавляет Movie.
     * @param m Movie
     * @return true
     */
    public boolean add(Movie m) {
        if (isСontain(m)) return false;
        movies.put((int) m.getId(), m);
        collection.add(m);
        update();
        return true;
    }

    /**
     * Обновляет Movie.
     * @param m Movie
     * @return true
     */
    public boolean update(Movie m) {
        if (!isСontain(m)) return false;
        collection.remove(byId((int) m.getId()));
        movies.put((int) m.getId(), m);
        collection.add(m);
        update();
        return true;
    }

    /**
     * Удаляет Movie по ID.
     * @param id Movie
     * @return true
     */
    public boolean remove(long id) {
        var m = byId((int) id);
        if (m == null) return false;
        movies.remove(m.getId());
        collection.remove(m);
        update();
        return true;
    }

    /**
     * Создает транзакцию или добавляет операцию в транзакцию.
     * @param cmd String
     * @param isFirst boolean
     */
    public void addLog(String cmd, boolean isFirst) {
        if (isFirst)
            logStack.push("+");
        if (!cmd.equals(""))
            logStack.push(cmd);
    }

    /**
     * Фиксирует изменения коллекции.
     */
    public void update() {
        Collections.sort(collection);
    }


    /**
     * Сохраняет коллекцию в файл.
     */
    public void saveCollection() {
        withCsvManager.CollectionToFile(collection);
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Загружает коллекцию из файла.
     * @return true
     */
    public boolean loadCollection() {
        movies.clear();
        withCsvManager.ReadCollection(collection);
        lastInitTime = LocalDateTime.now();
        for (var e : collection)
            if (byId((int) e.getId()) != null) {
                collection.clear();
                return false;
            } else {
                if (e.getId() > currentId) currentId = (int) e.getId();
                movies.put((int) e.getId(), e);
            }
        update();
        return true;
    }

    /**
     * @param id movie
     * @param repId введенный id
     * @return true в случае успеха.
     */
    public boolean swap(long id, long repId) {
        var e = byId((int) id);
        var re = byId((int) repId);
        if (e == null) return false;
        if (re == null) return false;
        var ind = collection.indexOf(e);
        var rind = collection.indexOf(re);
        if (ind < 0) return false;
        if (rind < 0) return false;

        e.setId(repId);
        re.setId(id);

        movies.put((int) e.getId(), e);
        movies.put((int) re.getId(), re);

        update();
        return true;
    }

    /**
     * Ищет максимальное значение по полю oscarsCount
     * @return maxOscars
     */
    public long FindMax() {
        long maxOscars = 0;
        for (var m : collection) {
            if (maxOscars < m.getOscarsCount()) {
                maxOscars = m.getOscarsCount();

            }
        }
        return maxOscars;
    }

    /**
     * Мешает коллекцию.
     */
    public void shuffle() {
        if (collection.size() == 2){
            Collections.swap(collection, 0, 1);
        }
        else {
            Collections.shuffle(collection);
        }
    }


    /**
     *  Удаляет элементы меньшие чем заданное число оскаров.
     *  @return Array x, состоящий из ID
     */
    public long[] deleteMins() {
        long x[] = new long[collection.size()];
        int count = 0;
        long min = collection.get(collection.size() - 1).getOscarsCount();
        for (var mov : collection) {
            if (min > mov.getOscarsCount()) {
                x[count] = mov.getId();
                count += 1;
            }
        }
        return x;
    }

    /**
     *  Очищает коллекцию.
     *  @return Array x, состоящий из ID
     */
    public long[] clear() {
        long x[] = new long[collection.size()];
        int count = 0;
        for (var mov : collection) {
            x[count] += mov.getId();
            count++;
        }
        return x;
    }


    /**
     * Выводит элементы, значение поля genre которых меньше заданного
     * @param genre MovieGenre
     * @return Array y, состоящий из ID
     */
    public long[] minGenre(MovieGenre genre) {
        long x[] = new long[collection.size()];
        int c = 0;
        for (var m : collection) {
            if (m.getGenre().compareTo(genre) < 0) {
                x[c] = m.getId();
                c += 1;
            }
        }
        return x;
    }


    /**
     *  Сортирует поля oscarsCount
     *  @return Array y, состоящий из отсортированных коллекций.
     */
    public long[] FieldOscars() {
        long x[] = new long[collection.size()];
        int count = 0;
        for (var mov : collection) {
            x[count] += mov.getOscarsCount();
            count += 1;
        }
        Arrays.sort(x);
        long y[] = new long[collection.size()];
        for (int i = collection.size() - 1; i >= 0; i--) {
            y[i] = x[collection.size() - i - 1];
        }
        return y;
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var m : collection) {
            info.append(m.toString() + "\n\n");
        }
        return info.toString().trim();
    }


}