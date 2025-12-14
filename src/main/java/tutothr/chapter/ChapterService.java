package tutothr.chapter;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

@Service
public class ChapterService extends BaseService {

    private ChapterRepositoryI chapterRepository;

    public ChapterService(ChapterRepositoryI chapterRepository) {
        super();
        this.chapterRepository = chapterRepository;
    }

    @Override
    public void init() {
        fields = List.of(
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea")
        );
        
    }
    
}
