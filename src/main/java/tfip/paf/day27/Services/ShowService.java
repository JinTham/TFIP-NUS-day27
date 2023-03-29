package tfip.paf.day27.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.paf.day27.Models.Comment;
import tfip.paf.day27.Models.Show;
import tfip.paf.day27.Repositories.ShowRepository;

@Service
public class ShowService {
    
    @Autowired
    private ShowRepository showRepo;

    public Optional<Show> getShow(String showName) {
		List<Show> results = showRepo.getShow(showName);
		if (results.isEmpty())
			return Optional.empty();
		// return the first result
		return Optional.of(results.get(0));
	}

    public void insertComment(Comment comment) {
        showRepo.insertComment(comment);
    }

    public void findShowsByType(String type) {
        showRepo.findShowsByType(type);
    }

    public void groupShowsByTimezone() {
        showRepo.groupShowsByTimezone();
    }

    public void summarizeShows(String type) {
        showRepo.summarizeShows(type);
    }

    public void showCategories() {
        showRepo.showCategories();
    }

}
