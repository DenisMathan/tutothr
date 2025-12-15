package tutothr.chapter;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

@Service
public class ChapterService extends BaseService {

    private ChapterRepositoryI chapterRepository;
    private ChapterPermissionService chapterPermissionService;

    public ChapterService(ChapterRepositoryI chapterRepository, ChapterPermissionService chapterPermissionService) {
        super();
        this.chapterRepository = chapterRepository;
        this.chapterPermissionService = chapterPermissionService;
    }

    @Override
    public void init() {
        fields = List.of(
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea")
        );
    }

    public void deleteChapterById(Long id) {
        chapterRepository.findById(id).ifPresent(chapter -> {
            if (this.chapterPermissionService.isCurrentUserOwner(chapter.getOwnerId())) {
                chapterRepository.delete(chapter);
            }
        });
    }
    public Chapter findById(Long id) {
        return chapterRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        chapterRepository.deleteById(id);
    }

    @Override
    public void save(Object chapter) {
        chapterRepository.save((Chapter) chapter);
    }
    
    
}
