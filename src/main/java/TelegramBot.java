import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    final String BOT_NAME;
    final String BOT_TOKEN;
    final String URL = "https://api.nasa.gov/planetary/apod?api_key=P743s7MiEwkcEtgRdQ8nUD0xNgtvQaSYjPhFmcBO";

    private static final String HELP_TEXT = "Демонстрация моего бота:\n\n" +
            "Вы можете выполнять команды из главного меню слева или набрав команду:\n\n" +
            "Введите /start, чтобы увидеть приветственное сообщение\n\n" +
            "Введите /help, чтобы снова увидеть это сообщение\n\n" +
            "Введите /give, чтобы загрузить изображение NASA\n\n" +
            "Введите /date, чтобы загрузить изображение NASA на определенную дату";

    //Создаем параметры бота и регестрируем его
    public TelegramBot(String BOT_NAME, String BOT_TOKEN) {
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String answer = update.getMessage().getText();

            long chatID = update.getMessage().getChatId();

            switch (answer) {
                case "Старт":
                case "/start":
                    sendMessage("Привет! Я бот, который присылает фото от NASA. Нажмите кнопку 'Ссылка', чтобы " +
                            "получить фото дня", chatID);
                    break;
                case "Помощь":
                case "/help":
                    sendMessage(HELP_TEXT, chatID);
                    break;
                case "Ссылка":
                case "/give":
                    String url = null;
                    try {
                        url = Utils.getUrl(URL);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMessage(url, chatID);
                    break;
                case "/date":
                case "Ввести дату":
                    sendMessage("Введите дату в формате YYYY-MM-DD: ", chatID);
                    break;
                default:
                    if (answer.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        String date = answer;
                        try {
                            sendMessage(Utils.getUrl(URL + "&date=" + date), chatID);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else
                        sendMessage("Я тебя не понимаю", chatID);
                    }
        }
    }
        void sendMessage(String msg, long chatID) {
            SendMessage message = new SendMessage();

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            message.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);

            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRowFirst = new KeyboardRow();
            keyboardRowFirst.add("Старт");
            keyboardRowFirst.add("Помощь");
            keyboard.add(keyboardRowFirst);

            KeyboardRow keyboardRowSecond = new KeyboardRow();
            keyboardRowSecond.add("Ссылка");
            keyboard.add(keyboardRowSecond);

            KeyboardRow keyboardThirdRow = new KeyboardRow();
            keyboardThirdRow.add("Ввести дату");
            keyboard.add(keyboardThirdRow);

            replyKeyboardMarkup.setKeyboard(keyboard);

            message.setChatId(chatID);
            message.setText(msg);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Не смог отослать сообщение");
            }
        }
    @Override
    public String getBotUsername() {

        return BOT_NAME;

    }
    @Override
    public String getBotToken() {

        return  BOT_TOKEN;
    }
}
