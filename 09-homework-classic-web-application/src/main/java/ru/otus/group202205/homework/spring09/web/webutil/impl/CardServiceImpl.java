package ru.otus.group202205.homework.spring09.web.webutil.impl;

import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.otus.group202205.homework.spring09.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring09.web.webutil.CardService;

@Component
public class CardServiceImpl implements CardService {

  @Override
  public void prepareCard(Model model, ActionMode actionMode) {
    addPageTitleToModel(model,
        actionMode);
    addFlagAttributesToModel(model,
        actionMode);
    putSubmitButtonCaptionInModel(model);
  }

  private void addPageTitleToModel(Model model, ActionMode actionMode) {
    String entityName = ((String) model.getAttribute("entityClass")).toLowerCase(Locale.ROOT);
    switch (actionMode) {
      case READ: {
        model.addAttribute("pageTitle",
            String.format("Read %s page",
                entityName));
        return;
      }
      case CREATE: {
        model.addAttribute("pageTitle",
            String.format("Add new %s",
                entityName));
        return;
      }
      case UPDATE: {
        model.addAttribute("pageTitle",
            String.format("Update %s page",
                entityName));
      }

    }

  }

  private void addFlagAttributesToModel(Model model, ActionMode actionMode) {
    boolean isView = ActionMode.READ.equals(actionMode);
    boolean isUpdating = ActionMode.UPDATE.equals(actionMode);
    boolean isCreating = ActionMode.CREATE.equals(actionMode);
    model.addAttribute("viewing",
        isView);
    model.addAttribute("editing",
        isCreating || isUpdating);
    model.addAttribute("updating",
        isUpdating);
  }

  private void putSubmitButtonCaptionInModel(Model model) {
    boolean isUpdating = Boolean.parseBoolean(String.valueOf(model.getAttribute("updating")));
    if (isUpdating) {
      model.addAttribute("submitButtonCaption",
          "Update");
      return;
    }
    boolean editing = Boolean.parseBoolean(String.valueOf(model.getAttribute("editing")));
    if (editing) {
      model.addAttribute("submitButtonCaption",
          "Create");
    }
  }

}
