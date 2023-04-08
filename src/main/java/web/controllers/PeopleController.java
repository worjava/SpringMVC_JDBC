package web.controllers;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import web.models.Person;

import javax.validation.Valid;
import java.sql.SQLException;


@Controller
@RequestMapping("/people") // is all adress begin a /people

public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) throws SQLException { // модель это костыль для того чтобы работат с представлениями

        model.addAttribute("people", personDAO.index());// добавили метод к пиплк
        return "people/index"; // имя для генерации запрос данного метода
    }

    @GetMapping("/{id}") // add slash /people/id
    public String show(@PathVariable("id") int id, Model model) {// id pus to "id" where @GetMapping..
        // Получаем одного человека из DAO и передадим на отображение
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new") //people /new вернется html форма для создания нового человека
    public String newPerson(Model model) { // в модели мы передаем объект персон
        model.addAttribute("person", new Person()); //!!! будут значения по умолчанию нул
        return "people/new"; //возвращаем шаблон html будет лежать в папке пипл и называться new
    }

    @PostMapping() // адрес не передаем по /people попадаем в метод
    public String creat(@ModelAttribute("person")
                        @Valid Person person, //  для того что бы создать нового человека и положить данные используем ModelAttribut
                        BindingResult bindingResult) throws SQLException {

         if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);// ключ person а объект из форма которые реализуем в шаблоне new
        return "redirect:/people";// когда человек будет добавлен в базу данных вернет страниу people c помощью redirect
    }

    @GetMapping("/{id}/edit")
    // страница для редактирования человека  с помощью гет запроса возращает html страницу человека
    public String edit(Model model, @PathVariable("id") int id) {// извлекаем id из адреса запроса с помощью @PathVariable
        model.addAttribute("person", personDAO.show(id));  // помещаем челоевка с текущим id в мождель

        return "people/edit";
    }

    @PatchMapping("/{id}")//используется для обновления ресурса на сервере
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "people/edit"; // возвращаем страницу для редактирования человека

        personDAO.update(id, person);  // обновляем человека в базе даных который пришел на вход
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) throws SQLException {
        personDAO.delete(id);
        return "redirect:/people";
    }


}

