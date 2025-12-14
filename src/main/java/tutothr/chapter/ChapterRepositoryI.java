package tutothr.chapter;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.common.MyBaseRepository;

public interface ChapterRepositoryI extends MyBaseRepository<Chapter, Long>, JpaRepository<Chapter, Long> {}
