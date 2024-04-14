package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seoul.culture.demo.domain.CultureForm;

@Controller
@RequiredArgsConstructor
public class CultureController {
    @GetMapping("/")
    public String createCultureForm(Model model){
        model.addAttribute("cultureForm", new CultureForm());
        return "index";
    }

    @GetMapping("/culture/search")
    public String showInfo(CultureForm cultureForm, Model model){
        // 원래는 여기서 사용자의 입력에 따라 적당한 culture들을 찾아서 Model에 담아야한다.
        // 우선은 데이터만 잘 전달되는지 확인하자
        model.addAttribute("data", cultureForm);
        return "search";
    }
}
