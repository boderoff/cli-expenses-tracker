package main.java.com.ledgerlite.app;

public enum Command {
    HELP("help","Список команд"),
    ADD_INCOME("Доход", "Добавить новый доход"),
    ADD_EXPENSE("Трата", "Добавить новую строку расхода"),
    LIST("List", "Список транзакций"),
    BALANCE("Balance", "Текущий баланс"),
    ADD_CATEGORY("Добавить категорию", "Добавить категорию трат"),
    LIST_CATEGORY("Категории", "Доступные категории"),
    REMOVE("Remove", "Удалить транзакцию"),
    EXIT("Exit", "exit"),
    UNKNOWN("","");

    private final String text;
    private final String description;

    Command(String text, String description){
        this.text = text;
        this.description = description;
    }

    public static Command fromString(String text) {
        for (Command cmd : values()){
            if (cmd.text.equalsIgnoreCase(text.trim())){
                return cmd;
            }
        }
        return UNKNOWN;
    }
    public static String getHelp(){
        StringBuilder sb = new StringBuilder("Доступные команды:\n");
        for (Command cmd : values()){
            if (cmd != UNKNOWN){
                sb.append(String.format(" %-20s %s\n", cmd.text, cmd.description));
            }
        }
        return sb.toString();
    }
}
