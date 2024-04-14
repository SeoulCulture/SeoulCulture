package seoul.culture.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import seoul.culture.demo.domain.CultureForm;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CultureController {
    @GetMapping("/")
    public String createForm(Model model){
        model.addAttribute("cultureForm", new CultureForm());
        return "index";
    }

    @GetMapping("/moods")
    @ResponseBody
    public List<String> moods(){
        List<String> moods = new ArrayList<>();
        moods.add("행복");
        moods.add("슬픔");
        moods.add("무기력");
        return moods;
    }

    @PostMapping("/culture/search")
    public String showInfo(CultureForm cultureForm){

        return "members/createMemberForm";
    }
}
