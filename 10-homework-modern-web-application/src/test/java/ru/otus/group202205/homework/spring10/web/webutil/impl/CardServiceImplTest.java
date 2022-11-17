package ru.otus.group202205.homework.spring10.web.webutil.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import ru.otus.group202205.homework.spring10.web.webutil.ActionMode;
import ru.otus.group202205.homework.spring10.web.webutil.CardService;

@SpringBootTest(classes = CardServiceImpl.class)
class CardServiceImplTest {

  @Autowired
  private CardService cardService;

  @Test
  void shouldBeAddedCreatePageAttributes() {
    ConcurrentModel model = new ConcurrentModel();
    model.addAttribute("entityClass",
        "Entity");
    cardService.prepareCard(model,
        ActionMode.CREATE);
    assertThat(model.getAttribute("viewing"))
        .isNotNull()
        .isEqualTo(false);
    assertThat(model.getAttribute("editing"))
        .isNotNull()
        .isEqualTo(true);
    assertThat(model.getAttribute("updating"))
        .isNotNull()
        .isEqualTo(false);
    assertThat(model.getAttribute("pageTitle"))
        .isNotNull()
        .isEqualTo("Add new entity");
    assertThat(model.getAttribute("submitButtonCaption"))
        .isNotNull()
        .isEqualTo("Create");
  }

  @Test
  void shouldBeAddedReadPageAttributes() {
    ConcurrentModel model = new ConcurrentModel();
    model.addAttribute("entityClass",
        "Entity");
    cardService.prepareCard(model,
        ActionMode.READ);
    assertThat(model.getAttribute("viewing"))
        .isNotNull()
        .isEqualTo(true);
    assertThat(model.getAttribute("editing"))
        .isNotNull()
        .isEqualTo(false);
    assertThat(model.getAttribute("updating"))
        .isNotNull()
        .isEqualTo(false);
    assertThat(model.getAttribute("pageTitle"))
        .isNotNull()
        .isEqualTo("Read entity page");
    assertThat(model.getAttribute("submitButtonCaption")).isNull();
  }

  @Test
  void shouldBeAddedUpdatePageAttributes() {
    ConcurrentModel model = new ConcurrentModel();
    model.addAttribute("entityClass",
        "Entity");
    cardService.prepareCard(model,
        ActionMode.UPDATE);
    assertThat(model.getAttribute("viewing"))
        .isNotNull()
        .isEqualTo(false);
    assertThat(model.getAttribute("editing"))
        .isNotNull()
        .isEqualTo(true);
    assertThat(model.getAttribute("updating"))
        .isNotNull()
        .isEqualTo(true);
    assertThat(model.getAttribute("pageTitle"))
        .isNotNull()
        .isEqualTo("Update entity page");
    assertThat(model.getAttribute("submitButtonCaption"))
        .isNotNull()
        .isEqualTo("Update");
  }

}