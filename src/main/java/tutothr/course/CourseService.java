package tutothr.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import tutothr.chapter.ChapterDTO;
import tutothr.common.BaseService;
import tutothr.common.utils.enums.SortDirectionEnum;
import tutothr.course.interfaces.CourseMapperI;
import tutothr.course.interfaces.CourseRepositoryI;

@Service
public class CourseService extends BaseService<CourseDTO, Course> {
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 5;
	private static final CourseSortFieldEnum DEFAULT_SORT_BY = CourseSortFieldEnum.CREATED_AT;
	private static final SortDirectionEnum DEFAULT_SORT_DIRECTION = SortDirectionEnum.DESC;

	private CoursePermissionService coursePermissionService;
	@Autowired
	private CourseMapperI mapper;

	public CourseService(CourseRepositoryI courseRepository, CoursePermissionService coursePermissionService,
			CourseMapperI mapper) {
		super(courseRepository);
		this.coursePermissionService = coursePermissionService;
		this.mapper = mapper;
	}

	@Override
	public CourseDTO mapToDTO(Course entity) {
		CourseDTO result = mapper.toDTO(entity);
		result.setIsOwner(coursePermissionService.isCurrentUserOwner(entity.getOwnerId()));
		if (result.getIsOwner()) {
			result.setAddChapter(new ChapterDTO());
			result.getAddChapter().setCourseId(entity.getId());
		}
		return result;
	}

	@Override
	public Course mapToEntity(CourseDTO dto) {
		return mapper.toEntity(dto);
	}

	// Paul
	/**
	 * Sucht Kurse mit verschiedenen Filtern, Sortierung und Paging.
	 */
	public Page<CourseDTO> search(CourseSearchDTO searchDTO) {
		Specification<Course> spec = buildSpecification(searchDTO);
		Pageable pageable = buildPageable(searchDTO);
		Page<Course> results = repository.findAll(spec, pageable);
		return results.map(this::mapToDTO);
	}

	/**
	 * Baut die Specification aus den Suchparametern. Nur nicht-null Parameter
	 * werden als Filter hinzugefuegt.
	 */
	private Specification<Course> buildSpecification(CourseSearchDTO searchDTO) {
		Specification<Course> spec = (root, query, cb) -> cb.conjunction();

		if (searchDTO.getText() != null && !searchDTO.getText().isBlank()) {
			spec = spec.and(CourseSpecifications.textContains(searchDTO.getText()));
		}

		if (searchDTO.getCategoryIds() != null && !searchDTO.getCategoryIds().isEmpty()) {
			spec = spec.and(CourseSpecifications.hasAnyCategory(searchDTO.getCategoryIds()));
		}

		if (searchDTO.getMinPrice() != null) {
			spec = spec.and(CourseSpecifications.minPrice(searchDTO.getMinPrice()));
		}

		if (searchDTO.getMaxPrice() != null) {
			spec = spec.and(CourseSpecifications.maxPrice(searchDTO.getMaxPrice()));
		}

		if (searchDTO.getMinRating() != null) {
			spec = spec.and(CourseSpecifications.minRating(searchDTO.getMinRating()));
		}

		if (searchDTO.getTutorName() != null && !searchDTO.getTutorName().isBlank()) {
			spec = spec.and(CourseSpecifications.tutorNameContains(searchDTO.getTutorName()));
		}

		if (Boolean.TRUE.equals(searchDTO.getOnlyAvailable())) {
			spec = spec.and(CourseSpecifications.hasAvailableTimeSlots());
		}
		
		if (searchDTO.getHashtag() != null && !searchDTO.getHashtag().isEmpty()) {
			spec = spec.and(CourseSpecifications.hasHashtag(searchDTO.getHashtag()));
		}

		return spec;
	}

	/**
	 * Baut das Pageable-Objekt fuer Sortierung und Paging.
	 */
	private Pageable buildPageable(CourseSearchDTO searchDTO) {
		int page = searchDTO.getPage() != null ? searchDTO.getPage() : DEFAULT_PAGE;
		int size = searchDTO.getSize() != null ? searchDTO.getSize() : DEFAULT_SIZE;
		CourseSortFieldEnum sortBy = searchDTO.getSortBy() != null ? searchDTO.getSortBy() : DEFAULT_SORT_BY;
		SortDirectionEnum sortDirection = searchDTO.getSortDirection() != null ? searchDTO.getSortDirection()
				: DEFAULT_SORT_DIRECTION;

		Sort sort;
		if (SortDirectionEnum.ASC.equals(sortDirection)) {
			sort = Sort.by(sortBy.getFieldName()).ascending();
		} else {
			sort = Sort.by(sortBy.getFieldName()).descending();
		}

		return PageRequest.of(page, size, sort);
	}
}
