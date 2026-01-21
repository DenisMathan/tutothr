package tutothr.chapter;

import org.springframework.stereotype.Service;

import tutothr.chapter.interfaces.ChapterMapperI;
import tutothr.chapter.interfaces.ChapterRepositoryI;
import tutothr.common.BaseService;
import tutothr.common.services.FileStorageService;
import tutothr.course.CourseDTO;
import tutothr.course.CourseService;

@Service
public class ChapterService extends BaseService<ChapterDTO, Chapter> {

    private ChapterMapperI mapper;
    private CourseService courseService;
    private FileStorageService fileStorageService;

    public ChapterService(ChapterRepositoryI chapterRepository, ChapterPermissionService chapterPermissionService, ChapterMapperI mapper, CourseService courseService, FileStorageService fileStorageService) {
        super(chapterRepository);

        this.mapper = mapper;
        this.courseService = courseService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ChapterDTO mapToDTO(Chapter entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public Chapter mapToEntity(ChapterDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public void saveDTO(ChapterDTO chapterDTO) {
        Chapter chapter = mapToEntity(chapterDTO);
        chapter.setCourse(this.courseService.findById(chapterDTO.getCourseId()));
        
        // Save first to generate ID
        chapter = repository.save(chapter);
        
        // Handle files
        boolean filesAdded = false;
        if (chapterDTO.getFiles() != null) {
            for (org.springframework.web.multipart.MultipartFile file : chapterDTO.getFiles()) {
                if (!file.isEmpty()) {
                    String url = fileStorageService.store(file, chapter.getId());
                    chapter.getAttachmentUrls().add(url);
                    filesAdded = true;
                }
            }
        }
        
        if (filesAdded) {
            repository.save(chapter);
        }
    }

    @Override
    public ChapterDTO update(ChapterDTO chapterDTO) {
        Chapter existingChapter = findById(chapterDTO.getId());
        
        // Felder manuell übertragen (nur die, die im Formular sind)
        existingChapter.setTitle(chapterDTO.getTitle());
        existingChapter.setDescription(chapterDTO.getDescription());
        existingChapter.setPaywalled(chapterDTO.isPaywalled());
        existingChapter.setPrice(chapterDTO.getPrice());
        // WICHTIG: position und attachmentUrls NICHT überschreiben!
        
        // Handle new files (append to existing)
        if (chapterDTO.getFiles() != null) {
            for (org.springframework.web.multipart.MultipartFile file : chapterDTO.getFiles()) {
                if (!file.isEmpty()) {
                    String url = fileStorageService.store(file, existingChapter.getId());
                    existingChapter.getAttachmentUrls().add(url);
                }
            }
        }
        
        repository.save(existingChapter);
        return mapToDTO(existingChapter);
    }

    public CourseDTO findCourseDTOById(Long id) {
        return this.courseService.findDTOById(id);
    }

    public void deleteAttachment(Long chapterId, int attachmentIndex) {
        Chapter chapter = findById(chapterId);
        if (chapter != null && attachmentIndex >= 0 && attachmentIndex < chapter.getAttachmentUrls().size()) {
            String url = chapter.getAttachmentUrls().get(attachmentIndex);
            
            // Delete file from storage
            fileStorageService.delete(url);

            chapter.getAttachmentUrls().remove(attachmentIndex);
            repository.save(chapter);
        }
    }
}
