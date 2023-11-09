package ru.itsyn.jmix.menu_editor.util;

import com.vaadin.flow.spring.annotation.UIScope;
import io.jmix.core.Messages;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
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
                .withHeader(caption)
                .withText(message)
                .withActions(actions);
    }

}
