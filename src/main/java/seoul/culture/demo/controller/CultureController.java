package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seoul.culture.demo.dto.CultureSearchForm;
import seoul.culture.demo.pathfinder.ReverseGeocoding;
import seoul.culture.demo.service.SearchService;
import seoul.culture.demo.util.Formatter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CultureController {
    private final SearchService searchService;
    private final ReverseGeocoding reverseGeocoding;

    @GetMapping("/")
    public String createCultureForm(Model model){
        CultureSearchForm cultureSearchForm = new CultureSearchForm();
        model.addAttribute("cultureForm", cultureSearchForm);
        cultureSearchForm.setHowToGo("WALKING");
        return "index";
    }

    @RequestMapping(value = "/culture/search")
    // get, post 둘다 받아야 한다. post로 첫 요청 받아도, 렌더링하려면 get 을 날리는 듯?
    public String showInfo(CultureSearchForm formData, Model model) throws IOException {
        if (formData.getPlace() == null && (formData.getLatitude() == null || formData.getLongitude() == null)) {
            throw new IllegalArgumentException("위경도정보없음");
        }

        Map<String, Object> map = searchService.search(formData);

        CultureSearchForm cultureSearchForm = new CultureSearchForm();
        cultureSearchForm.setHowToGo(formData.getHowToGo());
        cultureSearchForm.setPlace(
                formData.getHowToGo().equals("DRIVING") ? "DRIVING" : "WALKING"
        );
        model.addAttribute("cultureForm", cultureSearchForm);
        model.addAttribute("markerInfo", map.get("cultures"));
        model.addAttribute("placeInfo", map.get("places"));
        model.addAttribute("latitude", map.get("lat"));
        model.addAttribute("longitude", map.get("lon"));
        return "map";
    }

    @GetMapping("/culture/address")
    @ResponseBody
    public String getAddress(@RequestParam double srcLat, @RequestParam double srcLon,
                                          @RequestParam double dstLat, @RequestParam double dstLon,
                                          @RequestParam String howToGo) throws IOException {
        Map<String, String> address = reverseGeocoding.getAddress(dstLat, dstLon);
        return Formatter.toAddress(address);
    }
}