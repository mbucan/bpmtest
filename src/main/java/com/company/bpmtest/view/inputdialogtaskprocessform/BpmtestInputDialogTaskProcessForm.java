package com.company.bpmtest.view.inputdialogtaskprocessform;

import com.company.bpmtest.view.main.MainView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.bpmflowui.event.TaskCompletedUiEvent;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.StandardOutcome;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.bpmflowui.view.inputdialogform.InputDialogTaskProcessForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Route(value = "bpm/inputdialogtaskprocessform", layout = MainView.class)
@ViewController("bpm_InputDialogTaskProcessForm")
@ViewDescriptor("bpmtest-input-dialog-task-process-form.xml")
public class BpmtestInputDialogTaskProcessForm extends InputDialogTaskProcessForm {

    @Autowired
    private Notifications notifications;

    @Override
    protected void createHorizontalLayout(List<FormOutcome> outcomes) {
        if (outcomes != null && !outcomes.isEmpty()) {
            for (FormOutcome outcome : outcomes) {
                outcomesPanel.add(createOutcomeBtn(outcome));
            }
        } else {
            outcomesPanel.add(createCompleteTaskBtn());
            outcomesPanel.add(createTestBtn());
        }
    }

    protected JmixButton createTestBtn() {
        JmixButton button = uiComponents.create(JmixButton.class);
        BaseAction testBtnAction = new BaseAction("testTask")
                .withText("Test Button")
                .withIcon(ComponentUtils.convertToIcon(VaadinIcon.CHECK))
                .withHandler(actionPerformedEvent -> {
//                    Notifications notifications = getApplicationContext().getBean(Notifications.class);
                    notifications.show("Test notification");
                });
        button.setAction(testBtnAction);
        return button;
    }
}
