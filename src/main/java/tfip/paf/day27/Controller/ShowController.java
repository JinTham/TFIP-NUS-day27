package tfip.paf.day27.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tfip.paf.day27.Models.Comment;
import tfip.paf.day27.Models.Show;
import tfip.paf.day27.Services.ShowService;

@Controller
@RequestMapping
public class ShowController {
    
    @Autowired
    private ShowService showSvc;

    @GetMapping(path="/search")
    public String getShowCommentPage(@RequestParam String showName, Model model) {
        Optional<Show> opt = showSvc.getShow(showName);
        if (opt.isEmpty()) {
			model.addAttribute("message", "No TV show with the name '%s'".formatted(showName));
			return "notfound";
		}
        model.addAttribute("tvshow",opt.get());
        return "commentPage";
    }

    @PostMapping(path="/comment")
    public String postComment(@ModelAttribute Comment comment, Model model) {
        System.out.printf("---->> comment: %s\n", comment);
        showSvc.insertComment(comment);
        return "redirect:/index.html";
    }

}
