package tutothr.chapter;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

// @Service
// public class ChapterService extends BaseService<BaseDTO, Chapter> {

//     private ChapterPermissionService chapterPermissionService;

//     public ChapterService(ChapterRepositoryI chapterRepository, ChapterPermissionService chapterPermissionService) {
//         super(chapterRepository);
//         this.chapterPermissionService = chapterPermissionService;
//     }

//     @Override
//     public void init() {
//         fields = List.of(
//             new Field("title", "Titel", "text"),
//             new Field("description", "Beschreibung", "textarea")
//         );
//     }

//     public void deleteChapterById(Long id) {
//         repository.findById(id).ifPresent(chapter -> {
//             if (this.chapterPermissionService.isCurrentUserOwner(chapter.getOwnerId())) {
//                 repository.delete(chapter);
//             }
//         });
//     }
    
//     @Override
//     public List<BaseDTO> getAllDTOs() {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }

//     @Override
//     public BaseDTO mapToDTO(Chapter entity) {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }

//     @Override
//     public Chapter mapToEntity(BaseDTO dto) {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }
// }
