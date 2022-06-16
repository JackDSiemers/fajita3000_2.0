package ch.bbw.controllers;

import ch.bbw.models.Fajita;
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
public class MainController {
    static Logger log = Logger.getLogger("FajitaController");

    @Autowired
    private FajitaRepository fajitaRepository;

    @GetMapping("/")
    public String fajitaForm(Model model) {
        model.addAttribute("fajita", new Fajita());
        return "index";
    }

    @PostMapping("/editprofile")
    public String checkFajitaInfo(@Valid @ModelAttribute Fajita fajita, BindingResult bindingResult, Model model) {
        log.info("check fajita/post");
        fajitaRepository.save(fajita);
        model.addAttribute("person", new Person());
        model.addAttribute("fajitaId", fajita.getId());
        if(bindingResult.hasErrors()){
            return"index";
        }
        return "payment";
    }

}
