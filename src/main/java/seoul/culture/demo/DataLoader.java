package seoul.culture.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import seoul.culture.demo.domain.Mood;
import seoul.culture.demo.domain.MoodType;
import seoul.culture.demo.repository.MoodRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final MoodRepository moodRepository;

    @Autowired
    public DataLoader(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    @Override
    public void run(String... args){
        for (MoodType moodType : MoodType.values()) {
            Mood mood =  new Mood(moodType);
            moodRepository.save(mood);
        }
    }
}
