package ru.practice.cryptobot.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.practice.cryptobot.dto.NotificationType;
import ru.practice.cryptobot.service.CryptoCurrencyService;
import ru.practice.cryptobot.utils.TextUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "To subscribe for the exact BTC price in USD for buy";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();

        String result = cryptoCurrencyService.subscribe(message.getChatId(), message.getFrom(), message.getText());

        answer.setChatId(message.getChatId());

        try {
            answer.setText("Current BTC price: " + TextUtil.toString(cryptoCurrencyService.getBitcoinPrice()) + " USD");
            absSender.execute(answer);
            answer.setText(result);
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        };
    }
}