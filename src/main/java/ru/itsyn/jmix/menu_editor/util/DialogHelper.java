package ru.itsyn.jmix.menu_editor.util;

import com.vaadin.spring.annotation.UIScope;
import io.jmix.core.Messages;
import io.jmix.ui.Dialogs;
import io.jmix.ui.action.Action.ActionPerformedEvent;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.action.DialogAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@UIScope
@Component("menu_DialogHelper")
public class DialogHelper {

    @Autowired
    Messages messages;
    @Autowired
    Dialogs dialogs;

    public Dialogs.OptionDialogBuilder newConfirmationDialog(
            String message,
            Consumer<ActionPerformedEvent> action
    ) {
        var caption = messages.getMessage("dialogs.Confirmation");
        var actions = new BaseAction[] {
                new DialogAction(DialogAction.Type.YES).withHandler(action),
                new DialogAction(DialogAction.Type.CANCEL)
        };
        return dialogs.createOptionDialog()
                .withCaption(caption)
                .withMessage(message)
                .withActions(actions);
    }

}
