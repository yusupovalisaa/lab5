import checks.*;
import checks.managers.CollectionManager;
import checks.managers.CommandsManager;
import checks.managers.WithCsvManager;
import commands.*;
import models.*;

public class Main {
    public static void main(String[] args) {
        var console = new StandartConsole();
        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var withCSV = new WithCsvManager(args[0], console);
        var collectionManager = new CollectionManager(withCSV);
        if (!collectionManager.loadCollection()) {
            System.exit(1);
        }



        var commandsManager = new CommandsManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new UpdateID(console, collectionManager));
            register("remove_by_id", new RemoveByID(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("execute_script", new ExecuteScript(console));
            register("exit", new Exit(console));
            register("addMax", new AddMax(console, collectionManager));
            register("shuffle", new Shuffle(console, collectionManager));
            register("remove_lower", new RemoveLower(console, collectionManager));
            register("filter_less_than_genre", new FilterLessThanGenre(console, collectionManager));
            register("print_ascending", new PrintAscending(console, collectionManager));
            register("print_field_descending_oscars_count", new PrintFieldDescendingOscarsCount(console, collectionManager));
        }};

        new Run(console, commandsManager).interactiveMode();
    }
}
