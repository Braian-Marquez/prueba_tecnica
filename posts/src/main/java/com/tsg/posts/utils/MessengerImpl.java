package com.tsg.posts.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import com.tsg.commons.models.enums.CodeEnum;
import java.util.Locale;


@Component
public class MessengerImpl implements Messenger{

    private final MessageSource message;
    

    public MessengerImpl(MessageSource message) {
		super();
		this.message = message;
	}

	public String getMessage(CodeEnum code) {
        return message.getMessage(code.name(),null, Locale.US);
    }

    public String getMessage(CodeEnum code, Object... args){
        return message.getMessage(code.name(), args, Locale.getDefault());
    }
}
