package com.company.bpmtest.app;

import com.company.bpmtest.view.inputdialogtaskprocessform.BpmtestInputDialogTaskProcessForm;
import io.jmix.bpmflowui.processform.viewcreator.impl.InputDialogProcessFormViewCreator;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.OpenMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static io.jmix.bpmflowui.processform.viewcreator.ProcessFormHelper.getOpenMode;

@Component("bpmtest_BpmtestInputDialogProcessFormViewCreator")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BpmtestInputDialogProcessFormViewCreator extends InputDialogProcessFormViewCreator {
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private ViewNavigators viewNavigators;

    // copied from InputDialogProcessFormViewCreator.createUserTaskView,
    // then InputDialogTaskProcessForm has been overrided with BpmtestInputDialogTaskProcessForm
    public DialogWindow<?> createUserTaskView(CreationContext creationContext) {
        if (getOpenMode(creationContext.getFormData()) == OpenMode.DIALOG) {
            DialogWindow<BpmtestInputDialogTaskProcessForm> taskProcessForm =
                    dialogWindows.view(creationContext.getOrigin(), BpmtestInputDialogTaskProcessForm.class)
                            .build();
            taskProcessForm.getView().setTask(creationContext.getTask());
            //
            taskProcessForm.getView().setOriginalOrigin(creationContext.getOrigin());
            return taskProcessForm;
        } else {
            viewNavigators.view(BpmtestInputDialogTaskProcessForm.class)
                    .withBackwardNavigation(true)
                    .withAfterNavigationHandler(event -> {
                        event.getView().setTask(creationContext.getTask());
                        //
                        event.getView().setOriginalOrigin(creationContext.getOrigin());
                    })
                    .navigate();
            return null;
        }
    }

    /*@Override
    public DialogWindow<?> createStartProcessView(CreationContext creationContext) {
        // please, make the same for InputDialogStartProcessForm when BpmtestInputDialogStartProcessForm will be created
    }*/
}