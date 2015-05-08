package io.cyberstock.tcdop.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Created by beolnix on 08/05/15.
 */
public class DOMessagesHelper {

    private MessageSource messageSource;
    private Locale locale;

    public DOMessagesHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = Locale.getDefault();
    }

    public String getMsg(String msgCode, Object... args) {
        return messageSource.getMessage(msgCode, args, locale);
    }

    public String getMsg(String msgCode) {
        return messageSource.getMessage(msgCode, null, locale);
    }

}
