package com.company.bpmtest.view.inputdialogtaskprocessform;

import com.company.bpmtest.view.main.MainView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import io.jmix.bpm.data.form.FormOutcome;
import io.jmix.bpmflowui.event.TaskCompletedUiEvent;
import io.jmix.bpmflowui.processform.ProcessFormContext;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.StandardOutcome;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.bpmflowui.view.inputdialogform.InputDialogTaskProcessForm;
import org.flowable.engine.ProcessEngines;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route(value = "bpm/inputdialogtaskprocessform", layout = MainView.class)
@ViewController("bpm_InputDialogTaskProcessForm")
@ViewDescriptor("bpmtest-input-dialog-task-process-form.xml")
public class BpmtestInputDialogTaskProcessForm extends InputDialogTaskProcessForm {

    @Autowired
    private Notifications notifications;
    @Autowired
    private ProcessFormContext processFormContext;

    private View originalOrigin;

    @Override
    protected void createHorizontalLayout(List<FormOutcome> outcomes) {
        if (outcomes != null && !outcomes.isEmpty()) {
            for (FormOutcome outcome : outcomes) {
                outcomesPanel.add(createOutcomeBtn(outcome));
            }
        } else {
            // create previous task button only for Second Task
            if ((task.getName()).equals("Second Task")) {
                outcomesPanel.add(createPreviousTaskBtn());
            }
            // create next task button only for First Task, otherwise just make normal complete button from super
            if ((task.getName()).equals("First Task")) {
                outcomesPanel.add(createNextTaskBtn());
            } else {
                outcomesPanel.add(createCompleteTaskBtn());
            }
        }
    }

    //used code from InputDialogTaskProcessForm.createCompleteTaskBtn and hardcoded move to Second Task
    protected JmixButton createNextTaskBtn() {
        JmixButton button = uiComponents.create(JmixButton.class);
        BaseAction outcomeBtnAction = new BaseAction("nextTask")
//                .withText(messageBundle.getMessage("completeTask"))
                .withText("Next Task")
                .withIcon(ComponentUtils.convertToIcon(VaadinIcon.CHECK))
                .withHandler(actionPerformedEvent -> {
                    ValidationErrors validationErrors = viewValidation.validateUiComponents(getContent());
                    if (!validationErrors.isEmpty()) {
                        viewValidation.showValidationErrors(validationErrors);
                        return;
                    }
                    Map<String, Object> processVariables = collectProcessVariables(formData);
                    bpmTaskService.complete(task.getId(), processVariables);
//                    View originView = this;
                    close(StandardOutcome.SAVE);
                    eventPublisher.publishEvent(new TaskCompletedUiEvent(this, task));
                    //
                    Task nextTask = bpmTaskService.createTaskQuery().processInstanceId(this.task.getProcessInstanceId()).active().singleResult();
                    processFormViews.openTaskProcessForm(nextTask, getOriginalOrigin());

                });
        button.setAction(outcomeBtnAction);
        return button;
    }

    protected JmixButton createPreviousTaskBtn() {
        JmixButton button = uiComponents.create(JmixButton.class);
        BaseAction testBtnAction = new BaseAction("testTask")
                .withText("Previous Task Button")
                .withIcon(ComponentUtils.convertToIcon(VaadinIcon.CHECK))
                .withHandler(actionPerformedEvent -> {
                    ProcessEngines.getDefaultProcessEngine().getRuntimeService()
                            .createChangeActivityStateBuilder()
                            .moveExecutionToActivityId(task.getExecutionId(), "Activity_03n9jck")
                            .changeState();
                    Task previousTask = bpmTaskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().singleResult();
                    close(StandardOutcome.CLOSE);
                    processFormViews.openTaskProcessForm(previousTask, getOriginalOrigin());
                });
        button.setAction(testBtnAction);
        return button;
    }

    public View getOriginalOrigin() {
        return originalOrigin;
    }

    public void setOriginalOrigin(View originalOrigin) {
        this.originalOrigin = originalOrigin;
    }

}
