package ru.etoqacursBot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.etoqacursBot.impl.getLessonImpl;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
            if (update.hasMessage()){
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message){
        if (message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()){
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command){
                    case "/get_lesson":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        getLesson getles = new getLessonImpl();
                        for (Map.Entry<String,String> map : getles.getLesson().entrySet()){
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(map.getKey())
                                            .callbackData(map.getKey())
                                            .url(map.getValue())
                                            .build()
                            ));
                        }
                        execute(SendMessage.builder()
                                .text("Please select the lesson you would like")
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());


                        return;
                    case "/start":
                        execute(SendMessage.builder()
                                .text("Hi " + message.getFrom().getFirstName())
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                }
            }
        }
        else {
            if (message.hasText()){
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Please choose lesson via command /get_lesson")
                        .build());
            }
        }
    }

    @Override
    public String getBotUsername(){
        return "@EtoQACursBot";
    }

    @Override
    public String getBotToken(){
        return "5184614799:AAEm5E8rnvjOKLmOEBxFer81V21_XwKcXCI";
    }

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot();
        bot.execute(SendMessage.builder().chatId("501162546").text("hi").build());
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}
