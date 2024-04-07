package checks.managers;

import checks.StandartConsole;
import models.*;
import au.com.bytecode.opencsv.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 *  Работа с файлом.
 */
public class WithCsvManager {
    private final String filename;
    private final StandartConsole console;

    public WithCsvManager(String filename, StandartConsole console) {
        this.filename = filename;
        this.console = console;
    }

    /**
     *   Преобразует коллекцию в CSV-строку.
     */
    private String CollectionToCSV(Collection<Movie> collection) {
        try {
            StringWriter sw = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(sw, ';');
            for (var e : collection) {
                csvWriter.writeNext(Movie.toArray(e));
            }
            String csv = sw.toString();
            return csv;
        } catch (Exception e) {
            console.printError("Ошибка сериализации");
            return null;
        }
    }

    /**
     *     Записывает коллекцию в файл.
     */
    public void CollectionToFile(Collection<Movie> collection) {
        try {
            var csv = CollectionToCSV(collection);
            if (csv == null) return;
            String currentDir = System.getProperty("user.dir");
            FileOutputStream outputStream = new FileOutputStream(currentDir + '/' + this.filename);
            BufferedOutputStream buffOutputStr = new BufferedOutputStream(outputStream);
            try {
                buffOutputStr.write(csv.getBytes());
                buffOutputStr.flush();
                buffOutputStr.close();
                console.println("Коллекция успешна сохранена в файл!");
            } catch (IOException e) {
                console.printError("Неожиданная ошибка сохранения");
            }
        } catch (FileNotFoundException | NullPointerException e) {
            console.printError("Файл не найден");
        }
    }

    /**
     *     Преобразует CSV-строку в коллекцию.
     */
    private ArrayList<Movie> CsvToCollection(String s) {
        try {
            StringReader sr = new StringReader(s);
            CSVReader csvReader = new CSVReader(sr, ';');
            ArrayList<Movie> ds = new ArrayList<Movie>();
            String[] record = null;
            while ((record = csvReader.readNext()) != null) {

                Movie m = Movie.fromArray(record);
                if (m.validate()) {
                    ds.add(m);
                }
                else {
                    console.printError("Файл с колекцией содержит не действительные данные");
                }
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            console.printError("Ошибка десериализации");
            return null;
        }
    }

    /**
     *     Считывает коллекцию из файла.
     */
    public void ReadCollection(Collection<Movie> collection) {
        if (this.filename != null && !this.filename.isEmpty()) {
            String currentDir = System.getProperty("user.dir");
            try (var fileReader = new Scanner(new File(currentDir + '/' +this.filename))) {
                var s = new StringBuilder("");
                while (fileReader.hasNextLine()) {
                    s.append(fileReader.nextLine());
                    s.append("\n");
                }
                collection.clear();
                for (var e : CsvToCollection(s.toString()))
                    collection.add(e);
                if (collection != null) {
                    console.println("Коллекция успешна загружена!");
                    console.println("Введите команду 'help', чтобы получить справки по всем доступным командам!");
                    return;
                } else
                    console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден! Был создан файл с введённым названием. Вы можете ввести команду 'help' для получения справки по доступным командам и продолжить работу.");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NullPointerException exception){
                console.printError("Загрузочный файл некорректный! Вы можете ввести команду 'help' для получения справки по доступным командам и продолжить работу.");
            }
        } else {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
        }
        collection = new ArrayList<Movie>();
    }
}

