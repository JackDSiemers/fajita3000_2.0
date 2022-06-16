package ch.bbw.controllers;



import ch.bbw.models.Person;
import ch.bbw.repositories.FajitaRepository;
import ch.bbw.repositories.GameRepository;
import ch.bbw.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class PersonController {

    static Logger log = Logger.getLogger("PersonController");


    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/payment")
    public String checkPersonInfo(@Valid @ModelAttribute Person person, BindingResult bindingResult, Model model) {
        log.info("check person info/post");
        personRepository.save(person);
        model.addAttribute("person", person);
        model.addAttribute("")
        if(bindingResult.hasErrors()){
            return"payment";
        }
        return "confirm";
    }
}
