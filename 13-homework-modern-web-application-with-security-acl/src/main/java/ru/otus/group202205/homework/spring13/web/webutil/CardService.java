package ru.otus.group202205.homework.spring13.web.webutil;

import org.springframework.ui.Model;

public interface CardService {

  void prepareCard(Model model, ActionMode actionMode);

}
